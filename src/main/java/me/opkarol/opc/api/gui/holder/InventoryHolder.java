package me.opkarol.opc.api.gui.holder;

import me.opkarol.opc.api.gui.OpInventory;
import me.opkarol.opc.api.gui.inventory.InventoryFactory;

public abstract class InventoryHolder implements IInventoryHolder {
    private final OpInventory inventory;

    public InventoryHolder(int inventorySize, String title) {
        inventory = new OpInventory(new InventoryFactory(inventorySize, title));
        setupInventory(inventory);
        registerToInventoriesHolder();
    }

    public abstract void setupInventory(OpInventory inventory);

    @Override
    public OpInventory getInventory() {
        return inventory;
    }
}
