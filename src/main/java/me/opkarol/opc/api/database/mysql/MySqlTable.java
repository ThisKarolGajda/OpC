package me.opkarol.opc.api.database.mysql;

import me.opkarol.opc.api.map.OpLinkedMap;

public class MySqlTable {
    private final OpLinkedMap<MySqlVariable, MySqlAttribute[]> map = new OpLinkedMap<>();
    private String tableName;

    private String finalString;

    public MySqlTable(String tableName) {
        this.tableName = tableName;
    }

    public MySqlTable fromString(String s) {
        this.finalString = s;
        return this;
    }

    public MySqlTable addMySqlVariable(MySqlVariable variableName, MySqlAttribute... attributes) {
        map.set(variableName, attributes);
        return this;
    }

    public MySqlTable addMySqlVariable(String name, MySqlVariableType type, MySqlAttribute... attributes) {
        return addMySqlVariable(new MySqlVariable(name, type), attributes);
    }

    public String toCreateTableString() {
        return finalString == null ? String.format("CREATE TABLE IF NOT EXISTS %s (%s);", getTableName(), getTableValues()) : finalString;
    }

    public String getTableValues() {
        if (map.keySet().size() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        MySqlVariable primary = null;
        for (MySqlVariable variable : map.keySet()) {
            builder.append(variable.name()).append(" ").append(variable.variableName());
            MySqlAttribute[] attributes = map.getOrDefault(variable, null);
            if (attributes != null && attributes.length != 0) {
                for (MySqlAttribute attribute : attributes) {
                    if (attribute.equals(MySqlAttribute.PRIMARY)) {
                        primary = variable;
                    } else {
                        builder.append(attribute.getText());

                    }
                }
            }
            builder.append(", ");
        }
        if (primary != null) {
            builder.append(String.format(MySqlAttribute.PRIMARY.getText(), primary.name())).append(", ");
        }

        return builder.substring(0, builder.length() - 2);
    }

    public String getTableName() {
        return tableName == null ? "DEFAULT_NULL_TABLE" : String.format("`%s`", tableName);
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public OpLinkedMap<MySqlVariable, MySqlAttribute[]> getMap() {
        return map;
    }
}
