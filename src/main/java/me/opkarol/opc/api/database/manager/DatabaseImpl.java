package me.opkarol.opc.api.database.manager;

import me.opkarol.opc.api.database.manager.settings.FlatDatabaseSettingsFactory;
import me.opkarol.opc.api.database.manager.settings.SqlDatabaseSettings;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@SuppressWarnings("all")
public class DatabaseImpl<O, C> {
    private static DatabaseFactory databasePlugin;
    private static DatabaseImpl plugin;
    private DatabaseFactory<O, C> databaseInterface;

    {
        plugin = this;
    }

    public DatabaseImpl(@NotNull Class<? extends O> clazz, String fileName, Function<O, C> getObject) {
        this.databaseInterface = setDatabasePluginWithSql(fileName, getObject, clazz);
    }

    public DatabaseImpl(Class<O> clazz, SqlDatabaseSettings sqlDatabaseSettings, String fileName, Function<O, C> getObject) {
        databaseInterface = setDatabasePluginWithSql(sqlDatabaseSettings, fileName, getObject, clazz);
    }

    public DatabaseImpl(@NotNull Class<O> clazz, String fileName, Function<O, C> getObject, boolean mysqlEnabled) {
        if (mysqlEnabled) {
            databaseInterface = setDatabasePluginWithSql(fileName, getObject, clazz);
        } else {
            databaseInterface = setDatabasePlugin(fileName, getObject);
        }
    }

    public static <O, C> DatabaseImpl<O, C> getPlugin() {
        return (DatabaseImpl<O, C>) plugin;
    }

    public static <O, C> DatabaseFactory<O, C> getInterface() {
        return (DatabaseFactory<O, C>) databasePlugin;
    }

    public static <O, C> IDefaultDatabase<O, C> getDatabase() {
        return (IDefaultDatabase<O, C>) databasePlugin.getLocalDatabase();
    }

    public DatabaseFactory<O, C> setDatabasePluginWithSql(String fileName, Function<O, C> getObject, Class<? extends O> clazz) {
        DatabaseImpl.databasePlugin = new DatabaseFactory(new SqlDatabaseSettings(), new FlatDatabaseSettingsFactory(fileName, (o, o2) -> {
            return getObject.apply((O) o).equals(o2);
        }), clazz);
        return (DatabaseFactory<O, C>) DatabaseImpl.databasePlugin;
    }

    public DatabaseFactory<O, C> setDatabasePluginWithSql(SqlDatabaseSettings sqlDatabaseSettings, String fileName, Function<O, C> getObject, Class<O> clazz) {
        DatabaseImpl.databasePlugin = new DatabaseFactory(sqlDatabaseSettings, new FlatDatabaseSettingsFactory(fileName, (o, o2) -> {
            return getObject.apply((O) o).equals(o2);
        }), clazz);
        return (DatabaseFactory<O, C>) DatabaseImpl.databasePlugin;
    }

    public DatabaseFactory<O, C> setDatabasePlugin(String fileName, Function<O, C> getObject) {
        DatabaseImpl.databasePlugin = new DatabaseFactory(new FlatDatabaseSettingsFactory(fileName, (o, o2) -> {
            return getObject.apply((O) o).equals(o2);
        }));
        return (DatabaseFactory<O, C>) DatabaseImpl.databasePlugin;
    }

    public void set(DatabaseFactory<O, C> databasePlugin) {
        DatabaseImpl.databasePlugin = databasePlugin;
    }

    public DatabaseFactory<O, C> getDatabaseInterface() {
        return databaseInterface;
    }
}
