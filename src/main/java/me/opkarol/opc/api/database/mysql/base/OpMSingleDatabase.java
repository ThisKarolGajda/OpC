package me.opkarol.opc.api.database.mysql.base;

import me.opkarol.opc.api.database.mysql.OpMConnection;
import me.opkarol.opc.api.database.mysql.objects.OpMObjects;
import me.opkarol.opc.api.database.mysql.table.MySqlDeleteTable;
import me.opkarol.opc.api.database.mysql.table.MySqlInsertTable;
import me.opkarol.opc.api.database.mysql.table.MySqlTable;
import me.opkarol.opc.api.database.mysql.types.MySqlVariableType;
import me.opkarol.opc.api.database.mysql.types.OpMCounter;
import me.opkarol.opc.api.files.Configuration;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.Objects;

public class OpMSingleDatabase<O> {
    private final String tableName;
    private final OpMConnection database;
    private final OpMObjects<O> objects;
    private final MySqlTable table;
    private final OpMCounter counter;

    public OpMSingleDatabase(String tableName, OpMConnection database, @NotNull OpMObjects<O> objects) {
        this.tableName = tableName;
        this.database = database;
        this.objects = objects;
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.attributes()));
        this.table = table;
        this.counter = new OpMCounter(database, this.tableName);
    }

    public void delete(O object) {
        MySqlDeleteTable table = new MySqlDeleteTable(getTable());
        objects.getObjectList().forEach(o -> {
            if (!o.isIgnoredInSearch()) {
                table.addDeletion(o.getVariable(), o.object().apply(object));
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
                table.addValue(o.getVariable(), o.object().apply(object));
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

    public String tableName() {
        return tableName;
    }

    public OpMConnection database() {
        return database;
    }

    public OpMObjects<O> objects() {
        return objects;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (OpMSingleDatabase) obj;
        return Objects.equals(this.tableName, that.tableName) &&
                Objects.equals(this.database, that.database) &&
                Objects.equals(this.objects, that.objects);
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

