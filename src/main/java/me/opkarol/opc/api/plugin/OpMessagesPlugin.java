package me.opkarol.opc.api.plugin;

import me.opkarol.opc.api.commands.OpCommandSender;
import me.opkarol.opc.api.files.ConfigurationMap;
import me.opkarol.opc.api.files.SimpleTranslation;
import me.opkarol.opc.api.files.TranslationObject;
import org.jetbrains.annotations.NotNull;

public abstract class OpMessagesPlugin<O, C> extends OpDatabasePlugin<O, C> {
    private static ConfigurationMap configurationMap;

    @Override
    public void onEnable() {
        super.onEnable();
        configurationMap = new ConfigurationMap(getInstance(), "messages");
        configurationMap.getConfiguration().createConfig();
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

    public static String getValue(String path, @NotNull SimpleTranslation translation) {
        return getMap().getValue(path, translation.getStrings());
    }

    public static String getValue(String path, String[][] objects) {
        return getMap().getValue(path, objects);
    }

    public static void sendMappedMessage(@NotNull OpCommandSender sender, String path, String[][] strings) {
        sender.sendMessage(getValue(path, strings));
    }

    public static void sendMappedMessage(@NotNull OpCommandSender sender, String path) {
        sender.sendMessage(getValue(path));
    }

    public static void sendMappedMessage(@NotNull OpCommandSender sender, String path, SimpleTranslation translation) {
        sender.sendMessage(getValue(path, translation));
    }

    public static @NotNull Runnable getMappedMessage(OpCommandSender sender, String path, String[][] strings) {
        return () -> sender.sendMessage(getValue(path, strings));
    }

    public static @NotNull Runnable getMappedMessage(OpCommandSender sender, String path, SimpleTranslation translation) {
        return () -> sender.sendMessage(getValue(path, translation));
    }

    public static @NotNull Runnable getMappedMessage(OpCommandSender sender, String path) {
        return () -> sender.sendMessage(getValue(path));
    }
}
