package me.opkarol.opc.api.tools.language.database;

import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.tools.language.Language;
import me.opkarol.opc.api.tools.language.loader.LanguageLoader;
import me.opkarol.opc.api.tools.language.types.LanguageObject;
import me.opkarol.opc.api.tools.language.types.LanguageType;
import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

public class LanguageDatabase implements Serializable {
    public final Language defaultLanguage;
    private final OpMap<UUID, LanguageType> playerLanguages = new OpMap<>();
    private List<Language> languageList = new ArrayList<>();
    private final String directory;
    private final LanguageLoader languageLoader;
    private LanguageType activeLanguage;
    private OpMap<String, Object> defaultValues;

    public LanguageDatabase(String directory, Language defaultLanguage) {
        this.directory = directory;
        this.languageLoader = new LanguageLoader(this);
        this.defaultLanguage = defaultLanguage;
        addLanguage(defaultLanguage);
        addLanguage(new Language(LanguageType.pl_PL));
        setActiveLanguage(defaultLanguage);
        addAllLanguages();
        loadLanguageValues();
    }

    public void loadLanguageValues() {
        defaultValues = getLanguageLoader().loadConfig(directory);
        if (getLanguageList() == null) {
            return;
        }

        for (Language language : getLanguageList()) {
            setLanguageValues(language);
        }
    }

    public void setLanguageValues(@NotNull Language language) {
        for (String path : defaultValues.keySet()) {
            Object defaultLanguageObject = defaultValues.unsafeGet(path);
            Object object = language.get(path);
            if (object == null) {
                // There is no object for that path, so we use default one
                language.add(path, defaultLanguageObject);
            } else {
                language.add(path, object);
            }
        }
    }

    public List<Language> getLanguageList() {
        return languageList;
    }

    public void addAllLanguages() {
        for (LanguageType type : LanguageType.values()) {
            addLanguage(new Language(type));
        }
    }

    public void addLanguage(@NotNull Language language) {
        language.setHolder(this);
        if (getLanguageList().stream().noneMatch(language1 -> language.getLanguageType() == language1.getLanguageType())) {
            getLanguageList().add(language);
        }
    }

    public boolean removeLanguage(LanguageType languageType) {
        return getLanguageList().removeIf(language -> language.getLanguageType() == languageType);
    }

    public void setLanguageList(List<Language> languageList) {
        this.languageList = languageList;
    }

    public Optional<Language> getLanguage(LanguageType languageType) {
        return getLanguageList().stream()
                .filter(language -> language.getLanguageType() == languageType)
                .findFirst();
    }

    public Optional<Language> getLanguage(UUID uuid) {
        return getLanguage(getPlayerLanguage(uuid));
    }

    public Optional<Language> getLanguage(@NotNull Player player) {
        return getLanguage(player.getUniqueId());
    }

    public Language getDefaultLanguage() {
        return defaultLanguage;
    }

    public String getDirectory() {
        return directory;
    }

    /*
     * Player languages
     */

    public void setPlayerLanguage(UUID uuid, LanguageType type) {
        playerLanguages.set(uuid, type);
    }

    public void setPlayerLanguage(@NotNull Player player, LanguageType type) {
        setPlayerLanguage(player.getUniqueId(), type);
    }

    public LanguageType getPlayerLanguage(UUID uuid) {
        return playerLanguages.getOrDefault(uuid, defaultLanguage.getLanguageType());
    }

    public LanguageType getPlayerLanguage(@NotNull Player player) {
        return getPlayerLanguage(player.getUniqueId());
    }

    /*
     * Languages editor
     */

    public void set(LanguageType type, String path, String object) {
        getLanguage(type).ifPresent(language -> language.add(path, object));
    }

    public String get(LanguageType type, String path) {
        Language language = getLanguage(type).orElse(null);
        if (language == null || language.getMessage(path) == null) {
            return defaultLanguage.getMessage(path);
        }
        return language.getMessage(path);
    }

    public Set<LanguageObject> get(LanguageType type) {
        Language language = getLanguage(type).orElse(null);
        return Objects.requireNonNullElse(language, defaultLanguage).getCache();
    }

    /*
     * Message sender
     */

    public void sendMessage(@NotNull Player player, String path) {
        player.sendMessage(FormatUtils.formatMessage(get(getPlayerLanguage(player), path)));
    }

    public LanguageLoader getLanguageLoader() {
        return languageLoader;
    }

    /*
     * Active language
     */

    public LanguageType getActiveLanguageType() {
        return activeLanguage;
    }

    public Language getActiveLanguage() {
        return getLanguageList().stream()
                .filter(language -> language.getLanguageType() == getActiveLanguageType())
                .findFirst()
                .orElse(null);
    }

    public boolean isActiveLanguage(LanguageType languageType) {
        return getActiveLanguageType() == languageType;
    }

    public boolean isActiveLanguage(@NotNull Language language) {
        return isActiveLanguage(language.getLanguageType());
    }

    public void setActiveLanguage(LanguageType activeLanguage) {
        this.activeLanguage = activeLanguage;
    }

    public void setActiveLanguage(@NotNull Language language) {
        this.activeLanguage = language.getLanguageType();
    }

    @Override
    public String toString() {
        return "LanguageDatabase{" +
                "defaultLanguage=" + defaultLanguage +
                ", playerLanguages=" + playerLanguages +
                ", languageList=" + languageList +
                ", directory='" + directory + '\'' +
                ", languageLoader=" + languageLoader +
                ", activeLanguage=" + activeLanguage +
                '}';
    }
}