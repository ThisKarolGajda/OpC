package me.opkarol.opc.api.database.manager;

import me.opkarol.opc.api.database.manager.settings.FlatDatabaseSettings;
import me.opkarol.opc.api.database.manager.settings.MySqlDatabaseSettings;
import me.opkarol.opc.api.utils.VariableUtil;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("all")
public class Database<O, C> {
    private static DatabaseImpl databasePlugin;
    private static Database plugin;
    private DatabaseImpl<O, C> databaseInterface;

    {
        plugin = this;
    }

    public Database(@NotNull Class<? extends O> clazz, String fileName, Function<O, C> getObject) {
        this.databaseInterface = setDatabasePluginWithMySql(fileName, getObject, clazz);
    }

    public Database(Class<O> clazz, MySqlDatabaseSettings mySqlDatabaseSettings, String fileName, Function<O, C> getObject) {
        databaseInterface = setDatabasePluginWithMySql(mySqlDatabaseSettings, fileName, getObject, clazz);
    }

    public Database(@NotNull Class<O> clazz, String fileName, Function<O, C> getObject, boolean mysqlEnabled) {
        if (mysqlEnabled) {
            databaseInterface = setDatabasePluginWithMySql(fileName, getObject, clazz);
        } else {
            databaseInterface = setDatabasePlugin(fileName, getObject);
        }
    }

    public static <O, C> Database<O, C> getPlugin() {
        return (Database<O, C>) plugin;
    }

    public static <O, C> DatabaseImpl<O, C> getInterface() {
        return (DatabaseImpl<O, C>) databasePlugin;
    }

    public static <O, C> IDefaultDatabase<O, C> getDatabase() {
        return (IDefaultDatabase<O, C>) databasePlugin.getLocalDatabase();
    }

    public DatabaseImpl<O, C> setDatabasePluginWithMySql(String fileName, Function<O, C> getObject, Class<? extends O> clazz) {
        Database.databasePlugin = new DatabaseImpl<O, C>(new MySqlDatabaseSettings(), new FlatDatabaseSettings<>(fileName) {
            @Override
            public Predicate<O> getPredicate(C object) {
                return o -> getObject.apply(o).equals(object);
            }
        }, VariableUtil.getOrDefault(clazz, null));
        return (DatabaseImpl<O, C>) Database.databasePlugin;
    }

    public DatabaseImpl<O, C> setDatabasePluginWithMySql(MySqlDatabaseSettings mySqlDatabaseSettings, String fileName, Function<O, C> getObject, Class<O> clazz) {
        Database.databasePlugin = new DatabaseImpl<>(mySqlDatabaseSettings, new FlatDatabaseSettings<O, C>(fileName) {
            @Override
            public Predicate<O> getPredicate(C object) {
                return o -> getObject.apply(o).equals(object);
            }
        }, VariableUtil.getOrDefault(clazz, null));
        return (DatabaseImpl<O, C>) Database.databasePlugin;
    }

    public DatabaseImpl<O, C> setDatabasePlugin(String fileName, Function<O, C> getObject) {
        Database.databasePlugin = new DatabaseImpl<>(new FlatDatabaseSettings<O, C>(fileName) {
            @Override
            public Predicate<O> getPredicate(C object) {
                return o -> getObject.apply(o).equals(object);
            }
        });
        return (DatabaseImpl<O, C>) Database.databasePlugin;
    }

    public void set(DatabaseImpl<O, C> databasePlugin) {
        Database.databasePlugin = databasePlugin;
    }

    public DatabaseImpl<O, C> getDatabaseInterface() {
        return databaseInterface;
    }
}
