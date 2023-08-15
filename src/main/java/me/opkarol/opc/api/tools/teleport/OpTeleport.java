package me.opkarol.opc.api.tools.teleport;

import me.opkarol.opc.api.list.OpList;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.map.OpMapBuilder;
import me.opkarol.opc.api.serialization.Serialize;
import me.opkarol.opc.api.tools.location.OpLocation;
import me.opkarol.opc.api.tools.location.OpSerializableLocation;
import me.opkarol.opc.api.tools.permission.PermissionManager;
import me.opkarol.opc.api.tools.runnable.OpRunnable;
import me.opkarol.opc.api.tools.runnable.OpTimerRunnable;
import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import static me.opkarol.opc.api.utils.VariableUtil.getOrDefault;

public class OpTeleport extends Serialize {
    private PermissionManager<Integer> permissionGroup;
    private TeleportSettings settings;
    private OpList<Player> players;
    private OpSerializableLocation location;
    private OpRunnable task;
    private TeleportRegistration registration;

    public OpTeleport(PermissionManager<Integer> permissionGroup, TeleportSettings settings) {
        super(null);
        this.permissionGroup = permissionGroup;
        this.settings = settings;
    }

    public OpTeleport(PermissionManager<Integer> permissionGroup) {
        super(null);
        this.permissionGroup = permissionGroup;
        this.settings = new DefaultTeleportSettings();
    }

    public OpTeleport(int timeToTeleport, TeleportSettings settings) {
        super(null);
        this.permissionGroup = new PermissionManager<>(timeToTeleport);
        this.settings = settings;
    }

    public OpTeleport(int timeToTeleport) {
        super(null);
        this.permissionGroup = new PermissionManager<>(timeToTeleport);
        this.settings = new DefaultTeleportSettings();
    }

    public OpTeleport(int timeToTeleport, String world, double x, double y, double z) {
        super(null);
        this.permissionGroup = new PermissionManager<>(timeToTeleport);
        this.settings = new DefaultTeleportSettings();
        this.setLocation(world, x, y, z);
    }

    public OpTeleport(int timeToTeleport, String world, double x, double z) {
        super(null);
        this.permissionGroup = new PermissionManager<>(timeToTeleport);
        this.settings = new DefaultTeleportSettings();
        this.setHighestLocation(world, x, z);
    }

    public OpTeleport(String world, double x, double y, double z) {
        super(null);
        this.setLocation(world, x, y, z);
        this.settings = new DefaultTeleportSettings();
    }

    public OpTeleport(String world, double x, double z) {
        super(null);
        this.setHighestLocation(world, x, z);
        this.settings = new DefaultTeleportSettings();
    }

    public OpTeleport() {
        super(null);
        settings = new DefaultTeleportSettings();
    }

    public OpTeleport(@NotNull OpMapBuilder<String, Object> objects) {
        super(null);
        objects.getByKey("permissionGroup").ifPresent(permissionGroup -> setPermissionGroup((PermissionManager<Integer>) permissionGroup));
        objects.getByKey("settings").ifPresent(settings -> setSettings((TeleportSettings) settings));
        objects.getByKey("location").ifPresent(location -> setLocation((OpSerializableLocation) location));
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

    public OpList<Player> getPlayers() {
        return getOrDefault(players, new OpList<>());
    }

    public OpTeleport setPlayers(OpList<Player> players) {
        this.players = players;
        return this;
    }

    public OpTeleport addTeleport(Player player) {
        OpList<Player> players = getPlayers();
        players.add(player);
        return setPlayers(players);
    }

    public boolean isSafeLocation(@NotNull OpLocation location) {
        Block feet = location.getLocation().getBlock();
        if (feet.getType().equals(Material.LAVA) || feet.getType().equals(Material.WATER) || !feet.getType().isTransparent() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
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
        loadChunk(location.getLocation().getChunk());

        int playerTeleportLength = (int) group.getPlayerObject(player, PermissionManager.OBJECT_TYPE.INTEGER);
        if (playerTeleportLength <= 1) {
            if (invertEndActionMethod(location, player, settings)) {
                settings.use(player, settings.getOnInvalid());
            }
            return this;
        } else {
            settings.use(player, settings.getOnStart());
        }

        task = new OpTimerRunnable().runTaskTimesDownAsynchronously(
                (onEach, integer) -> {
                    if (integer > 0) {
                        settings.use(player, settings.getOnEach(), "%time%", String.valueOf(integer));
                    }
                },
                end -> new OpRunnable(r -> {
                    if (invertEndActionMethod(location, player, settings)) {
                        settings.use(player, settings.getOnInvalid());
                    }
                }).runTask(), playerTeleportLength);
        return this;
    }

    private void loadChunk(@NotNull Chunk chunk) {
        if (!chunk.isLoaded()) {
            chunk.load();
        }
    }

    private boolean invertEndActionMethod(@NotNull OpSerializableLocation location, Player player, TeleportSettings settings) {
        if (isSafeLocation(location.toLocation())) {
            if (player.teleport(location.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN)) {
                settings.use(player, settings.getOnEnd(), "%location%", location.toFamilyString());
                return false;
            }
        }
        return true;
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

    public OpTeleport teleport(@NotNull OpList<Player> list, OpSerializableLocation location, @NotNull PermissionManager<Integer> group, @NotNull TeleportSettings settings) {
        list.forEach(player -> teleport(player, location, group, settings));
        return this;
    }

    public void cancel(String message) {
        cancel(message, players);
    }

    public void cancel(String message, Player player) {
        cancel(message, OpList.asList(player));
    }

    public void cancel(String message, OpList<Player> players) {
        if (getTask() != null) {
            task.cancelTask();
            if (players != null) {
                final String finalMessage = FormatUtils.formatMessage(message);
                players.forEach(player -> player.sendMessage(finalMessage));
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
        return new OpTeleport(permissionGroup)
                .setRegistration(registration)
                .setLocation(location)
                .setSettings(settings)
                .setPlayers(players);
    }

    @Override
    public @NotNull OpMap<String, Object> serialize() {
        return getMapBuilder()
                .setValue("permissionGroup", permissionGroup)
                .setValue("settings", settings)
                .setValue("location", location);
    }
}
