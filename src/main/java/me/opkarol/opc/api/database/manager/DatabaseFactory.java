package me.opkarol.opc.api.database.manager;

import me.opkarol.opc.api.database.flat.DefaultFlatDatabase;
import me.opkarol.opc.api.database.manager.settings.FlatDatabaseSettings;
import me.opkarol.opc.api.database.manager.settings.MySqlDatabaseSettings;
import me.opkarol.opc.api.database.mysql.reflection.base.OpMySqlDatabaseImpl;
import org.jetbrains.annotations.NotNull;

public class DatabaseFactory<O, C> {
    private final MySqlDatabaseSettings mySqlSettings;
    private final FlatDatabaseSettings<O, C> flatSettings;
    private static IDefaultDatabase database;

    public DatabaseFactory(@NotNull MySqlDatabaseSettings mysqlSettings) {
        this.mySqlSettings = mysqlSettings;
        this.flatSettings = null;
        if (mysqlSettings.isEnabled()) {
            database = new OpMySqlDatabaseImpl<>(mysqlSettings);
        } else {
            database = null;
        }
    }

    public DatabaseFactory(FlatDatabaseSettings<O, C> flatSettings) {
        this.flatSettings = flatSettings;
        this.mySqlSettings = null;
        database = new DefaultFlatDatabase<>(flatSettings);
    }

    public DatabaseFactory(@NotNull MySqlDatabaseSettings mysqlSettings, FlatDatabaseSettings<O, C> flatSettings) {
        this.mySqlSettings = mysqlSettings;
        this.flatSettings = flatSettings;
        if (mysqlSettings.isEnabled()) {
            database = new OpMySqlDatabaseImpl<>(mysqlSettings);
        } else {
            database = new DefaultFlatDatabase<>(flatSettings);
        }
    }

    public DatabaseFactory(@NotNull MySqlDatabaseSettings mysqlSettings, FlatDatabaseSettings<O, C> flatSettings, Class<? extends O> clazz) {
        this.mySqlSettings = mysqlSettings;
        this.flatSettings = flatSettings;
        if (mysqlSettings.isEnabled()) {
            database = new OpMySqlDatabaseImpl<>(mysqlSettings, clazz);
        } else {
            database = new DefaultFlatDatabase<>(flatSettings);
        }
    }

    public FlatDatabaseSettings<O, C> getFlatSettings() {
        return flatSettings;
    }

    public MySqlDatabaseSettings getMySqlSettings() {
        return mySqlSettings;
    }

    public static <O, C> IDefaultDatabase<O, C> getDatabase() {
        return (IDefaultDatabase<O, C>) database;
    }

    public IDefaultDatabase<O, C> getLocalDatabase() {
        return (IDefaultDatabase<O, C>) database;
    }
}
