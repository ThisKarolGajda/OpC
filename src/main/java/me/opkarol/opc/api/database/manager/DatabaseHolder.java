package me.opkarol.opc.api.database.manager;

public class DatabaseHolder<O, C> {
    private static DatabaseImpl<?, ?> database;

    public static <O, C> void setDatabase(DatabaseImpl<O, C> database) {
        DatabaseHolder.database = database;
    }

    public static DatabaseImpl<?, ?> getDatabase() {
        return database;
    }

    public IDefaultDatabase<O, C> getLocalDatabase() {
        return (IDefaultDatabase<O, C>) database.getLocalDatabase();
    }
}
