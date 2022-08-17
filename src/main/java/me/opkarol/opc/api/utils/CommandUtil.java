package me.opkarol.opc.api.utils;

import me.opkarol.opc.api.commands.OpCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandUtil {
    private static final List<OpCommand> commands = new ArrayList<>();

    public static void addCommand(OpCommand command) {
        commands.add(command);
    }

    public static void onDisable() {
        commands.forEach(OpCommand::unregister);
    }
}
