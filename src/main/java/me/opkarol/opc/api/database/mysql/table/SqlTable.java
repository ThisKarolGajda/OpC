package me.opkarol.opc.api.database.mysql.table;

import me.opkarol.opc.api.database.mysql.types.SqlAttribute;
import me.opkarol.opc.api.database.mysql.types.SqlVariable;
import me.opkarol.opc.api.database.mysql.types.SqlVariableType;
import me.opkarol.opc.api.map.OpLinkedMap;

import java.util.Arrays;
import java.util.List;

import static me.opkarol.opc.api.utils.VariableUtil.getOrDefault;

public class SqlTable {
    private final OpLinkedMap<SqlVariable, SqlAttribute[]> map = new OpLinkedMap<>();
    private String tableName;

    private String finalString;

    public SqlTable(String tableName) {
        this.tableName = tableName;
    }

    public SqlTable fromString(String s) {
        this.finalString = s;
        return this;
    }

    public SqlTable addMySqlVariable(SqlVariable variableName, SqlAttribute... attributes) {
        map.set(variableName, attributes);
        return this;
    }

    public SqlTable addMySqlVariable(String name, SqlVariableType type, SqlAttribute... attributes) {
        return addMySqlVariable(new SqlVariable(name, type), attributes);
    }

    public String toCreateTableString() {
        return getOrDefault(finalString, String.format("CREATE TABLE IF NOT EXISTS %s (%s);", getTableName(), getTableValues()));
    }

    public String getTableValues() {
        if (map.keySet().size() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        SqlVariable primary = null;
        for (SqlVariable variable : map.keySet()) {
            builder.append(variable.name()).append(" ").append(variable.variableName());
            SqlAttribute[] attributes = map.getOrDefault(variable, null);
            if (attributes != null && attributes.length != 0) {
                boolean alreadySet = false;
                for (SqlAttribute attribute : attributes) {
                    if (attribute.equals(SqlAttribute.PRIMARY)) {
                        primary = variable;
                    } else {
                        if (!alreadySet && variable.variableName().equals(SqlVariableType.TEXT)) {
                            builder.append(SqlAttribute.CHARACTER_SET.getText());
                            alreadySet = true;
                        }
                        builder.append(attribute.getText());
                    }
                }
            }
            builder.append(", ");
        }
        if (primary != null) {
            builder.append(String.format(SqlAttribute.PRIMARY.getText(), primary.name())).append(", ");
        }

        return builder.substring(0, builder.length() - 2);
    }

    public String getTableName() {
        return getOrDefault(String.format("`%s`", tableName), "DEFAULT_NULL_TABLE");
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public OpLinkedMap<SqlVariable, SqlAttribute[]> getMap() {
        return map;
    }

    public List<SqlAttribute> getAttributes(SqlVariable variable) {
        return Arrays.asList(getMap().getMap().get(variable));
    }
}
