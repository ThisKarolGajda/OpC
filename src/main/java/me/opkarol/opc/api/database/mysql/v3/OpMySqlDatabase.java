package me.opkarol.opc.api.database.mysql.v3;

import me.opkarol.opc.api.database.mysql.MySqlDeleteTable;
import me.opkarol.opc.api.database.mysql.MySqlInsertTable;
import me.opkarol.opc.api.database.mysql.MySqlTable;
import me.opkarol.opc.api.files.Configuration;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.Objects;

public final class OpMySqlDatabase<O> {
    private final String tableName;
    private final MySqlConnection database;
    private final MySqlObjects<O> objects;
    private final MySqlTable table;

    public OpMySqlDatabase(String tableName, MySqlConnection database, @NotNull MySqlObjects<O> objects) {
        this.tableName = tableName;
        this.database = database;
        this.objects = objects;
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.attributes()));
        this.table = table;
    }

    public void delete(O object) {
        MySqlDeleteTable table = new MySqlDeleteTable(getTable());
        objects.getObjectList().forEach(o -> table.addDeletion(o.getVariable(), o.object().apply(object)));
        database.delete(table);
    }

    public void insert(O object) {
        MySqlInsertTable table = new MySqlInsertTable(getTable());
        objects.getObjectList().forEach(o -> table.addValue(o.getVariable(), o.object().apply(object)));
        database.insert(table);
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

    public MySqlConnection database() {
        return database;
    }

    public MySqlObjects<O> objects() {
        return objects;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (OpMySqlDatabase) obj;
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
}
