package me.opkarol.opc.api.commands.suggestions;

import me.opkarol.opc.api.list.OpList;

public interface ISuggestion {
    OpList<String> getSuggestion();
}
