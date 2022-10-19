package me.opkarol.opc.api.database.flat;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.autostart.OpAutoDisable;
import me.opkarol.opc.api.database.manager.IDefaultDatabase;
import me.opkarol.opc.api.map.OpMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class DefaultFlatDatabase<O> extends FlatDatabase<OpMap<UUID, List<O>>> implements IDefaultDatabase<O> {
    private final OpMap<UUID, List<O>> map;

    public DefaultFlatDatabase(String fileName) {
        super(OpAPI.getInstance(), fileName);
        map = loadObject();
        OpAutoDisable.register(plugin -> saveObject(map));
    }

    public List<O> get(UUID uuid) {
        return map.getOrDefault(uuid, new ArrayList<>());
    }

    @Override
    public boolean delete(UUID uuid, Object object) {
        List<O> list = get(uuid);
        boolean b = list.removeIf(getPredicate(object));
        map.set(uuid, list);
        return b;
    }

    @Override
    public boolean contains(UUID uuid, Object object) {
        return get(uuid, object).isPresent();
    }

    @Override
    public Optional<O> get(UUID uuid, Object object) {
        return get(uuid).stream().filter(getPredicate(object)).findAny();
    }
}
