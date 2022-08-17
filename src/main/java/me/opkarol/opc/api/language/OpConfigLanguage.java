package me.opkarol.opc.api.language;

import me.opkarol.opc.api.files.Configuration;
import me.opkarol.opc.api.files.ConfigurationMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class OpConfigLanguage {
    private final Plugin plugin;
    private final LanguageType languageType;
    private final ConfigurationMap configuration;

    public OpConfigLanguage(Plugin plugin, @NotNull LanguageType languageType) {
        this.languageType = languageType;
        this.plugin = plugin;
        this.configuration = new ConfigurationMap(plugin, "lang\\" + languageType.name());
    }

    public Configuration getConfiguration() {
        return configuration.getConfiguration();
    }

    public String getValue(String path) {
        return configuration.getValue(path);
    }

    public String getFormattedValue(String path) {
        return configuration.getFormattedValue(path);
    }

    public LanguageType getLanguageType() {
        return languageType;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
