package me.opkarol.opc.api.plugins;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.file.Configuration;
import me.opkarol.opc.api.serialization.Serialization;
import me.opkarol.opc.api.tools.autostart.OpAutoDisable;
import me.opkarol.opc.api.tools.location.OpSerializableLocation;
import me.opkarol.opc.api.wrappers.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpPlugin extends JavaPlugin {
    private static OpPlugin plugin;

    static {
        Serialization.registerClass(OpObjectSerialized.class);
        Serialization.registerClass(OpParticle.class);
        Serialization.registerClass(OpSound.class);
        Serialization.registerClass(OpText.class);
        Serialization.registerClass(OpTitle.class);
        Serialization.registerClass(OpSerializableLocation.class);
    }

    private Configuration configuration;

    public static @NotNull Logger getLog() {
        return plugin.getLogger();
    }

    public static OpPlugin getInstance() {
        return plugin;
    }

    @Override
    public void onLoad() {
        plugin = this;
        configuration = new Configuration(plugin, "config.yml");
        configuration.createConfig();
        OpAPI.init(this);
    }

    @Override
    public void onEnable() {
        enable();
        registerEvents();
    }

    public void enable() {

    }

    public void disable() {

    }

    @Override
    public void onDisable() {
        OpAutoDisable.registerDisable();
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

    protected void disablePlugin(String message) {
        getLog().log(Level.SEVERE, message);
        getServer().getPluginManager().disablePlugin(this);
    }
}
