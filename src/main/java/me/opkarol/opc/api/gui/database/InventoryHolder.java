package me.opkarol.opc.api.gui.database;

import me.opkarol.opc.api.gui.inventory.AbstractInventory;
import me.opkarol.opc.api.map.OpMap;
import org.bukkit.inventory.Inventory;

public class InventoryHolder<S> {
    private final AbstractInventory<Integer, S> inventory;
    private OpMap<Integer, Inventory> inventoriesBuilt = new OpMap<>();
    private boolean autoBuild = true;

    public InventoryHolder(AbstractInventory<Integer, S> inventory) {
        this.inventory = inventory;
    }

    public Inventory build(int page) {
        Inventory inventory1 = inventory.generate(page);
        inventoriesBuilt.set(page, inventory1);
        return inventory1;
    }

    public void clearBuild(int page) {
        inventoriesBuilt.set(page, null);
    }

    public boolean isBuilt(int page) {
        return inventoriesBuilt.containsKey(page);
    }

    public OpMap<Integer, Inventory> getInventoriesBuilt() {
        return inventoriesBuilt;
    }

    public void setInventoriesBuilt(OpMap<Integer, Inventory> inventoriesBuilt) {
        this.inventoriesBuilt = inventoriesBuilt;
    }

    public void addInventoryBuilt(int page, Inventory inventory) {
        this.inventoriesBuilt.set(page, inventory);
    }

    public AbstractInventory<Integer, S> getInventory() {
        return inventory;
    }

    public boolean isAutoBuild() {
        return autoBuild;
    }

    public void setAutoBuild(boolean autoBuild) {
        this.autoBuild = autoBuild;
    }
}
