package me.opkarol.opc.api.database.mysql.resultset;

import me.opkarol.opc.api.location.OpSerializableLocation;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

public record OpMResultSet(ResultSet set) {

    public @Nullable String getText(String column) {
        try {
            return set.getString(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getInt(String column) {
        try {
            return set.getInt(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public @Nullable OpSerializableLocation getLocation(String column) {
        try {
            return new OpSerializableLocation(set.getString(column));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
