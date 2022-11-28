package me.opkarol.opc.api.plugin;

import me.opkarol.opc.api.database.manager.Database;
import me.opkarol.opc.api.database.manager.DatabaseImpl;
import me.opkarol.opc.api.database.manager.IDefaultDatabase;
import me.opkarol.opc.api.misc.TriOptional;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.util.function.Function;

public abstract class OpDatabasePlugin<O, C> extends OpPlugin {
    private static DatabaseImpl<?, ?> databaseInterface;
    private Class<O> clazz;

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

    @SuppressWarnings("unchecked")
    public OpDatabasePlugin(String fileName, Function<O, C> getObject) {
        clazz = (Class<O>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        setDatabasePlugin(new Database<>(clazz, fileName, getObject) {});
    }

    @Override
    public void onEnable() {
        //setDatabasePlugin(getDatabaseAsBase(getBase()));
        super.onEnable();
    }

    public abstract TriOptional<Class<O>, String, Function<O, C>> getBase();

    @Contract("_ -> new")
    private @NotNull Database<O, C> getDatabaseAsBase(@NotNull TriOptional<Class<O>, String, Function<O, C>> base) {
        return new Database<>(base.getFirstObject(), base.getSecondObject(), base.getThirdObject()) {};
    }

}
