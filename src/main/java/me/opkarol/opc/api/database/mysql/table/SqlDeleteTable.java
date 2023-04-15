package me.opkarol.opc.api.database.mysql.table;

import me.opkarol.opc.api.database.mysql.types.SqlVariable;
import me.opkarol.opc.api.map.OpMap;
import org.jetbrains.annotations.NotNull;

public class SqlDeleteTable {
    private final SqlTable table;
    private final OpMap<String, Object> valueMap = new OpMap<>();

    public SqlDeleteTable(SqlTable table) {
        this.table = table;
    }

    public SqlDeleteTable addDeletion(String variable, Object object) {
        valueMap.set(variable, object);
        return this;
    }

    public SqlDeleteTable addDeletion(@NotNull SqlVariable variable, Object object) {
        return addDeletion(variable.toString(), object);
    }

    public String toDeleteString() {
        return String.format("DELETE FROM %s WHERE %s;", table.getTableName(), getValues());
    }

    public String getValues() {
        StringBuilder builder = new StringBuilder();
        if (table.getMap().keySet().size() == 0 || valueMap.getMap().keySet().size() == 0) {
            throw new IllegalArgumentException("Too little arguments provided!");
        }

        for (String s : valueMap.keySet()) {
            Object value = valueMap.getOrDefault(s, null);
            if (value != null) {
                builder.append("`").append(s).append("` = '").append(value).append("'AND ");
            }
        }

        if (builder.length() < 4) {
            return "";
        }

        return builder.substring(0, builder.length() - 4);
    }
}
