package me.opkarol.opc.api.commands.suggestions;

import org.bukkit.command.CommandSender;

import java.util.function.BiFunction;

public class OpCommandSuggestion {
    private final BiFunction<CommandSender, String[], String> biFunction;

    public OpCommandSuggestion(BiFunction<CommandSender, String[], String> biFunction) {
        this.biFunction = biFunction;
    }

    public String apply(CommandSender commandSender, String[] args) {
        return biFunction.apply(commandSender, args);
    }
}
