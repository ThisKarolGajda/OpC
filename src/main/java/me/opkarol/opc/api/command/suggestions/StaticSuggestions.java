package me.opkarol.opc.api.command.suggestions;

import me.opkarol.opc.api.map.OpMap;
import org.jetbrains.annotations.NotNull;

public class StaticSuggestions {
    private static final OpMap<SUGGESTION_TYPE, ISuggestion> map = new OpMap<>();

    public static void add(SUGGESTION_TYPE type, ISuggestion suggestion) {
        map.set(type, suggestion);
    }

    public static @NotNull OpSimpleSuggestion get(SUGGESTION_TYPE type) {
        if (map.keySet().size() == 0) {
            load();
        }
        if (map.containsKey(type)) {
            return new OpSimpleSuggestion(map.getMap().get(type).getSuggestion());
        }
        return new OpSimpleSuggestion("");
    }

    public static void load() {
        add(SUGGESTION_TYPE.ENCHANTS, new EnchantSuggestion());
        add(SUGGESTION_TYPE.ONLINE_PLAYERS, new OnlinePlayerSuggestion());
        add(SUGGESTION_TYPE.SOUND, new SoundSuggestion());
        add(SUGGESTION_TYPE.SOUND_CATEGORY, new SoundCategorySuggestion());
        add(SUGGESTION_TYPE.MATERIAL, new MaterialSuggestion());
    }

    public enum SUGGESTION_TYPE {
        ENCHANTS, ONLINE_PLAYERS, SOUND, SOUND_CATEGORY, MATERIAL
    }
}
