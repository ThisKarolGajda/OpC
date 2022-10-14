package me.opkarol.opc.api.database.mysql.resultset;

import me.opkarol.opc.api.location.OpSerializableLocation;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public record OpMResultSet(ResultSet set) {

    public String getText(String column) {
        try {
            return set.getString(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public int getInt(String column) {
        try {
            return set.getInt(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public @NotNull OpSerializableLocation getLocation(String column) {
        try {
            return new OpSerializableLocation(set.getString(column));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new OpSerializableLocation();
    }

    public @NotNull UUID getUUID(String column) {
        return UUID.fromString(getText(column));
    }
}
