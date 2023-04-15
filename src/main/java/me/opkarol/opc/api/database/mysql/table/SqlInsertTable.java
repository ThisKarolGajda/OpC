package me.opkarol.opc.api.database.mysql.table;

import me.opkarol.opc.api.database.mysql.types.SqlAttribute;
import me.opkarol.opc.api.database.mysql.types.SqlVariable;
import me.opkarol.opc.api.map.OpMap;
import org.jetbrains.annotations.NotNull;

public class SqlInsertTable {
    private final SqlTable table;
    private final OpMap<String, Object> valueMap = new OpMap<>();

    public SqlInsertTable(SqlTable table) {
        this.table = table;
    }

    public SqlInsertTable addValue(@NotNull SqlVariable variable, Object value) {
        return addValue(variable.name(), value);
    }

    public SqlInsertTable addValue(String variable, Object value) {
        valueMap.set(variable, value);
        return this;
    }

    public String getShortenValues() {
        StringBuilder builder = new StringBuilder();
        if (table.getMap().keySet().size() == 0) {
            return "";
        }

        for (SqlVariable variable : table.getMap().keySet()) {
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

        for (SqlVariable variable : table.getMap().keySet()) {
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

        for (SqlVariable variable : table.getMap().keySet()) {
            Object value = valueMap.getOrDefault(variable.name(), null);
            if (value == null || table.getAttributes(variable).contains(SqlAttribute.PRIMARY) || table.getAttributes(variable).contains(SqlAttribute.IGNORE_IN_ALL_SEARCH)) {
                continue;
            }

            builder.append("`").append(variable.name()).append("` = '").append(value).append("', ");
        }

        return builder.substring(0, builder.length() - 2);
    }
}
