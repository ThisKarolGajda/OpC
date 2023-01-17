package me.opkarol.opc.api.command.simple;

import me.opkarol.opc.api.list.OpList;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public interface SubCommand {
    String getName();

    default String requiredPermission() {
        return null;
    }

    default boolean requiresPermission() {
        return false;
    }

    default boolean hasCustomTabCompletion() {
        return false;
    }

    default List<String> tabComplete(int currentIndex, String[] args) {
        return new ArrayList<>();
    }

    void execute(CommandSender sender, OpList<String> args);
}
