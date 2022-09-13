package me.opkarol.opc.api.configuration.object;

import me.opkarol.opc.api.configuration.OpConfiguration;
import me.opkarol.opc.api.files.Configuration;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.utils.VariableUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class OpConfigurationSection <K> extends OpConfigurationObject<OpConfigurationSection<K>> {
    private final OpMap<String, OpConfigurationObject<K>> map = new OpMap<>();
    private final OpConfiguration<K> configuration;
    private String path;

    public OpConfigurationSection(String path, OpConfiguration<K> configuration) {
        super(path, configuration);
        this.configuration = configuration;
        this.path = path;
    }

    public OpConfigurationSection(OpConfiguration<K> config, String path) {
        super(path, new OpConfigurationSection<>(path, config));
        this.configuration = config;
        this.path = path;
    }

    public OpConfigurationSection<K> getFromConfiguration() {
        return this.get(configuration.getConfiguration(), path);
    }

    public OpConfigurationSection<K> save(@NotNull Configuration configuration, String path) throws NullPointerException {
        map.keySet().forEach(s -> configuration.set(VariableUtil.ifNotEndsWithAdd(path, ".") + s, map.getOrDefault(s, null)));
        return null;
    }

    public OpConfigurationSection<K> get(@NotNull Configuration configuration, String path) {
        OpConfiguration<K> config = new OpConfiguration<K>(configuration, path).getAll();
        for (String s : config.getMap().keySet()) {
            Optional<OpConfigurationObject<K>> optional = config.getObject(s);
            if (optional.isEmpty()) {
                continue;
            }

            OpConfigurationObject<K> object = optional.get();
            map.set(object.getPath(), object);
        }
        return null;
    }

    public OpConfiguration<K> getConfiguration() {
        return configuration;
    }

    public OpConfigurationSection<K> setPath(String path) {
        this.path = path;
        return this;
    }

    public String getPath() {
        return path;
    }

    public OpMap<String, OpConfigurationObject<K>> getMap() {
        return map;
    }
}
