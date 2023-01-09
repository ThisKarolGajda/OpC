package me.opkarol.opc.api.tools.language.database;

import me.opkarol.opc.api.tools.language.LanguageType;

import java.util.UUID;

public interface ILanguageDatabase {
    void savePlayerLanguage(UUID uuid, LanguageType language);

    LanguageType getPlayerLanguage(UUID uuid);

    void onDisable();
}
