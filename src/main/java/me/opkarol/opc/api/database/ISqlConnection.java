package me.opkarol.opc.api.database;

import me.opkarol.opc.api.database.mysql.table.SqlDeleteTable;
import me.opkarol.opc.api.database.mysql.table.SqlInsertTable;
import me.opkarol.opc.api.database.mysql.table.SqlTable;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ISqlConnection {
    void setup();

    void close();

    void create(@NotNull SqlTable table);

    void insert(@NotNull SqlInsertTable table);

    void delete(@NotNull SqlDeleteTable table);

    ResultSet get(SqlTable getValue);

    void run(String statement);

    ResultSet query(String statement) throws SQLException;
}
