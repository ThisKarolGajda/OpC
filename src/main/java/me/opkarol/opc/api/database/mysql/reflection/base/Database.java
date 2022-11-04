package me.opkarol.opc.api.database.mysql.reflection.base;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.database.manager.settings.MySqlDatabaseSettings;
import me.opkarol.opc.api.database.mysql.OpMConnection;
import me.opkarol.opc.api.database.mysql.reflection.OpMReflection;
import me.opkarol.opc.api.database.mysql.reflection.objects.Objects;
import me.opkarol.opc.api.database.mysql.table.MySqlDeleteTable;
import me.opkarol.opc.api.database.mysql.table.MySqlInsertTable;
import me.opkarol.opc.api.database.mysql.table.MySqlTable;
import me.opkarol.opc.api.database.mysql.types.MySqlVariableType;
import me.opkarol.opc.api.database.mysql.types.OpMCounter;
import me.opkarol.opc.api.files.Configuration;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.util.Arrays;

public abstract class Database<O> {
    private final String tableName;
    private final OpMConnection database;
    private final Objects objects;
    private final MySqlTable table;
    private final OpMCounter counter;
    private final Class<O> type;

    public Database(@NotNull Class<O> object) {
        type = object;
        OpMReflection reflection = new OpMReflection(object);
        this.tableName = reflection.getTableName();
        this.database = new OpMConnection(OpAPI.getConfig(), "mysql");
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.getAttributes()));
        this.table = table;
        this.counter = new OpMCounter(this.database, this.tableName);
    }

    @SuppressWarnings("unchecked")
    public Database(Configuration configuration) {
        type = (Class<O>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        OpMReflection reflection = new OpMReflection(type);
        this.tableName = reflection.getTableName();
        this.database = new OpMConnection(configuration, "mysql");
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.getAttributes()));
        this.table = table;
        this.counter = new OpMCounter(this.database, this.tableName);
    }

    @SuppressWarnings("unchecked")
    public Database(String mysql) {
        type = (Class<O>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        OpMReflection reflection = new OpMReflection(type);
        this.tableName = reflection.getTableName();
        this.database = new OpMConnection(OpAPI.getConfig(), mysql);
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.getAttributes()));
        this.table = table;
        this.counter = new OpMCounter(this.database, this.tableName);
    }

    @SuppressWarnings("unchecked")
    public Database(@NotNull MySqlDatabaseSettings mysql) {
        type = (Class<O>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        OpMReflection reflection = new OpMReflection(type);
        this.tableName = reflection.getTableName();
        this.database = new OpMConnection(mysql.getJdbc(), mysql.getUser(), mysql.getPassword());
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.getAttributes()));
        this.table = table;
        this.counter = new OpMCounter(this.database, this.tableName);
    }

    public Database(Class<O> clazz, @NotNull MySqlDatabaseSettings mysql) {
        type = clazz;
        OpMReflection reflection = new OpMReflection(type);
        this.tableName = reflection.getTableName();
        this.database = new OpMConnection(mysql.getJdbc(), mysql.getUser(), mysql.getPassword());
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.getAttributes()));
        this.table = table;
        this.counter = new OpMCounter(this.database, this.tableName);
    }

    @SuppressWarnings("unchecked")
    public Database(Configuration configuration, String mysql) {
        type = (Class<O>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        OpMReflection reflection = new OpMReflection(type);
        this.tableName = reflection.getTableName();
        this.database = new OpMConnection(configuration, mysql);
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.getAttributes()));
        this.table = table;
        this.counter = new OpMCounter(this.database, this.tableName);
    }

    @SuppressWarnings("unchecked")
    public Database(Class<?> clazz, Configuration configuration, String mysql) {
        type = (Class<O>) clazz;
        OpMReflection reflection = new OpMReflection(type);
        this.tableName = reflection.getTableName();
        this.database = new OpMConnection(configuration, mysql);
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.getAttributes()));
        this.table = table;
        this.counter = new OpMCounter(this.database, this.tableName);
    }

    @SuppressWarnings("unchecked")
    public Database() {
        type = (Class<O>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        OpMReflection reflection = new OpMReflection(type);
        this.tableName = reflection.getTableName();
        this.database = new OpMConnection(OpAPI.getConfig(), "mysql");
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.getAttributes()));
        this.table = table;
        this.counter = new OpMCounter(this.database, this.tableName);
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

    public OpMConnection getDatabase() {
        return database;
    }

    public Objects getObjects() {
        return objects;
    }

    public MySqlTable getTable() {
        return table;
    }

    public OpMCounter getCounter() {
        return counter;
    }

    public Class<O> getType() {
        return type;
    }
}