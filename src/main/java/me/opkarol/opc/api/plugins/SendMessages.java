package me.opkarol.opc.api.plugins;

import me.opkarol.opc.api.command.OpCommandSender;
import me.opkarol.opc.api.file.ConfigurationMap;
import me.opkarol.opc.api.file.SimpleTranslation;
import me.opkarol.opc.api.file.TranslationObject;
import me.opkarol.opc.api.misc.Tuple;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("all")
interface SendMessages {
    ConfigurationMap getConfigurationMap();

    default String getValue(String path) {
        return getConfigurationMap().getValue(path);
    }

    default String getFormattedValue(String path) {
        return getConfigurationMap().getFormattedValue(path);
    }

    default String getValue(String path, TranslationObject... objects) {
        return getConfigurationMap().getValue(path, objects);
    }

    default String getValue(String path, @NotNull SimpleTranslation translation) {
        return getConfigurationMap().getValue(path, translation.getStrings());
    }

    default String getFormattedValue(String path, String[][] strings) {
        return getConfigurationMap().getFormattedValue(path, strings);
    }

    default String getFormattedValue(String path, Tuple<String, String>... tuples) {
        return getConfigurationMap().getFormattedValue(path, tuples);
    }

    default String getValue(String path, String[][] objects) {
        return getConfigurationMap().getValue(path, objects);
    }

    default void sendMappedMessage(@NotNull OpCommandSender sender, String path, String[][] strings) {
        sender.sendMessage(getFormattedValue(path, strings));
    }

    default void sendMappedMessage(@NotNull OpCommandSender sender, String path) {
        sender.sendMessage(getFormattedValue(path));
    }

    default void sendMappedMessage(@NotNull OpCommandSender sender, String path, Tuple<String, String>... tuples) {
        sender.sendMessage(getFormattedValue(path, tuples));
    }

    default void sendMappedMessage(@NotNull Player sender, String path, Tuple<String, String>... tuples) {
        sender.sendMessage(getFormattedValue(path, tuples));
    }

    default void sendMappedMessage(@NotNull OpCommandSender sender, String path, @NotNull SimpleTranslation translation) {
        sender.sendMessage(getFormattedValue(path, translation.getStrings()));
    }

    default void sendMappedMessage(@NotNull Player sender, String path, String[][] strings) {
        sender.sendMessage(getFormattedValue(path, strings));
    }

    default void sendMappedMessage(@NotNull Player sender, String path) {
        sender.sendMessage(getFormattedValue(path));
    }

    default void sendMappedMessage(@NotNull Player sender, String path, @NotNull SimpleTranslation translation) {
        sender.sendMessage(getFormattedValue(path, translation.getStrings()));
    }

    default @NotNull Runnable getMappedMessage(OpCommandSender sender, String path, String[][] strings) {
        return () -> sender.sendMessage(getValue(path, strings));
    }

    default @NotNull Runnable getMappedMessage(OpCommandSender sender, String path, SimpleTranslation translation) {
        return () -> sender.sendMessage(getValue(path, translation));
    }

    default @NotNull Runnable getMappedMessage(OpCommandSender sender, String path) {
        return () -> sender.sendMessage(getValue(path));
    }
}
