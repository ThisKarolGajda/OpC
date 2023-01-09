package me.opkarol.opc.api.command.suggestions;

import me.opkarol.opc.api.list.OpList;

public class OpSimpleSuggestion {
    private final OpList<String> suggestions;

    public OpSimpleSuggestion(OpList<String> suggestions) {
        this.suggestions = suggestions;
    }

    public OpSimpleSuggestion(String... suggestion) {
        this(OpList.asList(suggestion));
    }

    public OpList<String> getSuggestions() {
        return suggestions;
    }
}
