package me.opkarol.opc.api.command.suggestions;

import me.opkarol.opc.api.list.OpList;
import org.bukkit.SoundCategory;

import java.util.Arrays;

public class SoundCategorySuggestion implements ISuggestion {
    private final OpList<String> soundSuggestion = Arrays.stream(SoundCategory.values())
            .map(SoundCategory::name)
            .collect(OpList.getCollector());

    @Override
    public OpList<String> getSuggestion() {
        return soundSuggestion;
    }
}
