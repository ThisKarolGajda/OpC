package me.opkarol.opc.api.plugin;

import me.opkarol.opc.api.files.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Logger;

public abstract class OpPlugin extends JavaPlugin {
    private Configuration configuration;
    private static OpPlugin plugin;

    @Override
    public void onLoad() {
        plugin = this;
        configuration = new Configuration(this, "config.yml");
    }

    @Override
    public void onEnable() {
        enable();
        registerEvents();
        registerCommands();
    }

    public void enable() {

    }

    public void disable() {

    }

    @Override
    public void onDisable() {
        disable();
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

    @Deprecated
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
}
