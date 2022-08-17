package me.opkarol.opc.api.files;

import com.tchristofferson.configupdater.ConfigUpdater;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class Configuration {
    private final Plugin plugin;
    private final String pluginName;
    private String fileName;
    private final File pluginDataFolder;

    private final File configuration;
    private FileConfiguration config;

    public Configuration(@NotNull Plugin plugin, String fileName) {
        setName(fileName);
        this.plugin = plugin;
        this.pluginName = plugin.getName();
        this.pluginDataFolder = plugin.getDataFolder();
        configuration = getFile();
        config = YamlConfiguration.loadConfiguration(configuration);
        createConfig();
    }

    public Configuration(@NotNull Plugin plugin, String fileName, boolean readOnly) {
        setName(fileName);
        this.plugin = plugin;
        this.pluginName = plugin.getName();
        this.pluginDataFolder = plugin.getDataFolder();
        configuration = getFile();
        if (!readOnly) {
            config = YamlConfiguration.loadConfiguration(configuration);
        }
        createConfig();
    }

    public void createConfig() {
        if (!configuration.exists()) {
            if (!pluginDataFolder.exists()) {
                this.pluginDataFolder.mkdir();
            }
            plugin.saveResource(fileName, false);
        }
    }

    public void updateConfig() {
        try {
            ConfigUpdater.update(plugin, fileName, configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setName(String name) {
        if (name == null) {
            return;
        }

        if (name.endsWith(".yml")) {
            fileName = name;
        } else {
            fileName = name + ".yml";
        }
    }

    @Contract(" -> new")
    private @NotNull File getFile() {
        return new File(pluginDataFolder, fileName);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public File getConfiguration() {
        return configuration;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void save() {
        try {
            config.save(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(configuration);
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Object getObject(String path) {
        return getConfig().get(path);
    }

    public Optional<ConfigurationSection> getSection(String path) {
        if (config == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(config.getConfigurationSection(path.endsWith(".") ? path.substring(0, path.length() - 1) : path));
    }

    public Optional<Set<String>> getSectionKeys(String path) {
        Optional<ConfigurationSection> optional = getSection(path);
        return optional.map(section -> section.getKeys(false));
    }

    public void useSectionKeys(String path, Consumer<Set<String>> consumer) {
        getSectionKeys(path).ifPresent(consumer);
    }
}