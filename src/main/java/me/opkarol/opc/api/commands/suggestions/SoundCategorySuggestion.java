package me.opkarol.opc.api.commands.suggestions;

import org.bukkit.SoundCategory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SoundCategorySuggestion implements ISuggestion {
    private final List<String> soundSuggestion = Arrays.stream(SoundCategory.values())
            .map(SoundCategory::name)
            .collect(Collectors.toList());

    @Override
    public List<String> getSuggestion() {
        return soundSuggestion;
    }
}
