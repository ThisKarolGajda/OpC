package me.opkarol.opc.api.gui.holder;

import me.opkarol.opc.api.gui.OpInventory;
import me.opkarol.opc.api.gui.events.OnInventoryClose;
import me.opkarol.opc.api.gui.holder.inventory.InventoryEventHolder;
import me.opkarol.opc.api.gui.inventory.InventoryFactory;

import java.util.function.Consumer;

public abstract class InventoryExtender implements IInventoryHolder {
    private final OpInventory inventory;

    public InventoryExtender(int inventorySize, String title) {
        inventory = new OpInventory(new InventoryFactory(inventorySize, title));
        setupInventory(inventory);
        registerToInventoriesHolder();
    }

    public InventoryExtender(int inventorySize, String title, InventoryEventHolder action) {
        inventory = new OpInventory(new InventoryFactory(inventorySize, title), action);
        setupInventory(inventory);
        registerToInventoriesHolder();
    }

    public InventoryExtender(int inventorySize, String title, Consumer<OnInventoryClose> consumer) {
        inventory = new OpInventory(new InventoryFactory(inventorySize, title), consumer);
        setupInventory(inventory);
        registerToInventoriesHolder();
    }

    public abstract void setupInventory(OpInventory inventory);

    @Override
    public OpInventory getInventory() {
        return inventory;
    }
}
