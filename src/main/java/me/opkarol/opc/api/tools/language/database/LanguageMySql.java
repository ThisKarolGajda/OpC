package me.opkarol.opc.api.tools.language.database;

import me.opkarol.opc.api.tools.language.LanguageType;

import java.util.UUID;

public class LanguageMySql implements ILanguageDatabase {
    @Override
    public void savePlayerLanguage(UUID uuid, LanguageType language) {

    }

    @Override
    public LanguageType getPlayerLanguage(UUID uuid) {
        return null;
    }

    @Override
    public void onDisable() {

    }
}
