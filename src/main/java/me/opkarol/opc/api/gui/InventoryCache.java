package me.opkarol.opc.api.gui;

import me.opkarol.opc.api.gui.events.InventoryListener;
import me.opkarol.opc.api.gui.replacement.ReplacementInventoryImpl;
import me.opkarol.opc.api.map.OpMap;

import java.util.*;

import static me.opkarol.opc.api.utils.VariableUtil.getOrDefault;

public class InventoryCache {
    private static InventoryCache cache;
    private final OpMap<UUID, ReplacementInventoryImpl> activeInventories = new OpMap<>();
    private final Set<UUID> inventoriesOpened = new HashSet<>();

    public InventoryCache() {
        cache = this;
        new InventoryListener(this);
    }

    public static InventoryCache getCache() {
        return getOrDefault(cache, new InventoryCache());
    }

    public void setActiveInventory(UUID uuid, ReplacementInventoryImpl inventory) {
        activeInventories.set(uuid, inventory);
        inventoriesOpened.add(uuid);
    }

    public Optional<ReplacementInventoryImpl> getActiveInventory(UUID uuid) {
        return activeInventories.getByKey(uuid);
    }

    public void removeActiveInventory(UUID uuid) {
        activeInventories.remove(uuid);
    }

    public void removeInventoryOpen(UUID uuid) {
        inventoriesOpened.remove(uuid);
    }

    public boolean hasSavedInventory(UUID uuid) {
        return inventoriesOpened.contains(uuid);
    }

    public String getInventoriesOpened() {
        return Arrays.toString(inventoriesOpened.toArray());
    }
}
