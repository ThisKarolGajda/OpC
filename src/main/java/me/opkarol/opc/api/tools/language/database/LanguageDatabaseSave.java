package me.opkarol.opc.api.tools.language.database;

import me.opkarol.opc.api.database.flat.FlatDatabase;
import me.opkarol.opc.api.tools.autostart.IDisable;
import me.opkarol.opc.api.tools.autostart.OpAutoDisable;
import me.opkarol.opc.api.tools.language.Language;
import me.opkarol.opc.api.tools.language.loader.LanguageFileLoader;
import me.opkarol.opc.api.tools.language.types.LanguageType;
import org.bukkit.plugin.Plugin;

public class LanguageDatabaseSave implements IDisable {
    private final FlatDatabase<LanguageDatabase> flatDatabase;
    private LanguageDatabase database;
    private final LanguageFileLoader languageFileLoader;
    private static final String LANGUAGE_SAVE_FILE_NAME = "language-temp-save.yml";


    public LanguageDatabaseSave(Plugin plugin, String directory, LanguageType defaultLanguageType) {
        OpAutoDisable.add(this);
        languageFileLoader = new LanguageFileLoader();
        flatDatabase = new FlatDatabase<>(plugin, "language-database.db");
        database = flatDatabase.loadObject();
        if (database == null) {
            database = new LanguageDatabase(directory, new Language(defaultLanguageType));
        }
        database.loadLanguageValues();
    }

    @Override
    public void onDisable() {
        flatDatabase.saveObject(database);
    }

    public LanguageDatabase getDatabase() {
        return database;
    }

    public LanguageFileLoader getLanguageFileLoader() {
        return languageFileLoader;
    }

    public void save(LanguageType languageType) {
        getLanguageFileLoader().saveToFile(getDatabase().get(languageType), LANGUAGE_SAVE_FILE_NAME);
    }

    public void load(LanguageType languageType) {
        getDatabase()
                .getLanguage(languageType)
                .ifPresent(language -> language.setCache(getLanguageFileLoader().loadFromFile(LANGUAGE_SAVE_FILE_NAME)));
    }
}
