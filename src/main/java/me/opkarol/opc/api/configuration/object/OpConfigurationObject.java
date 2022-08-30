package me.opkarol.opc.api.configuration.object;

import me.opkarol.opc.api.configuration.CustomConfigurable;
import me.opkarol.opc.api.configuration.CustomConfiguration;

import java.util.function.Consumer;

public class OpConfigurationObject<T> implements CustomConfigurable {
    private final String path;
    private Object object;
    private boolean isConfigurationSection;

    public OpConfigurationObject(String path, Object object) {
        this.path = path;
        this.object = object;
    }

    public OpConfigurationObject(String path, OpConfigurationSection<T> object) {
        this.path = path;
        this.object = object;
        this.isConfigurationSection = true;
    }

    public Object getObject() {
        return object;
    }

    public String getPath() {
        return path;
    }

    public boolean isConfigurationSection() {
        return isConfigurationSection;
    }

    public boolean containsRealObject() {
        return object != null;
    }

    @Override
    public Consumer<CustomConfiguration> get() {
        return c -> object = c.get(path);
    }

    @Override
    public Consumer<CustomConfiguration> save() {
        return c -> c.set(path, object);
    }
}
