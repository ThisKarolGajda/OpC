package me.opkarol.opc.api.database.mysql.types;

import me.opkarol.opc.api.database.mysql.SqlConnection;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlIdCounter {
    private final String tableName;
    private int id = -1;

    public SqlIdCounter(SqlConnection connection, String tableName) {
        this.tableName = tableName;
        loadLastId(connection);
    }

    public int getNextId() {
        id++;
        return id;
    }

    @Deprecated
    public int getId() {
        return id;
    }

    public void loadLastId(@NotNull SqlConnection connection) {
        ResultSet set;
        try {
            set = connection.query(String.format("SELECT MAX(`id`) FROM `%s`;", tableName));
            if (set == null) {
                id = 0;
                return;
            }
            if (set.next()) {
                id = set.getInt("MAX(`id`)");
            }
        } catch (SQLException ignore) {
        }
    }
}
