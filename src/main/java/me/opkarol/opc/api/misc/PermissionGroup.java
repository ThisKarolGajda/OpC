package me.opkarol.opc.api.misc;

import me.opkarol.opc.api.utils.StringUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public record PermissionGroup<K>(String group, K object) implements Serializable {

    public static @NotNull PermissionGroup<?> get(@NotNull FileConfiguration config, String path) {
        String object = config.getString(path);
        double d = StringUtil.getDoubleFromString(object);
        if (d != -1) {
            return new PermissionGroup<>(path, d);
        }
        int i = StringUtil.getIntFromString(object);
        if (i != -1) {
            return new PermissionGroup<>(path, i);
        }
        if (StringUtil.isBoolean(object)) {
            return new PermissionGroup<>(path, StringUtil.getBooleanFromObject(object));
        }
        return new PermissionGroup<>(path, object);
    }
}
