package me.opkarol.opc.api.teleport;

import me.opkarol.opc.api.events.OpEvent;
import me.opkarol.opc.api.map.OpMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TeleportRegistration {
    private final OpMap<UUID, OpTeleport> map = new OpMap<>();
    private final String movedMessage;

    public TeleportRegistration(String movedMessage) {
        this.movedMessage = movedMessage;
        registerMovement();
    }

    public TeleportRegistration() {
        this("You moved! Teleportation cancelled!");
    }

    private void registerMovement() {
        OpEvent.registerEvent(PlayerMoveEvent.class, EventPriority.MONITOR, e -> {
            if (e.getTo() == null || !isTheSameLocation(e.getFrom(), e.getTo())) {
                removeIfHasPlayer(e.getPlayer());
            }
        });
    }

    public void removeIfHasPlayer(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        if (!map.containsKey(uuid)) {
            return;
        }

        OpTeleport teleport = map.getOrDefault(uuid, null);
        teleport.cancel(movedMessage, player);
    }

    public TeleportRegistration addPlayer(UUID uuid, OpTeleport teleport) {
        map.set(uuid, teleport);
        return this;
    }

    public TeleportRegistration addPlayer(@NotNull Player player, OpTeleport teleport) {
        map.set(player.getUniqueId(), teleport);
        return this;
    }

    private boolean isTheSameLocation(@NotNull Location a, @NotNull Location b) {
        return (int) a.getX() == (int) b.getX() && (int) a.getY() == (int) b.getY() && (int) a.getZ() == (int) b.getZ();
    }
}