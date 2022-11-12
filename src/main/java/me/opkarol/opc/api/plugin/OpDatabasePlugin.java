package me.opkarol.opc.api.plugin;

import me.opkarol.opc.api.database.manager.Database;
import me.opkarol.opc.api.database.manager.DatabaseHolder;
import me.opkarol.opc.api.database.manager.DatabaseImpl;
import me.opkarol.opc.api.database.manager.IDefaultDatabase;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class OpDatabasePlugin<O, C> extends OpPlugin {
    private static DatabaseImpl<?, ?> databaseInterface;

    public IDefaultDatabase<O, C> getLocalDatabase() {
        return (IDefaultDatabase<O, C>) databaseInterface.getLocalDatabase();
    }

    public static <O, C> IDefaultDatabase<O, C> getDatabaseInterface() {
        return (IDefaultDatabase<O, C>) databaseInterface.getLocalDatabase();
    }

    public static <O, C> void setDatabaseInterface(DatabaseImpl<O, C> databaseInterface) {
        OpDatabasePlugin.databaseInterface = databaseInterface;
    }

    public static <O, C> void setDatabasePlugin(@NotNull Database<O, C> databasePlugin) {
        OpDatabasePlugin.databaseInterface = databasePlugin.getDatabaseInterface();
    }

    @Override
    public void onEnable() {
        setDatabasePlugin(getBase());
        DatabaseHolder.setDatabase((DatabaseImpl<UUID, String>) getLocalDatabase());
        super.onEnable();
    }

    public abstract Database<O, C> getBase();
}
