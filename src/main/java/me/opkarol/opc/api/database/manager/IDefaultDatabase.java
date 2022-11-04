package me.opkarol.opc.api.database.manager;

import me.opkarol.opc.api.commands.suggestions.OpCommandSuggestion;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface IDefaultDatabase<O, C> {

    Predicate<O> getPredicate(C object);

    boolean delete(UUID uuid, C object);

    boolean contains(UUID uuid, C object);

    Optional<O> get(UUID uuid, C object);

    void add(UUID uuid, O object);

    List<O> getList(UUID uuid);

    default void checkAndUse(UUID uuid, C object, Runnable noObject, Consumer<O> objectFound) {
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

    default void deleteAndUse(UUID uuid, C object, Runnable noObject, Runnable objectFound) {
        if (delete(uuid, object)) {
            objectFound.run();
        } else {
            noObject.run();
        }
    }

    default void addAndUse(UUID uuid, C object, O obj, Runnable noObject, Consumer<O> objectFound) {
        if (contains(uuid, object)) {
            noObject.run();
        } else {
            add(uuid, obj);
            objectFound.accept(obj);
        }
    }

    default OpCommandSuggestion getSuggestions(Function<O, String> function) {
        return new OpCommandSuggestion((sender, args) -> {
            if (sender.isPlayer()) {
                UUID uuid = sender.getPlayer().getUniqueId();
                return getList(uuid)
                        .stream().map(function)
                        .toList();
            }
            return Collections.singletonList("");
        });
    }
}
