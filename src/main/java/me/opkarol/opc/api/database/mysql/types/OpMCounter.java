package me.opkarol.opc.api.database.mysql.types;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.database.mysql.OpMConnection;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OpMCounter {
    private int id = -1;
    private final String tableName;

    public OpMCounter(OpMConnection connection, String tableName) {
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

    public void loadLastId(@NotNull OpMConnection connection) {
        OpAPI.getInstance().getLogger().info(tableName + " ------------------------------------");
        ResultSet set = connection.query(String.format("SELECT MAX(`id`) FROM `%s`;", tableName));
        if (set == null) {
            id = 0;
            return;
        }
        try {
            if (set.next()) {
                id = set.getInt("MAX(`id`)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
