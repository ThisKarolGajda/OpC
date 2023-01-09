package me.opkarol.opc.api.file;

import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.utils.VariableUtil;
import org.bukkit.plugin.Plugin;

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
     * @param path the path from which value will be taken
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

    public String getFormattedValue(String path) {
        return formatMessage(getValue(path));
    }
}
