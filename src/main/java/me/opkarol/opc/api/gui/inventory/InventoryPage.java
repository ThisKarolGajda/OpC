package me.opkarol.opc.api.gui.inventory;

import me.opkarol.opc.api.map.OpMap;

import java.util.Optional;

public class InventoryPage<K, V> {
    private final OpMap<K, V> map = new OpMap<>();

    public OpMap<K, V> getMap() {
        return map;
    }

    public void set(K slot, V item) {
        map.set(slot, item);
    }

    public void remove(K slot) {
        map.remove(slot);
    }

    public Optional<V> get(K slot) {
        return map.getByKey(slot);
    }
}
