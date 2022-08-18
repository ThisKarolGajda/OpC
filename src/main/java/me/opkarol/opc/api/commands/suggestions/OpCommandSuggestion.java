package me.opkarol.opc.api.commands.suggestions;

import me.opkarol.opc.api.commands.OpCommandSender;

import java.util.function.BiFunction;

public record OpCommandSuggestion(
        BiFunction<OpCommandSender, String[], String> biFunction) {

    public String apply(OpCommandSender commandSender, String[] args) {
        return biFunction.apply(commandSender, args);
    }
}