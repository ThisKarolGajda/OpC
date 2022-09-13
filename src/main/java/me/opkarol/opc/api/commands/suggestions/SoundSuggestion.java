package me.opkarol.opc.api.commands.suggestions;

import me.opkarol.opc.api.list.OpList;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;

import java.util.Arrays;

public class SoundSuggestion implements ISuggestion {
    private final OpList<String> soundSuggestion = Arrays.stream(Sound.values())
            .map(Sound::getKey).map(NamespacedKey::getKey)
            .map(s -> s.replace("minecraft:", "").toUpperCase().replace(".", "_"))
            .collect(OpList.getCollector());

    @Override
    public OpList<String> getSuggestion() {
        return soundSuggestion;
    }

}
