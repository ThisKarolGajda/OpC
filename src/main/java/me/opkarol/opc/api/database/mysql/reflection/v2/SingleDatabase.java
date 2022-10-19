package me.opkarol.opc.api.database.mysql.reflection.v2;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.database.mysql.OpMConnection;
import me.opkarol.opc.api.database.mysql.reflection.OpMReflection;
import me.opkarol.opc.api.database.mysql.table.MySqlDeleteTable;
import me.opkarol.opc.api.database.mysql.table.MySqlInsertTable;
import me.opkarol.opc.api.database.mysql.table.MySqlTable;
import me.opkarol.opc.api.database.mysql.types.MySqlVariableType;
import me.opkarol.opc.api.database.mysql.types.OpMCounter;
import me.opkarol.opc.api.files.Configuration;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.Objects;

public class SingleDatabase<O> {
    private final String tableName;
    private final OpMConnection database;
    private final me.opkarol.opc.api.database.mysql.reflection.Objects objects;
    private final MySqlTable table;
    private final OpMCounter counter;

    public SingleDatabase(OpMConnection database, @NotNull OpMReflection<O> reflection) {
        this.tableName = reflection.getTableName();
        this.database = database;
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.attributes()));
        this.table = table;
        OpAPI.getInstance().getLogger().info(tableName + " -= -= -=-= -=-= == " + this.tableName);

        this.counter = new OpMCounter(database, this.tableName);
    }

    public SingleDatabase(String configurationPath, @NotNull OpMReflection<O> reflection) {
        this.tableName = reflection.getTableName();
        this.database = new OpMConnection(OpAPI.getConfig(), configurationPath);
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.attributes()));
        this.table = table;
        OpAPI.getInstance().getLogger().info(tableName + " -= -= -=-= -=-= == " + this.tableName);

        this.counter = new OpMCounter(database, this.tableName);
    }

    public SingleDatabase(String configurationPath, @NotNull O object) {
        OpMReflection<O> reflection = new OpMReflection<>(object);
        this.tableName = reflection.getTableName();
        this.database = new OpMConnection(OpAPI.getConfig(), configurationPath);
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.attributes()));
        this.table = table;
        OpAPI.getInstance().getLogger().info(tableName + " -= -= -=-= -=-= == " + this.tableName);

        this.counter = new OpMCounter(database, this.tableName);
    }

    public SingleDatabase(OpMConnection database, @NotNull O object) {
        OpMReflection<O> reflection = new OpMReflection<>(object);
        this.tableName = reflection.getTableName();
        this.database = database;
        this.objects = reflection.getObjects();
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.attributes()));
        this.table = table;
        OpAPI.getInstance().getLogger().info(tableName + " -= -= -=-= -=-= == " + this.tableName);

        this.counter = new OpMCounter(database, this.tableName);
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
            if (o.isPrimary() && o.type().equals(MySqlVariableType.INT) && lastId < 0) {
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

    public me.opkarol.opc.api.database.mysql.reflection.Objects getObjects() {
        return objects;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, database, objects);
    }

    @Override
    public String toString() {
        return "OpMySqlDatabase[" +
                "tableName=" + tableName + ", " +
                "database=" + database + ", " +
                "objects=" + objects + ']';
    }

    public MySqlTable getTable() {
        return table;
    }

    public OpMCounter getCounter() {
        return counter;
    }
}
