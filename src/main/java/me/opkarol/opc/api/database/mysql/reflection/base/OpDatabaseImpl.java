package me.opkarol.opc.api.database.mysql.reflection.base;

import me.opkarol.opc.api.autostart.OpAutoDisable;
import me.opkarol.opc.api.commands.suggestions.OpCommandSuggestion;
import me.opkarol.opc.api.database.manager.IDefaultDatabase;
import me.opkarol.opc.api.database.manager.settings.MySqlDatabaseSettings;
import me.opkarol.opc.api.files.Configuration;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class OpDatabaseImpl<O, C> extends DatabaseImpl<O, C> implements IDefaultDatabase<O, C> {
    private static OpDatabaseImpl database;

    public OpDatabaseImpl() {
        database = this;
        registerDisablement();
    }

    public OpDatabaseImpl(String mysql) {
        super(mysql);
        database = this;
        registerDisablement();
    }

    public OpDatabaseImpl(MySqlDatabaseSettings mysql) {
        super(mysql);
        database = this;
        registerDisablement();
    }

    public OpDatabaseImpl(MySqlDatabaseSettings mysql, Class<O> clazz) {
        super(clazz, mysql);
        database = this;
        registerDisablement();
    }

    public OpDatabaseImpl(Configuration configuration, String mysql) {
        super(configuration, mysql);
        database = this;
        registerDisablement();
    }

    public OpDatabaseImpl(Class<O> clazz, Configuration configuration, String mysql) {
        super(clazz, configuration, mysql);
        database = this;
        registerDisablement();
    }

    public OpDatabaseImpl<O, C> getLocalDatabase() {
        return (OpDatabaseImpl<O, C>) database;
    }

    public static <O, C> OpDatabaseImpl<O, C> getFixedInstance() {
        return (OpDatabaseImpl<O, C>) database;
    }

    public void checkAndUse(UUID uuid, C object, Runnable noObject, Consumer<O> objectFound) {
        if (contains(uuid, object)) {
            Optional<O> optional = get(uuid, object);
            if (optional.isEmpty()) {
                noObject.run();
            } else {
                objectFound.accept(optional.get());
            }
        } else {
            noObject.run();
        }
    }

    public void deleteAndUse(UUID uuid, C object, Runnable noObject, Runnable objectFound) {
        if (delete(uuid, object)) {
            objectFound.run();
        } else {
            noObject.run();
        }
    }

    public void addAndUse(UUID uuid, C object, O obj, Runnable noObject, Consumer<O> objectFound) {
        if (contains(uuid, object)) {
            noObject.run();
        } else {
            add(obj);
            objectFound.accept(obj);
        }
    }

    public OpCommandSuggestion getSuggestions(Function<O, String> function) {
        return new OpCommandSuggestion((sender, args) -> {
            if (sender.isPlayer()) {
                UUID uuid = sender.getPlayer().getUniqueId();
                return ((OpDatabaseImpl<O, C>)getFixedInstance())
                        .getList(uuid)
                        .stream().map(function)
                        .toList();
            }
            return Collections.singletonList("");
        });
    }

    @Override
    public void add(UUID uuid, O object) {
        add(object);
    }

    private void registerDisablement() {
        OpAutoDisable.register((plugin) -> database.close());
    }
}
