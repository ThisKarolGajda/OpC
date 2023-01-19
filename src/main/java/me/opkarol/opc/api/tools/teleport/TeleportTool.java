package me.opkarol.opc.api.tools.teleport;

import me.opkarol.opc.api.tools.SingletonHolder;
import me.opkarol.opc.api.tools.location.OpSerializableLocation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportTool extends SingletonHolder {
    private final OpTeleport teleport;

    public TeleportTool() {
        this.teleport = new OpTeleport().setRegistration(new TeleportRegistration());
    }

    public TeleportTool(@NotNull OpTeleport teleport) {
        this.teleport = teleport.setRegistration(new TeleportRegistration());
    }

    public OpTeleport getTeleport() {
        return teleport;
    }

    public void teleport(OpSerializableLocation location, Player player) {
        teleport.copy().setLocation(location).teleport(player);
    }
}
