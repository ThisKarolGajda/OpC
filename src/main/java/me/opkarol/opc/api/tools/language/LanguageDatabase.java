package me.opkarol.opc.api.tools.language;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.list.OpList;
import me.opkarol.opc.api.tools.language.database.ILanguageDatabase;
import me.opkarol.opc.api.tools.language.database.LanguageFlat;
import me.opkarol.opc.api.tools.language.database.LanguageMySql;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

import static me.opkarol.opc.api.utils.VariableUtil.getOrDefault;

public class LanguageDatabase {
    private static final OpList<OpPluginLanguage> languages = new OpList<>();
    private static LanguageDatabase languageDatabase;
    private ILanguageDatabase languageTool;

    public LanguageDatabase() {
        FileConfiguration configuration = OpAPI.getPlugin().getConfig();
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
        return getOrDefault(languageDatabase, new LanguageDatabase());
    }

    public OpList<OpPluginLanguage> getLanguages() {
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
