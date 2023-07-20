package me.opkarol.opc.api.tools.language;

import me.opkarol.opc.api.gui.IGuiItemBuilder;
import me.opkarol.opc.api.tools.language.database.LanguageDatabase;
import me.opkarol.opc.api.tools.language.editor.LanguageEditorSection;
import me.opkarol.opc.api.tools.language.types.LanguageObject;
import me.opkarol.opc.api.tools.language.types.LanguageType;
import me.opkarol.opc.api.tools.language.types.LanguageVariable;
import me.opkarol.opc.api.utils.FormatUtils;
import me.opkarol.opc.api.utils.VariableUtil;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

public class Language implements Serializable, IGuiItemBuilder {
    private final LanguageType languageType;
    private Set<LanguageObject> cache = new HashSet<>();
    private LanguageDatabase languageDatabase;

    public Language(LanguageType languageType) {
        this.languageType = languageType;
    }

    public LanguageType getLanguageType() {
        return languageType;
    }

    public String getMessage(String path) {
        LanguageObject object = get(path);
        if (object != null) {
            return FormatUtils.formatMessage(object.getObject());
        }
        return path;
    }

    public void add(String path, @NotNull Object object) {
        remove(path);
        if (object instanceof String string) {
            cache.add(new LanguageObject(path, string));
        } else if (object instanceof LanguageVariable languageVariable) {
            cache.add(new LanguageObject(path, languageVariable.getObject()));
        }else if (object instanceof LanguageObject languageObject) {
            cache.add(languageObject);
        }
    }

    public void remove(String path) {
        cache.removeIf(languagePathWithObjectValue -> languagePathWithObjectValue.getPath().equals(path));
    }

    public LanguageObject get(String path) {
        return cache.stream()
                .filter(languagePathWithObjectValue -> languagePathWithObjectValue.getPath().equals(path))
                .findFirst()
                .orElse(null);
    }

    public Set<LanguageObject> getCache() {
        return cache;
    }

    @Override
    public String getItemName() {
        return "&f&l" + languageType.getLanguage();
    }

    @Override
    public List<String> getItemLore() {
        return List.of("&7Language information",
                " &7• short code » &f&l" + languageType.name(),
                " &7• active » &f&l" + (isActive() ? "&a✔" : "&c✖"),
                "",
                "&7Press &f&lLMB &7to edit this!",
                "&7Press &f&lRMB &7to set this active!");
    }

    @Override
    public String toString() {
        return "Language{" +
                "languageType=" + languageType +
                ", cache=" + cache +
                '}';
    }

    public void setHolder(LanguageDatabase languageDatabase) {
        this.languageDatabase = languageDatabase;
    }

    public void setActive() {
        languageDatabase.setActiveLanguage(this);
    }

    public boolean isActive() {
        return languageDatabase.isActiveLanguage(this);
    }

    public LanguageDatabase getLanguageDatabase() {
        return languageDatabase;
    }

    public List<Object> getItemsForPath(String path) {
        // Remove trailing dot from path if present
        path = VariableUtil.ifEndsWithRemove(path, ".");
        List<Object> list = new ArrayList<>();
        String[] current = path.split("\\.");

        for (LanguageObject pathWithObjectValue : cache) {
            // Skip entries with sections shorter or equal to the current path
            if (pathWithObjectValue.getSections().length <= current.length) {
                continue;
            }

            // Check if the sections are equal
            boolean add = areSectionsEqual(pathWithObjectValue.getSections(), current);
            if (!add) {
                continue;
            }

            if (current.length == pathWithObjectValue.getSections().length - 1) {
                // If it's the last section, add the item if sections are equal
                list.add(pathWithObjectValue);
            } else if (isSectionNotPresent(list, pathWithObjectValue, current.length)) {
                // If it's not the last section and section is not already present in the list, add it
                list.add(pathWithObjectValue.toSection(current.length));
            }
        }

        return list;
    }

    private boolean areSectionsEqual(String[] sections1, String[] sections2) {
        return Arrays.equals(Arrays.copyOf(sections1, sections2.length), sections2);
    }

    private boolean isSectionNotPresent(@NotNull List<Object> list, LanguageObject pathWithObjectValue, int index) {
        return list.stream()
                .filter(object -> object instanceof LanguageEditorSection)
                .noneMatch(section -> ((LanguageEditorSection) section).getSection().equals(pathWithObjectValue.getSections()[index]));
    }

    public void setCache(Set<LanguageObject> cache) {
        this.cache = cache;
    }
}


