package me.opkarol.opc.api.plugins;

import me.opkarol.opc.api.command.OpCommandSender;
import me.opkarol.opc.api.file.ConfigurationMap;
import me.opkarol.opc.api.file.SimpleTranslation;
import me.opkarol.opc.api.file.TranslationObject;
import org.jetbrains.annotations.NotNull;

public abstract class OpDatabaseMessagesPlugin<O, C> extends OpDatabasePlugin<O, C> {
    private static ConfigurationMap configurationMap;

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

    @Override
    public void onEnable() {
        configurationMap = new ConfigurationMap(getInstance(), "messages");
        if (!configurationMap.getConfiguration().createConfig()) {
            disablePlugin("Messages file created. Restart the server now, in order to allow plugin load messages.");
        }
        super.onEnable();
    }
}
