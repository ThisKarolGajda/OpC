package me.opkarol.opc.api.plugins;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.file.Configuration;
import me.opkarol.opc.api.serialization.Serialization;
import me.opkarol.opc.api.tools.autostart.OpAutoDisable;
import me.opkarol.opc.api.tools.location.OpSerializableLocation;
import me.opkarol.opc.api.wrappers.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.autocomplete.SuggestionProviderFactory;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class OpPlugin extends JavaPlugin {
    private static OpPlugin plugin;
    private BukkitCommandHandler commandHandler;

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
        BukkitCommandHandler.create(this);
        commandHandler.getAutoCompleter().registerSuggestionFactory(0, SuggestionProviderFactory.forType(Player.class, SuggestionProvider.map(Bukkit::getOnlinePlayers, Player::getName)));
        enable();
        registerEvents();
        if (registerCommandsWithBrigadier()) {
            commandHandler.registerBrigadier();
        }

    }

    public abstract void enable();

    public abstract void disable();

    @Override
    public void onDisable() {
        OpAutoDisable.registerDisable();
        OpAPI.unregisterCommands();
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

    public boolean registerCommandsWithBrigadier() {
        return false;
    }

    protected void disablePlugin(String message) {
        getLog().log(Level.SEVERE, message);
        getServer().getPluginManager().disablePlugin(this);
    }

    public BukkitCommandHandler getCommandHandler() {
        return commandHandler;
    }
}
