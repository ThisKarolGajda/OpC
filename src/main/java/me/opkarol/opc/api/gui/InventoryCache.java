package me.opkarol.opc.api.gui;

import me.opkarol.opc.api.gui.events.InventoryListener;
import me.opkarol.opc.api.gui.replacement.ReplacementInventoryImpl;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.utils.VariableUtil;

import java.util.*;

public class InventoryCache {
    private final OpMap<UUID, ReplacementInventoryImpl> activeInventories = new OpMap<>();
    private final Set<UUID> inventoriesOpened = new HashSet<>();

    public InventoryCache() {
        new InventoryListener(this);
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
        return Arrays.toString(inventoriesOpened.toArray()) + " ::: " + VariableUtil.stringValueOfMap(activeInventories);
    }
}
