package me.opkarol.opc.api.database.flat;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.database.manager.IDefaultDatabase;
import me.opkarol.opc.api.database.manager.settings.FlatDatabaseSettings;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.tools.autostart.IDisable;
import me.opkarol.opc.api.tools.autostart.OpAutoDisable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

public class DefaultFlatDatabase<O, C> extends FlatDatabase<OpMap<C, List<O>>> implements IDefaultDatabase<O, C>, IDisable {
    private final OpMap<C, List<O>> map;
    private final FlatDatabaseSettings<O, C> settings;

    public DefaultFlatDatabase(@NotNull FlatDatabaseSettings<O, C> settings) {
        super(OpAPI.getPlugin(), settings.getFileName());
        this.settings = settings;
        OpMap<C, List<O>> map = loadObject();
        this.map = Objects.requireNonNullElseGet(map, OpMap::new);
        OpAutoDisable.add(this);
    }

    @Override
    public Predicate<O> getPredicate(C object) {
        return getSettings().getPredicate(object);
    }

    @Override
    public boolean delete(C object) {
        List<O> list = getList(object);
        boolean b = list.removeIf(getPredicate(object));
        map.set(object, list);
        return b;
    }

    @Override
    public boolean contains(C object) {
        return get(object).isPresent();
    }

    @Override
    public Optional<O> get(C object) {
        return getList(object).stream().filter(getPredicate(object)).findAny();
    }

    @Override
    public void add(C uuid, O object) {
        List<O> list = map.getOrDefault(uuid, new ArrayList<>());
        list.add(object);
        map.set(uuid, list);
    }

    @Override
    public List<O> getList(C uuid) {
        return map.getOrDefault(uuid, new ArrayList<>());
    }

    @Override
    public OpMap<C, List<O>> getObjectsMap() {
        return map;
    }

    public FlatDatabaseSettings<O, C> getSettings() {
        return settings;
    }

    @Override
    public void onDisable() {
        saveObject(this.map);
    }
}
