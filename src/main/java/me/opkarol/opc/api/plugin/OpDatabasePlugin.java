package me.opkarol.opc.api.plugin;

import me.opkarol.opc.api.database.manager.Database;
import me.opkarol.opc.api.database.manager.DatabaseImpl;
import me.opkarol.opc.api.database.manager.IDefaultDatabase;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.misc.BiOptional;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

public abstract class OpDatabasePlugin<O, C> extends OpPlugin {
    private static DatabaseImpl<?, ?> databaseInterface;
    private final OpMap<Class<?>, Object> savedInstances = new OpMap<>();

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
    }

    public abstract BiOptional<String, Function<O, C>> getBase();

    @SuppressWarnings("all")
    private @NotNull Database<O, C> getDatabaseAsBase(@NotNull BiOptional<String, Function<O, C>> base) {
        return new Database<>(getClassInstance(), base.getFirst().get(), base.getSecond().get()) {};
    }

    public abstract Class<? extends O> getClassInstance();

    public OpMap<Class<?>, ?> getSavedInstances() {
        return savedInstances;
    }

    public <K> OpDatabasePlugin<O, C> saveInstance(K k) {
        savedInstances.set(k.getClass(), k);
        return this;
    }

    public <K> Optional<Object> getInstance(Class<K> kClass) {
        return savedInstances.getByKey(kClass);
    }
}
