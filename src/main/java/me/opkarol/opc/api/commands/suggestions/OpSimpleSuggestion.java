package me.opkarol.opc.api.commands.suggestions;

import java.util.Arrays;
import java.util.List;

public class OpSimpleSuggestion {
    private final List<String> suggestions;

    public OpSimpleSuggestion(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public OpSimpleSuggestion(String... suggestion) {
        this(Arrays.asList(suggestion));
    }

    public List<String> getSuggestions() {
        return suggestions;
    }
}
