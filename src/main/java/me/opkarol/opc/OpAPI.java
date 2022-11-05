package me.opkarol.opc;

import me.opkarol.opc.api.commands.OpCommand;
import me.opkarol.opc.api.files.Configuration;
import me.opkarol.opc.api.list.OpList;
import me.opkarol.opc.api.plugin.OpPlugin;
import org.jetbrains.annotations.NotNull;

public class OpAPI {
    private static OpPlugin plugin;
    private static final OpList<OpCommand> commands = new OpList<>();

    public static OpPlugin getInstance() {
        return plugin;
    }

    public static void init(@NotNull OpPlugin plugin) {
        OpAPI.plugin = plugin;
        logInfo(plugin.getName());
    }
    public static void addCommand(OpCommand command) {
        commands.add(command);
    }

    public static void unregisterCommands() {
        commands.forEach(OpCommand::unregister);
    }

    public static Configuration getConfig() {
        return plugin.getConfiguration();
    }

    public static void logInfo(String message) {
        getInstance().getLogger().info(message);
    }
}