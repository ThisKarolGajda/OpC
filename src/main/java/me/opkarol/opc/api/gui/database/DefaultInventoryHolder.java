package me.opkarol.opc.api.gui.database;

import me.opkarol.opc.api.gui.inventory.AbstractInventory;
import me.opkarol.opc.api.gui.items.InventoryItem;

public class DefaultInventoryHolder extends InventoryHolder<InventoryItem> {

    public DefaultInventoryHolder(AbstractInventory<Integer, InventoryItem> inventory) {
        super(inventory);
    }
}
