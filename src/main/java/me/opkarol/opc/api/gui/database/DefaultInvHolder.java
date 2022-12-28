package me.opkarol.opc.api.gui.database;

import me.opkarol.opc.api.gui.inventory.AbstractInventory;
import me.opkarol.opc.api.gui.items.InventoryItem;

public class DefaultInvHolder extends InvHolder<InventoryItem> {

    public DefaultInvHolder(AbstractInventory<Integer, InventoryItem> inventory) {
        super(inventory);
    }
}
