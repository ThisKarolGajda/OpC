package me.opkarol.opc.api.database.mysql.reflection.base;

import me.opkarol.opc.api.database.manager.settings.SqlDatabaseSettings;
import me.opkarol.opc.api.database.mysql.SqlConnection;
import me.opkarol.opc.api.database.mysql.reflection.SqlReflection;
import me.opkarol.opc.api.database.mysql.reflection.objects.SqlReflectionObjects;
import me.opkarol.opc.api.database.mysql.table.SqlDeleteTable;
import me.opkarol.opc.api.database.mysql.table.SqlInsertTable;
import me.opkarol.opc.api.database.mysql.table.SqlTable;
import me.opkarol.opc.api.database.mysql.types.SqlIdCounter;
import me.opkarol.opc.api.database.mysql.types.SqlVariableType;
import me.opkarol.opc.api.utils.VariableUtil;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.Objects;

public abstract class SqlDatabaseHolder<O> {
    private final SqlConnection database;
    private final SqlTable table;
    private final SqlIdCounter counter;
    private final Class<O> classType;
    private final SqlReflection reflection;

    public SqlDatabaseHolder(Class<O> clazz, @NotNull SqlDatabaseSettings settings) {
        this.classType = clazz;
        this.reflection = new SqlReflection(classType);
        this.database = new SqlConnection(settings);
        SqlTable table = new SqlTable(getTableName());
        addVariables(table);
        this.table = table;
        this.counter = new SqlIdCounter(this.database, this.getTableName());
    }

    public void addVariables(SqlTable table) {
        getObjects().getObjectList().stream()
                .filter(Objects::nonNull)
                .forEach(o -> table.addMySqlVariable(o.getVariable(), o.getAttributes()));
    }

    public void deleteObject(O object) {
        SqlDeleteTable table = new SqlDeleteTable(getTable());
        getObjects().getObjectList().forEach(o -> {
            if (o.isNotIgnoredInSearch()) {
                table.addDeletion(o.getVariable(), o.getObject(object));
            }
        });
        database.delete(table);
    }

    public int insertObject(O object, int lastId) {
        SqlInsertTable table = new SqlInsertTable(getTable());
        final int[] i = {lastId};
        getObjects().getObjectList().forEach(o -> {
            if (o.isPrimary() && o.getType().equals(SqlVariableType.INT) && lastId < 0) {
                i[0] = counter.getNextId();
                table.addValue(o.getVariable(), i[0]);
            } else {
                table.addValue(o.getVariable(), o.getObject(object));
            }
        });
        database.insert(table);
        return i[0];
    }

    public ResultSet getResultSet() {
        return database.get(getTable());
    }

    public void createTable() {
        database.create(getTable());
    }

    public void close() {
        database.close();
    }

    public String getTableName() {
        return VariableUtil.getOrDefault(reflection.getTableName(), "table");
    }

    public SqlConnection getDatabase() {
        return database;
    }

    public SqlReflectionObjects getObjects() {
        return reflection.getObjects();
    }

    public SqlTable getTable() {
        return table;
    }

    public SqlIdCounter getCounter() {
        return counter;
    }

    public Class<O> getClassType() {
        return classType;
    }
}