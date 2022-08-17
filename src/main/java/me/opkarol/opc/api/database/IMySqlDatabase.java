package me.opkarol.opc.api.database;

import me.opkarol.opc.api.database.mysql.MySqlDeleteTable;
import me.opkarol.opc.api.database.mysql.MySqlInsertTable;
import me.opkarol.opc.api.database.mysql.MySqlTable;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;

public interface IMySqlDatabase {
    void setup();

    void close();

    void create(@NotNull MySqlTable table);

    void insert(@NotNull MySqlInsertTable table);

    void delete(@NotNull MySqlDeleteTable table);

    ResultSet get(String getValue);
}
