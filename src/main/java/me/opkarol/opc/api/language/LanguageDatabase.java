package me.opkarol.opc.api.language;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.language.database.ILanguageDatabase;
import me.opkarol.opc.api.language.database.LanguageFlat;
import me.opkarol.opc.api.language.database.LanguageMySql;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LanguageDatabase {
    private static LanguageDatabase languageDatabase;
    private static final List<OpPluginLanguage> languages = new ArrayList<>();
    private ILanguageDatabase languageTool;

    public LanguageDatabase() {
        FileConfiguration configuration = OpAPI.getInstance().getConfig();
        if (!configuration.getBoolean("language.enabled")) {
            return;
        }

        languageDatabase = this;
        String type = configuration.getString("language.type");
        if (type == null) {
            languageTool = new LanguageFlat();
            return;
        }

        switch (type) {
            case "flat" -> languageTool = new LanguageFlat();
            case "mysql" -> languageTool = new LanguageMySql();
            default -> throw new IllegalArgumentException("Wrong type of file saving for language!");
        }
    }

    public static synchronized OpPluginLanguage addPluginLanguage(OpPluginLanguage language) {
        languages.add(language);
        return language;
    }

    public static LanguageDatabase getInstance() {
        return languageDatabase == null ? new LanguageDatabase() : languageDatabase;
    }

    public List<OpPluginLanguage> getLanguages() {
        return languages;
    }

    public ILanguageDatabase getLanguageTool() {
        return languageTool;
    }

    public LanguageType getPlayerLanguage(UUID uuid) {
        return getLanguageTool().getPlayerLanguage(uuid);
    }

    public void setPlayerLanguage(UUID uuid, LanguageType type) {
        getLanguageTool().savePlayerLanguage(uuid, type);
    }
}
