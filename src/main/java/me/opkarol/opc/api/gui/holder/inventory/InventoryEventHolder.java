package me.opkarol.opc.api.gui.holder.inventory;

import me.opkarol.opc.api.gui.events.chest.OnInventoryClose;

public abstract class InventoryEventHolder {

    public abstract void close(OnInventoryClose event);
}
