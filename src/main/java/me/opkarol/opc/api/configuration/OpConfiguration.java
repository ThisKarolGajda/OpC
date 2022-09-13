package me.opkarol.opc.api.configuration;

import me.opkarol.opc.api.configuration.object.OpConfigurationObject;
import me.opkarol.opc.api.configuration.object.OpConfigurationSection;
import me.opkarol.opc.api.files.Configuration;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.utils.VariableUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static me.opkarol.opc.api.utils.VariableUtil.ifNotEndsWithAdd;

public class OpConfiguration <K> {
    private Configuration configuration;
    private FileConfiguration config;
    private String path;
    private final OpMap<String, OpConfigurationObject<K>> map = new OpMap<>();

    public OpConfiguration(Configuration configuration, String path) {
        this.configuration = configuration;
        if (configuration != null) {
            this.config = configuration.getConfig();
        }
        this.path = path;
    }

    public OpConfiguration<K> addObject(@NotNull OpConfigurationObject<?> object) {
        return addObject(object.getPath(), object.getObject());
    }

    public OpConfiguration<K> addObject(String path) {
        return addObject(path, null);
    }

    public OpConfiguration<K> addObjects(String path, String... objects) {
        if (objects != null && objects.length > 0) {
            for (String s : objects) {
                addObject(getPath(ifNotEndsWithAdd(path, ".") + s));
            }
        }
        return this;
    }

    public OpConfiguration<K> addObject(String path, Object object) {
        String p = getPath(path);
        map.set(p, new OpConfigurationObject<>(p, object));
        return this;
    }

    public OpConfiguration<K> addConfigurationSection(String path, @NotNull OpConfigurationSection<K> object) {
        String p = getPath(path);
        OpConfigurationObject<K> section = (OpConfigurationObject<K>) object.setPath(p);
        map.set(p, section);
        return this;
    }

    public OpConfiguration<K> addConfigurationSection(String path) {
        return addConfigurationSection(path, new OpConfigurationSection<>(this, path));
    }

    private @NotNull String getPath(@NotNull String path) {
        if (path.startsWith(".")) {
            return VariableUtil.ifEndsWithRemove(this.path, ".") + path;
        }
        return ifNotEndsWithAdd(this.path, ".") + path;
    }

    public OpConfiguration<K> save() {
        for (String path : map.keySet()) {
            CustomConfigurable object = map.getOrDefault(path, null);
            if (object == null) {
                continue;
            }

            object.save(path);
        }
        configuration.save();
        return this;
    }

    public OpConfiguration<K> get() {
        for (String path : map.keySet()) {
            OpConfigurationObject<K> object = map.getOrDefault(path, null);
            if (object == null) {
                map.set(path, new OpConfigurationObject<>(path, config.get(path)));
            } else {
                if (object.isConfigurationSection() && object.containsRealObject()) {
                    OpConfigurationSection<K> section = (OpConfigurationSection<K>) object;
                    for (String key : section.getMap().keySet()) {
                        OpConfigurationObject<K> object1 = section.getMap().getMap().get(key);
                        map.set(getPath(path) + key, object1);
                    }
                }
            }
        }
        return this;
    }

    public OpConfiguration<K> getAll() {
        return getAll(path);
    }

    private OpConfiguration<K> getAll(String path) {
        path = VariableUtil.ifEndsWithRemove(path, ".");
        if (!isConfigurationSection(path)) {
            addObject(new OpConfigurationObject<K>(path, config.get(path)));
            return this;
        }

        String finalPath = path;
        configuration.useSectionKeys(path, key -> {
            String iPath = finalPath + "." + key;
            if (isConfigurationSection(iPath)) {
                getAll(iPath);
            } else {
                addObject(new OpConfigurationObject<K>(iPath, config.get(iPath)));
            }
        });
        return this;
    }

    public boolean isConfigurationSection(String path) {
        return config.isConfigurationSection(path);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        if (configuration != null) {
            this.config = configuration.getConfig();
        }
    }

    public Optional<OpConfigurationObject<K>> getObject(String path) {
        return map.getByKey(path);
    }

    public OpMap<String, OpConfigurationObject<K>> getMap() {
        return map;
    }
}
