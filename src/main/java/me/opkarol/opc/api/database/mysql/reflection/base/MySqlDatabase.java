package me.opkarol.opc.api.database.mysql.reflection.base;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.database.manager.settings.MySqlDatabaseSettings;
import me.opkarol.opc.api.database.mysql.MySqlConnection;
import me.opkarol.opc.api.database.mysql.reflection.MySqlReflection;
import me.opkarol.opc.api.database.mysql.reflection.objects.MySqlObjects;
import me.opkarol.opc.api.database.mysql.table.MySqlDeleteTable;
import me.opkarol.opc.api.database.mysql.table.MySqlInsertTable;
import me.opkarol.opc.api.database.mysql.table.MySqlTable;
import me.opkarol.opc.api.database.mysql.types.MySqlVariableType;
import me.opkarol.opc.api.database.mysql.types.MySqlIdCount;
import me.opkarol.opc.api.file.Configuration;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;

public abstract class MySqlDatabase<O> {
    private final String tableName;
    private final MySqlConnection database;
    private final MySqlObjects objects;
    private final MySqlTable table;
    private final MySqlIdCount counter;
    private final Class<O> type;

    public MySqlDatabase(@NotNull Class<O> object) {
        type = object;
        MySqlReflection reflection = new MySqlReflection(object);
        this.tableName = reflection.getTableName();
        this.database = new MySqlConnection(OpAPI.getConfig(), "mysql");
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.getAttributes()));
        this.table = table;
        this.counter = new MySqlIdCount(this.database, this.tableName);
    }

    @SuppressWarnings("unchecked")
    public MySqlDatabase(Configuration configuration) {
        type = (Class<O>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        MySqlReflection reflection = new MySqlReflection(type);
        this.tableName = reflection.getTableName();
        this.database = new MySqlConnection(configuration, "mysql");
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.getAttributes()));
        this.table = table;
        this.counter = new MySqlIdCount(this.database, this.tableName);
    }

    @SuppressWarnings("unchecked")
    public MySqlDatabase(String mysql) {
        type = (Class<O>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        MySqlReflection reflection = new MySqlReflection(type);
        this.tableName = reflection.getTableName();
        this.database = new MySqlConnection(OpAPI.getConfig(), mysql);
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.getAttributes()));
        this.table = table;
        this.counter = new MySqlIdCount(this.database, this.tableName);
    }

    @SuppressWarnings("unchecked")
    public MySqlDatabase(@NotNull MySqlDatabaseSettings mysql) {
        type = (Class<O>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        MySqlReflection reflection = new MySqlReflection(type);
        this.tableName = reflection.getTableName();
        this.database = new MySqlConnection(mysql.getJdbc(), mysql.getUser(), mysql.getPassword());
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.getAttributes()));
        this.table = table;
        this.counter = new MySqlIdCount(this.database, this.tableName);
    }

    public MySqlDatabase(Class<O> clazz, @NotNull MySqlDatabaseSettings mysql) {
        type = clazz;
        MySqlReflection reflection = new MySqlReflection(type);
        this.tableName = reflection.getTableName();
        this.database = new MySqlConnection(mysql.getJdbc(), mysql.getUser(), mysql.getPassword());
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.getAttributes()));
        this.table = table;
        this.counter = new MySqlIdCount(this.database, this.tableName);
    }

    @SuppressWarnings("unchecked")
    public MySqlDatabase(Configuration configuration, String mysql) {
        type = (Class<O>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        MySqlReflection reflection = new MySqlReflection(type);
        this.tableName = reflection.getTableName();
        this.database = new MySqlConnection(configuration, mysql);
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.getAttributes()));
        this.table = table;
        this.counter = new MySqlIdCount(this.database, this.tableName);
    }

    @SuppressWarnings("unchecked")
    public MySqlDatabase(Class<?> clazz, Configuration configuration, String mysql) {
        type = (Class<O>) clazz;
        MySqlReflection reflection = new MySqlReflection(type);
        this.tableName = reflection.getTableName();
        this.database = new MySqlConnection(configuration, mysql);
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.getAttributes()));
        this.table = table;
        this.counter = new MySqlIdCount(this.database, this.tableName);
    }

    @SuppressWarnings("unchecked")
    public MySqlDatabase() {
        type = (Class<O>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        MySqlReflection reflection = new MySqlReflection(type);
        this.tableName = reflection.getTableName();
        this.database = new MySqlConnection(OpAPI.getConfig(), "mysql");
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.getAttributes()));
        this.table = table;
        this.counter = new MySqlIdCount(this.database, this.tableName);
    }

    public void delete(O object) {
        MySqlDeleteTable table = new MySqlDeleteTable(getTable());
        objects.getObjectList().forEach(o -> {
            if (o.isNotIgnoredInSearch()) {
                table.addDeletion(o.getVariable(), o.getObject(object));
            }
        });
        database.delete(table);
    }

    public int insert(O object, int lastId) {
        MySqlInsertTable table = new MySqlInsertTable(getTable());
        final int[] i = {lastId};
        objects.getObjectList().forEach(o -> {
            if (o.isPrimary() && o.getType().equals(MySqlVariableType.INT) && lastId < 0) {
                i[0] = counter.getNextId();
                table.addValue(o.getVariable(), i[0]);
            } else {
                table.addValue(o.getVariable(), o.getObject(object));
            }
        });
        database.insert(table);
        return i[0];
    }

    public ResultSet get() {
        return database.get(getTable());
    }

    public void create() {
        database.create(getTable());
    }

    public void close() {
        database.close();
    }

    public void setup() {
        database.setup();
    }

    public void setup(String jdbcUrl, String username, String password) {
        database.setup(jdbcUrl, username, password);
    }

    public void setup(@NotNull Configuration configuration, String path) {
        database.setup(configuration, path);
    }

    public String getTableName() {
        return tableName;
    }

    public MySqlConnection getDatabase() {
        return database;
    }

    public MySqlObjects getObjects() {
        return objects;
    }

    public MySqlTable getTable() {
        return table;
    }

    public MySqlIdCount getCounter() {
        return counter;
    }

    public Class<O> getType() {
        return type;
    }
}