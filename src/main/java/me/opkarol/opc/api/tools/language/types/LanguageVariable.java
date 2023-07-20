package me.opkarol.opc.api.tools.language.types;

import me.opkarol.opc.api.tools.language.database.LanguageDatabase;
import org.bukkit.entity.Player;

import java.io.Serializable;

public class LanguageVariable implements Serializable {
    private final String path;
    private String object;
    private final LanguageDatabase languageDatabase;

    public LanguageVariable(String path, LanguageDatabase languageDatabase) {
        this.path = path;
        this.languageDatabase = languageDatabase;
    }

    public LanguageVariable(String path, String object, LanguageDatabase languageDatabase) {
        this.path = path;
        this.object = object;
        this.languageDatabase = languageDatabase;
    }

    public void sendMessage(Player player) {
        languageDatabase.sendMessage(player, getPath());
    }

    public String getPath() {
        return path;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
