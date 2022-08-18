package me.opkarol.opc.api.commands;

import me.opkarol.opc.api.utils.FormatUtils;
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
        return this.isPlayer() ? this.getPlayer() : null;
    }

    public void sendMessage(String message) {
        if (isPlayer()) {
            sender.sendMessage(FormatUtils.formatMessage(message));
        } else {
            sender.sendMessage(message);
        }
    }
}
