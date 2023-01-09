package me.opkarol.opc.api.database.mysql.types;

import me.opkarol.opc.api.database.mysql.MySqlConnection;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlIdCount {
    private int id = -1;
    private final String tableName;

    public MySqlIdCount(MySqlConnection connection, String tableName) {
        this.tableName = tableName;
        loadLastId(connection);
    }

    public int getNextId() {
        id+=1;
        return id;
    }

    @Deprecated
    public int getId() {
        return id;
    }

    public void loadLastId(@NotNull MySqlConnection connection) {
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
