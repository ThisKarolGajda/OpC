package me.opkarol.opc.api.command.suggestions;

import me.opkarol.opc.api.list.OpList;

public interface ISuggestion {
    OpList<String> getSuggestion();
}
