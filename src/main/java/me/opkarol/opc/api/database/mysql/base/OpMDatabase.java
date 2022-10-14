package me.opkarol.opc.api.database.mysql.base;

import me.opkarol.opc.OpC;
import me.opkarol.opc.api.database.mysql.objects.IObjectDatabase;
import me.opkarol.opc.api.database.mysql.resultset.OpMResultSet;
import me.opkarol.opc.api.map.OpMap;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class OpMDatabase<O> extends IObjectDatabase<O, Integer> {
    private final OpMSingleDatabase<O> database;
    private final Function<O, Integer> getIdentification;
    private final BiFunction<O, Integer, O> setIdentification;
    private final Function<O, UUID> getUUID;
    private final OpMap<UUID, List<O>> uuidMap = new OpMap<>();

    public OpMDatabase(OpMSingleDatabase<O> database) {
        this.database = database;
        if (this.database != null) {
            database.create();
        }
        this.getIdentification = getIdentification();
        this.setIdentification = (o, integer) -> {
            setIdentification().accept(o, integer);
            return o;
        };
        this.getUUID = getUUID();
        load(getObjectAsResultSet());
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
        removeObject(object, o -> getUUID.apply(o).equals(getUUID.apply(object)));
        return true;
    }

    public boolean delete(Integer identification, Predicate<O> predicate) {
        Optional<O> optional = get(identification);
        if (optional.isEmpty()) {
            return false;
        }

        O object = optional.get();
        database.delete(object);
        getMap().remove(identification);
        removeObject(object, predicate);
        return true;
    }

    public void load(Function<OpMResultSet, O> getObjectFromSet) {
        ResultSet set = database.get();
        try {
            while (set.next()) {
                O object = getObjectFromSet.apply(new OpMResultSet(set));
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

    private @NotNull List<O> removeObject(O object, Predicate<O> predicate) {
        UUID uuid = getUUID.apply(object);
        List<O> list = uuidMap.getOrDefault(uuid, new ArrayList<>());
        list.removeIf(predicate);
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

    public int getId(UUID uuid, Predicate<O> predicate) {
        return getList(uuid).stream()
                .filter(predicate)
                .findAny()
                .map(getIdentification)
                .orElse(-1);
    }

    public boolean contains(UUID uuid, Predicate<O> predicate) {
        return get(uuid, predicate)
                .isPresent();
    }

    public boolean delete(UUID uuid, Predicate<O> predicate) {
        int id = getId(uuid, predicate);
        if (id != -1) {
            delete(id, predicate);
            return true;
        }
        return false;
    }

    public abstract Function<OpMResultSet, O> getObjectAsResultSet();

    public abstract Function<O, Integer> getIdentification();

    public abstract BiConsumer<O, Integer> setIdentification();

    public abstract Function<O, UUID> getUUID();
}