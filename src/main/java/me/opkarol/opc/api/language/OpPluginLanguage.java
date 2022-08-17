package me.opkarol.opc.api.language;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OpPluginLanguage {
    private final Plugin plugin;
    private final LanguageType[] supportedTypes;
    private final List<OpConfigLanguage> languages = new ArrayList<>();

    public OpPluginLanguage(Plugin plugin, LanguageType @NotNull ... supportedTypes) {
        this.plugin = plugin;
        this.supportedTypes = supportedTypes;

        if (supportedTypes.length == 0) {
            throw new IllegalArgumentException("OpPluginLanguage must contain at least 1 supported language type!");
        }
        for (LanguageType type : supportedTypes) {
            languages.add(new OpConfigLanguage(plugin, type));
        }
    }

    public LanguageType[] getSupportedTypes() {
        return supportedTypes;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public List<OpConfigLanguage> getLanguages() {
        return languages;
    }

    public Optional<OpConfigLanguage> getLanguage(LanguageType type) {
        return languages.stream().filter(language -> language.getLanguageType().equals(type)).findFirst();
    }

    public String getPlayerMessage(UUID uuid, String path) {
        final String[] temp = {null};
        getLanguage(LanguageDatabase.getInstance().getPlayerLanguage(uuid))
                .ifPresent(opConfigLanguage -> temp[0] = opConfigLanguage.getConfiguration().getConfig().getString(path));

        if (temp[0] == null) {
            return languages.get(0).getConfiguration().getConfig().getString(path);
        }

        return path;
    }
}
