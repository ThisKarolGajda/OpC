package me.opkarol.opc.api.database.mysql.table;

import me.opkarol.opc.api.database.mysql.types.MySqlAttribute;
import me.opkarol.opc.api.database.mysql.types.MySqlVariable;
import me.opkarol.opc.api.database.mysql.types.MySqlVariableType;
import me.opkarol.opc.api.map.OpLinkedMap;

import java.util.Arrays;
import java.util.List;

import static me.opkarol.opc.api.utils.VariableUtil.getOrDefault;

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
        return getOrDefault(finalString, String.format("CREATE TABLE IF NOT EXISTS %s (%s);", getTableName(), getTableValues()));
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
                boolean alreadySet = false;
                for (MySqlAttribute attribute : attributes) {
                    if (attribute.equals(MySqlAttribute.PRIMARY)) {
                        primary = variable;
                    } else {
                        if (!alreadySet && variable.variableName().equals(MySqlVariableType.TEXT)) {
                            builder.append(MySqlAttribute.CHARACTER_SET.getText());
                            alreadySet = true;
                        }
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
        return getOrDefault(String.format("`%s`", tableName), "DEFAULT_NULL_TABLE");
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public OpLinkedMap<MySqlVariable, MySqlAttribute[]> getMap() {
        return map;
    }

    public List<MySqlAttribute> getAttributes(MySqlVariable variable) {
        return Arrays.asList(getMap().getMap().get(variable));
    }
}
