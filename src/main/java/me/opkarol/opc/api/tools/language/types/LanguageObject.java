package me.opkarol.opc.api.tools.language.types;

import me.opkarol.opc.api.gui.IGuiItemBuilder;
import me.opkarol.opc.api.tools.language.Language;
import me.opkarol.opc.api.tools.language.editor.LanguageEditorSection;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class LanguageObject implements IGuiItemBuilder, Serializable {
    private final String path;
    private String object;
    private Language language;

    public LanguageObject(String path, String object) {
        this.path = path;
        this.object = object;
    }

    public LanguageObject(String path) {
        this.path = path;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getPath() {
        return path;
    }

    public String[] getSections() {
        return getPath().split("\\.");
    }

    public String[] getSectionsWithoutLast() {
        String[] sections = getPath().split("\\.");
        return Arrays.copyOf(sections, sections.length - 1);
    }

    public String getLastSection() {
        int length = getSections().length;
        if (length < 1) {
            return "";
        }
        return getSections()[length - 1];
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public String getItemName() {
        return "&f&l" + getLastSection();
    }

    @Override
    public List<String> getItemLore() {
        return List.of("&7Current object: " + (getObject() == null ? "empty" : getObject()),
                "&7Default object (" + language.getLanguageDatabase().getDefaultLanguage().getLanguageType().name() + "): " + language.getLanguageDatabase().defaultLanguage.get(path).getObject(),
                "",
                "&7Press &f&lLMB&7 to edit this!",
                "&7Press &f&lRMB&7 to view this!",
                "&7Press &f&lLMB+SHIFT&7 to copy default into this!");
    }

    public LanguageEditorSection toSection(int i) {
        return new LanguageEditorSection(this, i, LanguageEditorSection.EDIT_TYPE.ADD);
    }

    public String getEntireSection(int length) {
        int endIndex = Math.min(length, getSections().length);
        String[] subArray = Arrays.copyOfRange(getSections(), 0, endIndex);
        return String.join(".", subArray);
    }
}
