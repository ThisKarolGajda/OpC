package me.opkarol.opc.api.tools;

import me.opkarol.opc.api.location.OpSerializableLocation;
import me.opkarol.opc.api.teleport.OpTeleport;
import me.opkarol.opc.api.teleport.TeleportRegistration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportTool extends SingletonHolder {
    private final OpTeleport teleport;

    public TeleportTool(String path) {
        this.teleport = new OpTeleport(path).setRegistration(new TeleportRegistration());
    }

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
