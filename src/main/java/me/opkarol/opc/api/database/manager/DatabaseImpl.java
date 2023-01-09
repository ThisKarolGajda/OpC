package me.opkarol.opc.api.database.manager;

import me.opkarol.opc.api.database.manager.settings.FlatDatabaseSettings;
import me.opkarol.opc.api.database.manager.settings.MySqlDatabaseSettings;
import me.opkarol.opc.api.utils.VariableUtil;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("all")
public class DatabaseImpl<O, C> {
    private static DatabaseFactory databasePlugin;
    private static DatabaseImpl plugin;
    private DatabaseFactory<O, C> databaseInterface;

    {
        plugin = this;
    }

    public DatabaseImpl(@NotNull Class<? extends O> clazz, String fileName, Function<O, C> getObject) {
        this.databaseInterface = setDatabasePluginWithMySql(fileName, getObject, clazz);
    }

    public DatabaseImpl(Class<O> clazz, MySqlDatabaseSettings mySqlDatabaseSettings, String fileName, Function<O, C> getObject) {
        databaseInterface = setDatabasePluginWithMySql(mySqlDatabaseSettings, fileName, getObject, clazz);
    }

    public DatabaseImpl(@NotNull Class<O> clazz, String fileName, Function<O, C> getObject, boolean mysqlEnabled) {
        if (mysqlEnabled) {
            databaseInterface = setDatabasePluginWithMySql(fileName, getObject, clazz);
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

    public DatabaseFactory<O, C> setDatabasePluginWithMySql(String fileName, Function<O, C> getObject, Class<? extends O> clazz) {
        DatabaseImpl.databasePlugin = new DatabaseFactory<O, C>(new MySqlDatabaseSettings(), new FlatDatabaseSettings<>(fileName) {
            @Override
            public Predicate<O> getPredicate(C object) {
                return o -> getObject.apply(o).equals(object);
            }
        }, VariableUtil.getOrDefault(clazz, null));
        return (DatabaseFactory<O, C>) DatabaseImpl.databasePlugin;
    }

    public DatabaseFactory<O, C> setDatabasePluginWithMySql(MySqlDatabaseSettings mySqlDatabaseSettings, String fileName, Function<O, C> getObject, Class<O> clazz) {
        DatabaseImpl.databasePlugin = new DatabaseFactory<>(mySqlDatabaseSettings, new FlatDatabaseSettings<O, C>(fileName) {
            @Override
            public Predicate<O> getPredicate(C object) {
                return o -> getObject.apply(o).equals(object);
            }
        }, VariableUtil.getOrDefault(clazz, null));
        return (DatabaseFactory<O, C>) DatabaseImpl.databasePlugin;
    }

    public DatabaseFactory<O, C> setDatabasePlugin(String fileName, Function<O, C> getObject) {
        DatabaseImpl.databasePlugin = new DatabaseFactory<>(new FlatDatabaseSettings<O, C>(fileName) {
            @Override
            public Predicate<O> getPredicate(C object) {
                return o -> getObject.apply(o).equals(object);
            }
        });
        return (DatabaseFactory<O, C>) DatabaseImpl.databasePlugin;
    }

    public void set(DatabaseFactory<O, C> databasePlugin) {
        DatabaseImpl.databasePlugin = databasePlugin;
    }

    public DatabaseFactory<O, C> getDatabaseInterface() {
        return databaseInterface;
    }
}
