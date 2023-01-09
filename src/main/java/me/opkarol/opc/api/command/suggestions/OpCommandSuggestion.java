package me.opkarol.opc.api.command.suggestions;

import me.opkarol.opc.api.command.OpCommandSender;

import java.util.List;
import java.util.function.BiFunction;

public record OpCommandSuggestion(BiFunction<OpCommandSender, String[], List<String>> biFunction) {

    public List<String> apply(OpCommandSender commandSender, String[] args) {
        return biFunction.apply(commandSender, args);
    }
}