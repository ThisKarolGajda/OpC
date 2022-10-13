package me.opkarol.opc.api.database.mysql.table;

import me.opkarol.opc.api.database.mysql.types.MySqlAttribute;
import me.opkarol.opc.api.database.mysql.types.MySqlVariable;
import me.opkarol.opc.api.map.OpMap;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MySqlInsertTable {
    private final MySqlTable table;
    private final OpMap<String, Object> valueMap = new OpMap<>();

    public MySqlInsertTable(MySqlTable table) {
        this.table = table;
    }

    public MySqlInsertTable addValue(@NotNull MySqlVariable variable, Object value) {
        return addValue(variable.name(), value);
    }

    public MySqlInsertTable addValue(String variable, Object value) {
        valueMap.set(variable, value);
        return this;
    }
    public String getShortenValues() {
        StringBuilder builder = new StringBuilder();
        if (table.getMap().keySet().size() == 0) {
            return "";
        }

        for (MySqlVariable variable : table.getMap().keySet()) {
            builder.append("`").append(variable.name()).append("`, ");
        }

        return builder.substring(0, builder.length() - 2);
    }

    public String toInsertIntoString() {
        return String.format("INSERT INTO %s (%s) VALUES (%s) ON DUPLICATE KEY UPDATE %s;", table.getTableName(), getShortenValues(), getValues(), getDuplicateKeys());
    }

    public String getValues() {
        StringBuilder builder = new StringBuilder();
        if (table.getMap().keySet().size() == 0 || valueMap.getMap().keySet().size() == 0) {
            return "";
        }

        for (MySqlVariable variable : table.getMap().keySet()) {
            Object value = valueMap.getOrDefault(variable.name(), null);
            if (value == null) {
                continue;
            }

            builder.append("'").append(value).append("', ");
        }

        if (builder.length() < 2) {
            return "";
        }

        return builder.substring(0, builder.length() - 2);
    }

    public String getDuplicateKeys() {
        StringBuilder builder = new StringBuilder();
        if (table.getMap().keySet().size() == 0 || valueMap.getMap().keySet().size() == 0) {
            return "";
        }

        for (MySqlVariable variable : table.getMap().keySet()) {
            Object value = valueMap.getOrDefault(variable.name(), null);
            if (value == null || Arrays.asList(table.getMap().getMap().get(variable)).contains(MySqlAttribute.PRIMARY)) {
                continue;
            }

            builder.append("`").append(variable.name()).append("` = '").append(value).append("', ");
        }

        return builder.substring(0, builder.length() - 2);
    }
}
