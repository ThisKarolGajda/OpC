package me.opkarol.opc.api.database.mysql.table;

import me.opkarol.opc.api.database.mysql.types.MySqlVariable;
import me.opkarol.opc.api.map.OpMap;
import org.jetbrains.annotations.NotNull;

public class MySqlDeleteTable {
    private final MySqlTable table;
    private final OpMap<String, Object> valueMap = new OpMap<>();

    public MySqlDeleteTable(MySqlTable table) {
        this.table = table;
    }

    public MySqlDeleteTable addDeletion(String variable, Object object) {
        valueMap.set(variable, object);
        return this;
    }

    public MySqlDeleteTable addDeletion(@NotNull MySqlVariable variable, Object object) {
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
