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
import me.opkarol.opc.api.utils.VariableUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
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
        if (object == null || object.isNotValid()) {
            return this;
        }
        return setString(path, object.toString());
    }

    public CustomConfiguration setBoolean(String path, boolean object) {
        return set(path, object);
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

    public <M extends IEmptyConfiguration> CustomConfiguration setConfigurable(String path, M configurable) {
        if (configurable != null && !configurable.isEmpty()) {
            configurable.save(getPath(path));
        }
        return this;
    }

    public CustomConfiguration setStringList(String path, List<String> list) {
        return set(path, list);
    }

    public CustomConfiguration setEnum(String path, Enum<?> anEnum) {
        if (anEnum == null) {
            return this;
        }
        return setString(path, anEnum.name());
    }

    public CustomConfiguration setMaterial(String path, Material material) {
        if (material == null) {
            return this;
        }
        return setString(path, material.toString());
    }

    public Object get(String path) {
        return getConfig().get(getPath(path));
    }

    public Material getMaterial(String path) {
        return StringUtil.getMaterialFromString(getString(path));
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

    public boolean getBoolean(String path) {
        return getConfig().getBoolean(getPath(path));
    }

    public List<String> getStringList(String path) {
        return getConfig().getStringList(getPath(path));
    }

    @Deprecated
    public <M extends CustomConfigurable> M getConfigurable(String path, Class<M> clazz) {
        M main = ReflectionUtil.getInstance(clazz);
        main.get(getPath(path));
        return main;
    }

    public OpSerializableLocation getLocation(String path) {
        return new OpSerializableLocation(getString(path));
    }

    public <K extends Enum<K>> Optional<K> getEnum(String path, Class<K> clazz) {
        return StringUtil.getEnumValue(getString(path), clazz);
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
        configuration.useSectionKeys(VariableUtil.ifEndsWithRemove(defaultPath, "."), consumer);
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
        return VariableUtil.getOrDefault(defaultPath + path, path);
    }

    public CustomConfiguration setPath(String defaultPath) {
        this.defaultPath = VariableUtil.ifNotEndsWithAdd(defaultPath, ".");
        return this;
    }

    public CustomConfiguration addPath(String path) {
        return setPath(defaultPath + path);
    }

    public OpText getText(String path) {
        OpText obj = new OpText();
        obj.get(getPath(path));
        return obj;
    }

    public OpParticle getParticle(String path) {
        OpParticle obj = new OpParticle();
        obj.get(getPath(path));
        return obj;
    }

    public OpTitle getTitle(String path) {
        OpTitle obj = new OpTitle();
        obj.get(getPath(path));
        return obj;
    }

    public OpSound getSound(String path) {
        OpSound obj = new OpSound();
        obj.get(getPath(path));
        return obj;
    }

    public String getDefaultPath() {
        return defaultPath;
    }

    public String getDefaultPathWithRemovedDot() {
        return VariableUtil.ifEndsWithRemove(getDefaultPath(), ".");
    }

    @Deprecated
    public void setUnsafeObject(Object toSet) {
        getConfig().set(getDefaultPathWithRemovedDot(), toSet);
    }

    @Deprecated
    public Object getUnsafeObject() {
        return getConfig().get(getDefaultPathWithRemovedDot());
    }
}
