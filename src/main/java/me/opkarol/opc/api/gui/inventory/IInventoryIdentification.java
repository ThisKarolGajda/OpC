package me.opkarol.opc.api.gui.inventory;

import me.opkarol.opc.api.gui.database.InvHolderImpl;

public interface IInventoryIdentification {
    InvHolderImpl.INVENTORY_HOLDER_TYPE getType();
}
