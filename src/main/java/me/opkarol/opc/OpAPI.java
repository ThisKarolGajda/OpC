package me.opkarol.opc;

import me.opkarol.opc.api.commands.OpCommand;
import me.opkarol.opc.api.files.Configuration;
import me.opkarol.opc.api.plugin.OpPlugin;

import java.util.ArrayList;
import java.util.List;

public class OpAPI {
    private static OpPlugin plugin;
    private static final List<OpCommand> commands = new ArrayList<>();

    public static OpPlugin getInstance() {
        return plugin;
    }

    public static void init(OpPlugin plugin) {
        OpAPI.plugin = plugin;
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
}