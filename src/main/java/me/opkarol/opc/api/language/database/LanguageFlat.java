package me.opkarol.opc.api.language.database;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.database.flat.FlatDatabase;
import me.opkarol.opc.api.language.LanguageType;
import me.opkarol.opc.api.map.OpMap;

import java.util.UUID;

public class LanguageFlat implements ILanguageDatabase {
    private final FlatDatabase<OpMap<UUID, LanguageType>> database = new FlatDatabase<>(OpAPI.getInstance(), "lang-save");
    private final OpMap<UUID, LanguageType> map;

    public LanguageFlat() {
        OpMap<UUID, LanguageType> map1;
        map1 = database.loadObject();
        if (map1 == null) {
            map1 = new OpMap<>();
        }
        map = map1;
    }

    @Override
    public void savePlayerLanguage(UUID uuid, LanguageType language) {
        map.set(uuid, language);
    }

    @Override
    public LanguageType getPlayerLanguage(UUID uuid) {
        return map.getOrDefault(uuid, null);
    }

    @Override
    public void onDisable() {
        database.saveObject(map);
    }
}
