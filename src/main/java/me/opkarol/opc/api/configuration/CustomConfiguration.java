package me.opkarol.opc.api.configuration;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.files.Configuration;
import me.opkarol.opc.api.location.OpSerializableLocation;
import me.opkarol.opc.api.misc.OpParticle;
import me.opkarol.opc.api.misc.OpSound;
import me.opkarol.opc.api.misc.OpText;
import me.opkarol.opc.api.misc.OpTitle;
import me.opkarol.opc.api.utils.ReflectionUtil;
import me.opkarol.opc.api.utils.StringUtil;
import me.opkarol.opc.api.utils.Util;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Optional;
import java.util.function.Consumer;

public class CustomConfiguration {
    private final Configuration configuration = OpAPI.getConfig();
    private String defaultPath;

    public Configuration getConfiguration() {
        return configuration;
    }

    public FileConfiguration getConfig() {
        return configuration.getConfig();
    }

    public CustomConfiguration setString(String path, String object) {
        if (object == null) {
            return this;
        }
        getConfig().set(getPath(path), object);
        return this;
    }

    public CustomConfiguration setInt(String path, int object) {
        getConfig().set(getPath(path), object);
        return this;
    }

    public CustomConfiguration setFloat(String path, float object) {
        getConfig().set(getPath(path), object);
        return this;
    }

    public CustomConfiguration setDouble(String path, double object) {
        getConfig().set(getPath(path), object);
        return this;
    }

    public CustomConfiguration setLocation(String path, OpSerializableLocation object) {
        if (object == null) {
            return this;
        }
        return setString(path, object.toString());
    }

    public CustomConfiguration set(String path, Object object) {
        getConfig().set(getPath(path), object);
        return this;
    }

    public CustomConfiguration setConfigurable(String path, CustomConfigurable configurable) {
        if (configurable != null) {
            configurable.save(getPath(path));
        }
        return this;
    }

    public CustomConfiguration setEnum(String path, Enum<?> anEnum) {
        if (anEnum == null) {
            return this;
        }
        return setString(path, anEnum.name());
    }

    public Object get(String path) {
        return getConfig().get(getPath(path));
    }

    public String getString(String path) {
        return getConfig().getString(getPath(path));
    }

    public int getInt(String path) {
        return StringUtil.getIntFromString(getString(path));
    }

    public float getFloat(String path) {
        return StringUtil.getFloatFromString(getString(path));
    }

    public double getDouble(String path) {
        return StringUtil.getDoubleFromString(getString(path));
    }

    @Deprecated
    public <M extends CustomConfigurable> M getConfigurable(String path, Class<M> clazz) {
        M main = ReflectionUtil.getInstance(clazz);
        main.get(path);
        return main;
    }

    public OpSerializableLocation getLocation(String path) {
        return new OpSerializableLocation(getString(path));
    }

    public <K extends Enum<K>> Optional<K> getEnum(String path, Class<K> clazz) {
        return StringUtil.getEnumValue(getPath(path), clazz);
    }

    public <K extends Enum<K>> K getUnsafeEnum(String path, Class<K> clazz) {
        Optional<K> optional = getEnum(path, clazz);
        if (optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }

    public CustomConfiguration save() {
        configuration.save();
        return this;
    }

    public CustomConfiguration useSectionKeys(Consumer<String> consumer) {
        configuration.useSectionKeys(defaultPath, consumer);
        return this;
    }

    public CustomConfiguration useSectionKeys(String path, Consumer<String> consumer) {
        configuration.useSectionKeys(getPath(path), consumer);
        return this;
    }

    public <T> CustomConfiguration forEachIterable(Iterable<T> iterable, Consumer<T> consumer) {
        if (iterable != null && consumer != null) {
            iterable.forEach(consumer);
        }
        return this;
    }

    public String getPath(String path) {
        return Util.getOrDefault(defaultPath + path, path);
    }

    public CustomConfiguration setPath(String defaultPath) {
        this.defaultPath = Util.ifNotEndsWithAdd(defaultPath, ".");
        return this;
    }

    public OpText getText(String path) {
        OpText obj = new OpText();
        obj.get(path);
        return obj;
    }

    public OpParticle getParticle(String path) {
        OpParticle obj = new OpParticle();
        obj.get(path);
        return obj;
    }

    public OpTitle getTitle(String path) {
        OpTitle obj = new OpTitle();
        obj.get(path);
        return obj;
    }

    public OpSound getSound(String path) {
        OpSound obj = new OpSound();
        obj.get(path);
        return obj;
    }
}
