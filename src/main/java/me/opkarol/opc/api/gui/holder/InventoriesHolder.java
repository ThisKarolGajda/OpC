package me.opkarol.opc.api.gui.holder;

import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.utils.VariableUtil;

import java.util.Optional;

public class InventoriesHolder {
    private static InventoriesHolder holder;
    public OpMap<String, IInventoryHolder> map = new OpMap<>();

    public InventoriesHolder() {
        holder = this;
    }

    public void setMap(OpMap<String, IInventoryHolder> map) {
        this.map = map;
    }

    public OpMap<String, IInventoryHolder> getMap() {
        return map;
    }

    public void addInventory(String inventory, IInventoryHolder holder) {
        map.set(inventory, holder);
    }

    public Optional<IInventoryHolder> getInventory(String inventory) {
        return map.getByKey(inventory);
    }

    public static InventoriesHolder getHolder() {
        return VariableUtil.getOrDefault(holder, new InventoriesHolder());
    }

    public static Optional<IInventoryHolder> get(String inventory) {
        return getHolder().getInventory(inventory);
    }

    public static void add(String inventory, IInventoryHolder holder) {
        getHolder().addInventory(inventory, holder);
    }
}