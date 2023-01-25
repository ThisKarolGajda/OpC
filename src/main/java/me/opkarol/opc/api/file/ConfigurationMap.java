package me.opkarol.opc.api.file;

import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.misc.Tuple;
import me.opkarol.opc.api.utils.VariableUtil;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

import static me.opkarol.opc.api.utils.FormatUtils.formatMessage;

public class ConfigurationMap {
    private final Configuration configuration;
    private final OpMap<String, String> map;

    public ConfigurationMap(Plugin plugin, String file) {
        map = new OpMap<>();
        configuration = new Configuration(plugin, file);
    }

    public ConfigurationMap(Plugin plugin, String file, boolean readOnly) {
        map = new OpMap<>();
        configuration = new Configuration(plugin, file, readOnly);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public OpMap<String, String> getMap() {
        return map;
    }

    public String getValue(String path) {
        if (map.containsKey(path)) {
            return map.getMap().get(path);
        }
        Object object = getConfiguration().getObject(path);
        if (object == null) {
            getConfiguration().reload();
            return VariableUtil.getOrDefault(map.getMap().get(path), path);
        }

        if (object instanceof String str) {
            map.set(path, str);
            return str;
        }
        return object.toString();
    }

    public String getValue(String path, TranslationObject... objects) {
        String string = getValue(path);
        if (objects != null) {
            for (TranslationObject object1 : objects) {
                string = object1.replace(string);
            }
        }
        return string;
    }

    /**
     * Parameter objects in each of their table should have 2 objects.
     *
     * @param path    the path from which value will be taken
     * @param objects table of strings, first parameter in each table is replacement and second is replacement
     * @return configured value
     */
    public String getValue(String path, String[][] objects) {
        String string = getValue(path);
        if (objects != null) {
            for (String[] str : objects) {
                if (str.length == 2) {
                    string = string.replace(str[0], str[1]);
                }
            }
        }
        return string;
    }

    public String getValue(String path, OpMap<String, String> objects) {
        String string = getValue(path);
        if (objects != null) {
            for (String key : objects.keySet()) {
                Optional<String> optional = objects.getByKey(key);
                if (optional.isEmpty()) {
                    continue;
                }
                String value = optional.get();
                string = string.replace(key, value);
            }
        }
        return string;
    }

    @SafeVarargs
    public final String getValue(String path, Tuple<String, String>... tuples) {
        return getValue(path, VariableUtil.getMapFromTuples(tuples));
    }

    public String getFormattedValue(String path) {
        return formatMessage(getValue(path));
    }

    public String getFormattedValue(String path, String[][] objects) {
        return formatMessage(getValue(path, objects));
    }

    @SafeVarargs
    public final String getFormattedValue(String path, Tuple<String, String>... tuples) {
        return formatMessage(getValue(path, tuples));
    }
}
