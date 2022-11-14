package me.opkarol.opc.api.files;

import com.tchristofferson.configupdater.ConfigUpdater;
import me.opkarol.opc.api.location.OpSerializableLocation;
import me.opkarol.opc.api.utils.StringUtil;
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
    private final File pluginDataFolder;
    private final File configuration;
    private FileConfiguration config;
    private String fileName;

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
                this.pluginDataFolder.mkdirs();
            }
            try {
                plugin.saveResource(fileName, false);
                updateConfig();
            } catch (IllegalArgumentException ignore) {
                createNewFile();
            }
        }
    }

    public void createNewFile() {
        try {
            File file = getFile();
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
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

        if (name.contains(".")) {
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

    public void useSectionKey(String path, Consumer<Set<String>> consumer) {
        getSectionKeys(path).ifPresent(consumer);
    }

    public void useSectionKeys(String path, Consumer<String> consumer) {
        getSectionKeys(path).ifPresent(strings -> strings.forEach(consumer));
    }

    public Configuration set(String path, Object object) {
        if (getConfig() != null) {
            config.set(path, object);
        }
        return this;
    }

    public String get(String path) {
        if (getConfig() != null) {
            return config.getString(path);
        }
        return null;
    }

    public Object getObject(String path) {
        if (getConfig() != null) {
            return config.get(path);
        }
        try {
            YamlConfiguration.loadConfiguration(configuration);
        } catch (Exception ignore) {
            return null;
        }
        return getObject(path);
    }

    public int getInt(String path) {
        return StringUtil.getInt(get(path));
    }

    public double getDouble(String path) {
        return StringUtil.getDouble(get(path));
    }

    public float getFloat(String path) {
        return StringUtil.getFloat(get(path));
    }

    public OpSerializableLocation getLocation(String path) {
        return new OpSerializableLocation(get(path));
    }

    public <K extends Enum<K>> Optional<K> getEnum(String path, Class<K> enumType) {
        return StringUtil.getEnumValue(get(path), enumType);
    }

    public <K extends Enum<K>> K getUnsafeEnum(String path, Class<K> enumType) {
        return getEnum(path, enumType).orElse(null);
    }

    public <K extends Enum<K>> void useEnumValue(String path, Class<K> clazz, Consumer<K> consumer) {
        StringUtil.getEnumValue(get(path), clazz).ifPresent(consumer);
    }

    public boolean getBoolean(String path) {
        if (getConfig() != null) {
            return getConfig().getBoolean(path);
        }
        return false;
    }

    public String getString(String path) {
        if (getConfig() != null) {
            return getConfig().getString(path);
        }
        return null;
    }
}