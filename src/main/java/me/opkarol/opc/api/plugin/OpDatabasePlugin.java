package me.opkarol.opc.api.plugin;

import me.opkarol.opc.api.database.manager.Database;
import me.opkarol.opc.api.database.manager.DatabaseImpl;
import me.opkarol.opc.api.database.manager.IDefaultDatabase;
import me.opkarol.opc.api.misc.BiOptional;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

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
        setDatabasePlugin(getDatabaseAsBase(getBase()));
        super.onEnable();
    }

    public abstract BiOptional<String, Function<O, C>> getBase();

    @Contract("_ -> new")
    @SuppressWarnings("all")
    private @NotNull Database<O, C> getDatabaseAsBase(BiOptional<String, Function<O, C>> base) {
        return new Database<>(getClassInstance(), base.getFirst().get(), base.getSecond().get()) {};
    }

    public abstract Class<O> getClassInstance();
}
