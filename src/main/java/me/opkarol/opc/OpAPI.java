package me.opkarol.opc;

import me.opkarol.opc.api.file.Configuration;
import me.opkarol.opc.api.plugins.OpPlugin;
import org.jetbrains.annotations.NotNull;

public class OpAPI {
    private static OpPlugin plugin;
    private static OpAPI INSTANCE;

    public static OpPlugin getPlugin() {
        return plugin;
    }

    public OpAPI(OpPlugin opPlugin) {
        plugin = opPlugin;
        INSTANCE = this;
    }

    public static void init(@NotNull OpPlugin plugin) {
        new OpAPI(plugin);
    }

    public static Configuration getConfig() {
        return plugin.getConfiguration();
    }

    public static void logInfo(Object message) {
        getPlugin().getLogger().info(message == null ? "" : message.toString());
    }

    public static OpAPI getInstance() {
        return INSTANCE;
    }
}