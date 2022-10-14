package me.opkarol.opc.api.database.mysql.base;

import me.opkarol.opc.OpC;
import me.opkarol.opc.api.database.mysql.objects.IObjectDatabase;
import me.opkarol.opc.api.map.OpMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class OpMDatabase<O> extends IObjectDatabase<O, Integer> {
    private final OpMSingleDatabase<O> database;
    private final Function<O, Integer> getIdentification;
    private final BiFunction<O, Integer, O> setIdentification;
    private final Function<O, UUID> getUUID;
    private final OpMap<UUID, List<O>> uuidMap = new OpMap<>();
    private final Function<O, Object> defaultSearch;

    public OpMDatabase(OpMSingleDatabase<O> database, Function<O, Integer> getIdentificationFunction, BiConsumer<O, Integer> setIdentification, Function<O, UUID> getUUID, Function<ResultSet, O> getObjectFromSet) {
        this.database = database;
        if (this.database != null) {
            database.create();
        }
        this.getIdentification = getIdentificationFunction;
        this.setIdentification = (o, integer) -> {
            setIdentification.accept(o, integer);
            return o;
        };
        this.getUUID = getUUID;
        this.defaultSearch = o -> null;
        load(getObjectFromSet);
    }

    public OpMDatabase(OpMSingleDatabase<O> database, Function<O, Integer> getIdentificationFunction, BiConsumer<O, Integer> setIdentification, Function<O, UUID> getUUID, Function<ResultSet, O> getObjectFromSet, Function<O, Object> defaultSearch) {
        this.database = database;
        if (this.database != null) {
            database.create();
        }
        this.getIdentification = getIdentificationFunction;
        this.setIdentification = (o, integer) -> {
            setIdentification.accept(o, integer);
            return o;
        };
        this.getUUID = getUUID;
        this.defaultSearch = defaultSearch;
        load(getObjectFromSet);
    }


    public OpMSingleDatabase<O> getDatabase() {
        return database;
    }

    @Override
    public void add(O object) {
        int id = getIdentification.apply(object);
        int fixed = database.insert(object, id);
        if (fixed != id) {
            object = setIdentification.apply(object, fixed);
        }
        getMap().put(fixed, object);
        addObject(object, getUUID.apply(object));
    }

    @Override
    public Optional<O> get(Integer identification) {
        return getMap().getByKey(identification);
    }

    @Override
    public boolean delete(Integer identification) {
        Optional<O> optional = get(identification);
        if (optional.isEmpty()) {
            return false;
        }

        O object = optional.get();
        database.delete(object);
        getMap().remove(identification);
        removeObject(object);
        return true;
    }

    public void load(Function<ResultSet, O> getObjectFromSet) {
        ResultSet set = database.get();
        try {
            while (set.next()) {
                O object = getObjectFromSet.apply(set);
                getMap().put(getIdentification.apply(object), object);
                addObject(object, getUUID.apply(object));
                OpC.getInstance().getLogger().info(object.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private @NotNull List<O> addObject(O object, UUID uuid) {
        List<O> list = uuidMap.getOrDefault(uuid, new ArrayList<>());
        list.add(object);
        uuidMap.set(uuid, list);
        return list;
    }

    private @NotNull List<O> removeObject(O object) {
        UUID uuid = getUUID.apply(object);
        List<O> list = uuidMap.getOrDefault(uuid, new ArrayList<>());
        list.removeIf(o -> getUUID.apply(o).equals(uuid));
        uuidMap.set(uuid, list);
        return list;
    }

    public List<O> getList(UUID uuid) {
        return uuidMap.getOrDefault(uuid, new ArrayList<>());
    }

    public Optional<O> get(UUID uuid, Predicate<O> predicate) {
        return getList(uuid).stream()
                .filter(predicate)
                .findAny();
    }

    public Optional<O> get(UUID uuid, Object object) {
        return get(uuid, o -> o.equals(object));
    }

    public int getId(UUID uuid, Predicate<O> predicate) {
        return getList(uuid).stream()
                .filter(predicate)
                .findAny()
                .map(getIdentification)
                .orElse(-1);
    }

    public int getId(UUID uuid, Object object) {
        return getId(uuid, getSearchPredicate(object));
    }

    public boolean contains(UUID uuid, Predicate<O> predicate) {
        return get(uuid, predicate)
                .isPresent();
    }

    public boolean contains(UUID uuid, Object object) {
        return contains(uuid, getSearchPredicate(object));
    }

    public boolean delete(UUID uuid, Predicate<O> predicate) {
        int id = getId(uuid, predicate);
        if (id != -1) {
            delete(id);
            return true;
        }
        return false;
    }

    public boolean delete(UUID uuid, Object object) {
        return delete(uuid, getSearchPredicate(object));
    }

    @Contract(pure = true)
    private @NotNull Predicate<O> getSearchPredicate(Object object) {
        return o -> Objects.equals(defaultSearch.apply(o), object);
    }
}