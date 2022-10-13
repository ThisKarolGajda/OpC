package me.opkarol.opc.api.database.mysql.types;

import me.opkarol.opc.api.database.mysql.OpMConnection;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OpMCounter {
    private int id = -1;
    private final String tableName;

    public OpMCounter(OpMConnection connection, String tableName) {
        loadLastId(connection);
        this.tableName = tableName;
    }

    public int getNextId() {
        id+=1;
        return id;
    }

    @Deprecated
    public int getId() {
        return id;
    }

    public void loadLastId(@NotNull OpMConnection connection) {
        ResultSet set = connection.query(String.format("SELECT MAX(`id`) FROM `%s`;", tableName));
        try {
            if (set.next()) {
                id = set.getInt("MAX(`id`)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
