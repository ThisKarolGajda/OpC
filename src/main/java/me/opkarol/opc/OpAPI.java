package me.opkarol.opc;

import me.opkarol.opc.api.command.OpCommand;
import me.opkarol.opc.api.file.Configuration;
import me.opkarol.opc.api.gui.InventoryCache;
import me.opkarol.opc.api.list.OpList;
import me.opkarol.opc.api.plugins.OpPlugin;
import org.jetbrains.annotations.NotNull;

public class OpAPI {
    private static OpPlugin plugin;
    private static final OpList<OpCommand> commands = new OpList<>();
    private final InventoryCache inventoryCache;
    private static OpAPI INSTANCE;

    public static OpPlugin getPlugin() {
        return plugin;
    }

    public OpAPI(OpPlugin opPlugin) {
        plugin = opPlugin;
        INSTANCE = this;
        inventoryCache = new InventoryCache();
    }

    public static void init(@NotNull OpPlugin plugin) {
        new OpAPI(plugin);
    }

    public static void addCommand(OpCommand command) {
        commands.add(command);
    }

    public static void unregisterCommands() {
        commands.forEach(OpCommand::unregister);
    }

    public static Configuration getConfig() {
        return plugin.getConfiguration();
    }

    public static void logInfo(Object message) {
        getPlugin().getLogger().info(message == null ? "" : message.toString());
    }

    public InventoryCache getInventoryCache() {
        return inventoryCache;
    }

    public static OpAPI getInstance() {
        return INSTANCE;
    }
}