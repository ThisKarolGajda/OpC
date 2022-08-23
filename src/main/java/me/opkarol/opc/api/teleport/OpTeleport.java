package me.opkarol.opc.api.teleport;

import me.opkarol.opc.api.commands.OpCommandSender;
import me.opkarol.opc.api.files.Configuration;
import me.opkarol.opc.api.misc.*;
import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static me.opkarol.opc.api.utils.Util.getOrDefault;

public class OpTeleport implements Serializable {
    private PermissionManager<Integer> permissionGroup;
    private TeleportSettings settings;
    private List<Player> players;
    private OpSerializableLocation location;
    private OpRunnable task;
    private TeleportRegistration registration;

    public void saveInConfiguration(Configuration configuration, String path) {
        path = path.endsWith(".") ? path : path.concat(".");
        permissionGroup.saveInConfiguration(configuration, path + "permissionGroup");
        settings.saveInConfiguration(configuration, path + "settings");
        configuration.getConfig().set(path + "location", location.toString());
        configuration.save();
    }

    public OpTeleport(@NotNull Configuration configuration, String path) {
        path = path.endsWith(".") ? path : path.concat(".");
        FileConfiguration config = configuration.getConfig();
        permissionGroup = new PermissionManager<>(configuration, path + "permissionGroup");
        settings = new TeleportSettings(config, path + "settings");
        location = new OpSerializableLocation(config.getString(path + "location"));
    }

    public OpTeleport(PermissionManager<Integer> permissionGroup, TeleportSettings settings) {
        this.permissionGroup = permissionGroup;
        this.settings = settings;
    }

    public OpTeleport(PermissionManager<Integer> permissionGroup) {
        this.permissionGroup = permissionGroup;
        this.settings = new DefaultTeleportSettings();
    }

    public OpTeleport(int timeToTeleport, TeleportSettings settings) {
        this.permissionGroup = new PermissionManager<>(timeToTeleport);
        this.settings = settings;
    }

    public OpTeleport(int timeToTeleport) {
        this.permissionGroup = new PermissionManager<>(timeToTeleport);
        this.settings = new DefaultTeleportSettings();
    }

    public OpTeleport(int timeToTeleport, String world, double x, double y, double z) {
        this.permissionGroup = new PermissionManager<>(timeToTeleport);
        this.settings = new DefaultTeleportSettings();
        this.setLocation(world, x, y, z);
    }

    public OpTeleport(int timeToTeleport, String world, double x, double z) {
        this.permissionGroup = new PermissionManager<>(timeToTeleport);
        this.settings = new DefaultTeleportSettings();
        this.setHighestLocation(world, x, z);
    }

    public OpTeleport(String world, double x, double y, double z) {
        this.setLocation(world, x, y, z);
        this.settings = new DefaultTeleportSettings();
    }

    public OpTeleport(String world, double x, double z) {
        this.setHighestLocation(world, x, z);
        this.settings = new DefaultTeleportSettings();
    }

    public TeleportSettings getSettings() {
        return settings;
    }

    public OpTeleport setSettings(TeleportSettings settings) {
        this.settings = settings;
        return this;
    }

    public PermissionManager<Integer> getPermissionGroup() {
        return permissionGroup;
    }

    public OpTeleport setPermissionGroup(PermissionManager<Integer> permissionGroup) {
        this.permissionGroup = permissionGroup;
        return this;
    }

    public List<Player> getPlayers() {
        return getOrDefault(players, new ArrayList<>());
    }

    public OpTeleport setPlayers(List<Player> players) {
        this.players = players;
        return this;
    }

    public OpTeleport addTeleport(Player player) {
        List<Player> players = getPlayers();
        players.add(player);
        return setPlayers(players);
    }

    public boolean isSafeLocation(@NotNull OpLocation location) {
        Block feet = location.getLocation().getBlock();
        if (!feet.getType().isTransparent() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
            return false;
        }
        Block head = feet.getRelative(BlockFace.UP);
        if (!head.getType().isTransparent()) {
            return false;
        }
        Block ground = feet.getRelative(BlockFace.DOWN);
        return ground.getType().isSolid();
    }

    public OpSerializableLocation getLocation() {
        return location;
    }

    public OpTeleport setLocation(OpSerializableLocation location) {
        this.location = location;
        return this;
    }

    public OpTeleport setLocation(@NotNull OpLocation location) {
        return setLocation(location.toLocation());
    }

    public OpTeleport setLocation(String world, double x, double y, double z) {
        return setLocation(new OpSerializableLocation(x, y, z, 0, 0, Bukkit.getWorld(world)));
    }

    public OpTeleport setHighestLocation(@NotNull OpLocation location) {
        return setLocation(location.getHighestYLocation());
    }

    public OpTeleport setHighestLocation(@NotNull OpSerializableLocation location) {
        return setLocation(location.getHighestYLocation());
    }

    public OpTeleport setHighestLocation(String world, double x, double z) {
        return setLocation(new OpSerializableLocation(x, 0, z, 0, 0, Bukkit.getWorld(world)).getHighestYLocation());
    }

    public OpTeleport teleport(@NotNull Player player, @NotNull OpSerializableLocation location, @NotNull PermissionManager<Integer> group, TeleportSettings teleportSettings) {
        if (teleportSettings == null) {
            teleportSettings = new DefaultTeleportSettings();
        }
        if (getRegistration() != null) {
            registration.addPlayer(player, this);
        }
        TeleportSettings settings = teleportSettings;
        settings.use(player, settings.getOnStart());
        location.getLocation().getChunk().load();
        int playerTeleportLength = (int) group.getPlayerObject(player, PermissionManager.OBJECT_TYPE.INTEGER);
        if (playerTeleportLength < 1) {
            if (endActionMethod(location, player, settings)) {
                return this;
            }
        }
        task = new OpTimerRunnable().runTaskTimesDownAsynchronously((onEach, integer) -> {
            String s = String.valueOf(integer);
            settings.use(player, settings.getOnEach(), "%time%", s);
        }, end -> new OpRunnable(r -> {
            if (!endActionMethod(location, player, settings)) {
                settings.use(player, settings.getOnInvalid());
            }
        }).runTask(), playerTeleportLength);
        return this;
    }

    private boolean endActionMethod(@NotNull OpSerializableLocation location, Player player, TeleportSettings settings) {
        if (isSafeLocation(location.toLocation())) {
            if (player.teleport(location.getLocation())) {
                settings.use(player, settings.getOnEnd(), "%location%", location.toFamilyString());
                return true;
            }
        }
        return false;
    }

    public OpTeleport teleport(OpSerializableLocation location, @NotNull PermissionManager<Integer> group, @NotNull TeleportSettings settings) {
        return teleport(players, location, group, settings);
    }

    public OpTeleport teleportCopied(OpSerializableLocation location, @NotNull PermissionManager<Integer> group, @NotNull TeleportSettings settings) {
        return copy().teleport(players, location, group, settings);
    }

    public OpTeleport teleport(@NotNull PermissionManager<Integer> group, @NotNull TeleportSettings settings) {
        return teleport(players, location, group, settings);
    }

    public OpTeleport teleportCopied(@NotNull PermissionManager<Integer> group, @NotNull TeleportSettings settings) {
        return copy().teleport(players, location, group, settings);
    }

    public OpTeleport teleport(@NotNull TeleportSettings settings) {
        return teleport(players, location, permissionGroup, settings);
    }

    public OpTeleport teleportCopied(@NotNull TeleportSettings settings) {
        return copy().teleport(players, location, permissionGroup, settings);
    }

    public OpTeleport teleport() {
        return teleport(players, location, permissionGroup, settings);
    }

    public OpTeleport teleportCopied() {
        return copy().teleport(players, location, permissionGroup, settings);
    }

    public OpTeleport teleport(Player player) {
        return teleport(player, location, permissionGroup, settings);
    }

    public OpTeleport teleportCopied(Player player) {
        return copy().teleport(player, location, permissionGroup, settings);
    }

    public OpTeleport teleport(@NotNull OpCommandSender sender) {
        if (sender.isPlayer()) {
            return teleport(sender.getPlayer());
        }
        return this;
    }

    public OpTeleport teleportCopied(@NotNull OpCommandSender sender) {
        if (sender.isPlayer()) {
            return copy().teleport(sender.getPlayer());
        }
        return this;
    }

    public OpTeleport teleport(@NotNull List<Player> list, OpSerializableLocation location, @NotNull PermissionManager<Integer> group, @NotNull TeleportSettings settings) {
        list.forEach(player -> teleport(player, location, group, settings));
        return this;
    }

    public void cancel(String message) {
        if (getTask() != null) {
            if (task.cancelTask()) {
                players.forEach(player -> player.sendMessage(FormatUtils.formatMessage(message)));
            }
        }
    }

    public OpRunnable getTask() {
        return task;
    }

    public TeleportRegistration getRegistration() {
        return registration;
    }

    public OpTeleport setRegistration(TeleportRegistration registration) {
        this.registration = registration;
        return this;
    }

    public OpTeleport copy() {
        OpTeleport clone = new OpTeleport(permissionGroup);
        clone.setRegistration(registration);
        clone.setLocation(location);
        clone.setSettings(settings);
        clone.setPlayers(players);
        return clone;
    }
}
