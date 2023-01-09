package me.opkarol.opc.api.serialization;

import me.opkarol.opc.api.file.Configuration;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.map.OpMapBuilder;
import me.opkarol.opc.api.utils.VariableUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public abstract class Serialize implements Serializable {

    public Serialize(OpMapBuilder<String, Object> objects) {
        Serialization.registerClass(getClass());
    }

    public synchronized void save(@NotNull OpMap<String, Object> objects, @NotNull Configuration configuration, String path) {
        FileConfiguration config = configuration.getConfig();
        path = VariableUtil.ifNotEndsWithAdd(path, ".");

        for (String string : objects.keySet()) {
            Object object = objects.unsafeGet(string);
            if (object instanceof String stringObject && string.equals(Serialization.SERIALIZED_TYPE_KEY)) {
                config.set(path + string, Serialization.getAlias(stringObject));
                continue;
            }
            if (object instanceof Serialize serialize) {
                serialize.save(serialize.serialize(), configuration, path);
                continue;
            }
            config.set(path + string, object);
        }
        configuration.save();
    }

    public synchronized static Serializable get(@NotNull Configuration configuration, String path) {
        FileConfiguration config = configuration.getConfig();
        String finalPath = VariableUtil.ifNotEndsWithAdd(path, ".");
        OpMapBuilder<String, Object> tempMap = new OpMapBuilder<>();

        configuration.useSectionKeys(path, s -> tempMap.set(s, config.get(finalPath + s)));

        return Serialization.deserializeObject(tempMap);
    }
}
