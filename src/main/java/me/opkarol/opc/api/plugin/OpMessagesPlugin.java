package me.opkarol.opc.api.plugin;

import me.opkarol.opc.api.files.ConfigurationMap;
import me.opkarol.opc.api.files.TranslationObject;

public abstract class OpMessagesPlugin<O, C> extends OpDatabasePlugin<O, C> {
    private static final ConfigurationMap configurationMap;

    static {
        configurationMap = new ConfigurationMap(getInstance(), "messages");
        configurationMap.getConfiguration().updateConfig();
    }

    public static ConfigurationMap getMap() {
        return configurationMap;
    }

    public static String getValue(String path) {
        return getMap().getValue(path);
    }

    public static String getFormattedValue(String path) {
        return getMap().getFormattedValue(path);
    }

    public static String getValue(String path, TranslationObject... objects) {
        return getMap().getValue(path, objects);
    }

    public static String getValue(String path, String[][] objects) {
        return getMap().getValue(path, objects);
    }
}
