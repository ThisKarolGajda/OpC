package me.opkarol.opc.api.database;

import me.opkarol.opc.api.database.mysql.table.MySqlDeleteTable;
import me.opkarol.opc.api.database.mysql.table.MySqlInsertTable;
import me.opkarol.opc.api.database.mysql.table.MySqlTable;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IMySqlDatabase {
    void setup();

    void close();

    void create(@NotNull MySqlTable table);

    void insert(@NotNull MySqlInsertTable table);

    void delete(@NotNull MySqlDeleteTable table);

    ResultSet get(MySqlTable getValue);

    void run(String statement);

    ResultSet query(String statement) throws SQLException;
}
