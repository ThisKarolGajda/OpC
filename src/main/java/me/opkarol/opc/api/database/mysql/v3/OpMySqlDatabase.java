package me.opkarol.opc.api.database.mysql.v3;

import me.opkarol.opc.api.database.mysql.MySqlDatabase;
import me.opkarol.opc.api.database.mysql.MySqlDeleteTable;
import me.opkarol.opc.api.database.mysql.MySqlInsertTable;
import me.opkarol.opc.api.database.mysql.MySqlTable;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;

public record OpMySqlDatabase(String tableName, MySqlDatabase database, MySqlObjects objects) {

    public void delete(Object object) {
        MySqlDeleteTable table = new MySqlDeleteTable(getTable());
        objects.getObjectList().forEach(o -> table.addDeletion(o.getVariable(), o.object().apply(object)));
        database.delete(table);
    }

    public @NotNull MySqlTable getTable() {
        MySqlTable table = new MySqlTable(tableName);
        objects.getObjectList().forEach(o -> table.addMySqlVariable(o.getVariable(), o.attributes()));
        return table;
    }

    public void insert(Object object) {
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
}
