package me.opkarol.opc.api.gui.holder.item;

import me.opkarol.opc.api.gui.events.OnItemClicked;

public abstract class InventoryItemEventHolder implements Cloneable {

    public abstract void interact(OnItemClicked event);

    public abstract InventoryItemEventHolder clone();
}
