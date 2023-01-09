package me.opkarol.opc.api.database.flat;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.tools.autostart.OpAutoDisable;
import me.opkarol.opc.api.database.manager.IDefaultDatabase;
import me.opkarol.opc.api.database.manager.settings.FlatDatabaseSettings;
import me.opkarol.opc.api.map.OpMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

public class DefaultFlatDatabase<O, C> extends FlatDatabase<OpMap<UUID, List<O>>> implements IDefaultDatabase<O, C> {
    private final OpMap<UUID, List<O>> map;
    private final FlatDatabaseSettings<O, C> settings;

    public DefaultFlatDatabase(@NotNull FlatDatabaseSettings<O, C> settings) {
        super(OpAPI.getInstance(), settings.getFileName());
        this.settings = settings;
        OpMap<UUID, List<O>> map = loadObject();
        this.map = Objects.requireNonNullElseGet(map, OpMap::new);
        OpAutoDisable.register(plugin -> saveObject(this.map));
    }

    @Override
    public Predicate<O> getPredicate(C object) {
        return getSettings().getPredicate(object);
    }

    @Override
    public boolean delete(UUID uuid, C object) {
        List<O> list = getList(uuid);
        boolean b = list.removeIf(getPredicate(object));
        map.set(uuid, list);
        return b;
    }

    @Override
    public boolean contains(UUID uuid, C object) {
        return get(uuid, object).isPresent();
    }

    @Override
    public Optional<O> get(UUID uuid, C object) {
        return getList(uuid).stream().filter(getPredicate(object)).findAny();
    }

    @Override
    public void add(UUID uuid, O object) {
         List<O> list = map.getOrDefault(uuid, new ArrayList<>());
         list.add(object);
         map.set(uuid, list);
    }

    @Override
    public List<O> getList(UUID uuid) {
        return map.getOrDefault(uuid, new ArrayList<>());
    }

    public FlatDatabaseSettings<O, C> getSettings() {
        return settings;
    }
}
