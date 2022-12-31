package me.opkarol.opc.api.plugin;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.autostart.OpAutoDisable;
import me.opkarol.opc.api.autostart.OpAutoStart;
import me.opkarol.opc.api.files.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class OpPlugin extends JavaPlugin {
    private Configuration configuration;
    private static OpPlugin plugin;

    @Override
    public void onLoad() {
        plugin = this;
        configuration = new Configuration(plugin, "config.yml");
        configuration.createConfig();
        OpAPI.init(this);
    }

    @Override
    public void onEnable() {
        OpAutoStart.activate(plugin);
        enable();
        registerEvents();
        registerCommands();
    }

    public abstract void enable();

    public abstract void disable();

    @Override
    public void onDisable() {
        OpAPI.unregisterCommands();
        OpAutoDisable.activate(plugin);
        plugin = null;
    }

    @NotNull
    @Override
    public FileConfiguration getConfig() {
        return configuration.getConfig();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    @NotNull
    @Override
    protected File getFile() {
        return super.getFile();
    }

    public void registerEvents() {

    }

    public void registerCommands() {

    }

    public static @NotNull Logger getLog() {
        return plugin.getLogger();
    }

    public static OpPlugin getInstance() {
        return plugin;
    }

    protected void disablePlugin(String message) {
        getLog().log(Level.SEVERE, message);
        getServer().getPluginManager().disablePlugin(this);
    }
}
