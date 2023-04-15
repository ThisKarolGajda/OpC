package me.opkarol.opc.api.database.manager.settings;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.file.Configuration;
import me.opkarol.opc.api.utils.VariableUtil;
import org.jetbrains.annotations.NotNull;

public final class SqlDatabaseSettings {
    private final boolean enabled;
    private final String jdbc;
    private final String user;
    private final String password;

    public SqlDatabaseSettings(boolean enabled, String jdbc, String user, String password) {
        this.enabled = enabled;
        this.jdbc = jdbc;
        this.user = user;
        this.password = password;
    }

    public SqlDatabaseSettings(@NotNull Configuration configuration, String path) {
        path = VariableUtil.ifNotEndsWithAdd(path, ".");
        this.enabled = configuration.getBoolean(path + "enabled");
        this.jdbc = configuration.getString(path + "jdbc");
        this.user = configuration.getString(path + "user");
        this.password = configuration.getString(path + "password");
    }

    public SqlDatabaseSettings(String path) {
        this(OpAPI.getConfig(), path);
    }

    public SqlDatabaseSettings() {
        this(OpAPI.getConfig(), "mysql");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getJdbc() {
        return jdbc;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
