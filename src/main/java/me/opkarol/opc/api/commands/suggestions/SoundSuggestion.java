package me.opkarol.opc.api.commands.suggestions;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SoundSuggestion implements ISuggestion {
    private final List<String> soundSuggestion = Arrays.stream(Sound.values())
            .map(Sound::getKey).map(NamespacedKey::getKey)
            .map(s -> s.replace("minecraft:", "").toUpperCase().replace(".", "_"))
            .collect(Collectors.toList());

    @Override
    public List<String> getSuggestion() {
        return soundSuggestion;
    }

}
