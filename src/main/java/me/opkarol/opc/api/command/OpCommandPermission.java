package me.opkarol.opc.api.command;

import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public record OpCommandPermission(String permission, String noPermissionMessage, PERMISSION_TYPE type) {

    public boolean hasPermission(@NotNull CommandSender player, String permission) {
        if (permission == null) {
            return true;
        }
        return player.hasPermission(permission);
    }

    public boolean hasPermission(@NotNull CommandSender player) {
        return hasPermission(player, permission);
    }

    public boolean checkPermission(CommandSender player, String noPermissionMessage) {
        boolean hasPermission = hasPermission(player);
        if (!hasPermission) {
            player.sendMessage(FormatUtils.formatMessage(noPermissionMessage));
        }
        return hasPermission;
    }

    public boolean checkPermission(CommandSender player) {
        return checkPermission(player, noPermissionMessage);
    }

    public enum PERMISSION_TYPE {
        SEE_TAB_COMPLETE, USE_COMMAND
    }
}
