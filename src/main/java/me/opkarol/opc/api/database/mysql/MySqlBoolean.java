package me.opkarol.opc.api.database.mysql;

import org.jetbrains.annotations.NotNull;

public record MySqlBoolean(boolean b) {

    public boolean getBoolean() {
        return b;
    }

    public static @NotNull String fromBoolean(boolean b) {
        return new MySqlBoolean(b).toString();
    }

    @Override
    public @NotNull String toString() {
        return String.valueOf(getBoolean() ? 1 : 0);
    }
}
