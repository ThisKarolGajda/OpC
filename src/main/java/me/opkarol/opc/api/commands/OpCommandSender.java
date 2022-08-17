package me.opkarol.opc.api.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public record OpCommandSender(CommandSender sender) {

    public Player getPlayer() {
        return (Player) sender;
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public @Nullable Player getFixedPlayer() {
        if (!isPlayer()) {
            return null;
        }

        return getPlayer();
    }
}
