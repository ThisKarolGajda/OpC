package me.opkarol.opc.api.gui.misc;

import me.opkarol.opc.api.gui.InventoryListener;
import me.opkarol.opc.api.map.OpMap;

import java.util.Optional;
import java.util.UUID;

import static me.opkarol.opc.api.utils.Util.getOrDefault;

public class OpInventoryCache {
    private static OpInventoryCache cache;
    private final OpMap<UUID, OpInventoryObject> lastInventories = new OpMap<>();

    public OpInventoryCache() {
        cache = this;
        new InventoryListener();
    }

    public static OpInventoryCache getCache() {
        return getOrDefault(cache, new OpInventoryCache());
    }

    public void setLastInventory(UUID uuid, OpInventoryObject builder) {
        lastInventories.set(uuid, builder);
    }

    public Optional<OpInventoryObject> getLastInventory(UUID uuid) {
        return lastInventories.getByKey(uuid);
    }

    public void removeLastInventory(UUID uuid) {
        lastInventories.remove(uuid);
    }
}
