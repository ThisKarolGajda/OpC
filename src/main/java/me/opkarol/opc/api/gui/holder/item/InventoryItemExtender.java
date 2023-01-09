package me.opkarol.opc.api.gui.holder.item;

import me.opkarol.opc.api.gui.events.OnItemClicked;

import java.util.function.Consumer;

public class InventoryItemExtender extends InventoryItemEventHolder {
    private final Consumer<OnItemClicked> action;

    public InventoryItemExtender(Consumer<OnItemClicked> action) {
        this.action = action;
    }

    @Override
    public void interact(OnItemClicked event) {
        action.accept(event);
    }
}