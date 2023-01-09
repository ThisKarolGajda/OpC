package me.opkarol.opc.api.gui.inventory;

import me.opkarol.opc.api.gui.database.InventoryHolderFactory;

public interface IInventoryIdentification {
    InventoryHolderFactory.INVENTORY_HOLDER_TYPE getType();
}
