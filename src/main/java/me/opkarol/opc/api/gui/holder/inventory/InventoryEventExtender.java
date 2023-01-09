package me.opkarol.opc.api.gui.holder.inventory;

import me.opkarol.opc.api.gui.events.OnInventoryClose;

import java.util.function.Consumer;

public class InventoryEventExtender extends InventoryEventHolder {
    private final Consumer<OnInventoryClose> action;

    public InventoryEventExtender(Consumer<OnInventoryClose> action) {
        this.action = action;
    }

    @Override
    public void close(OnInventoryClose event) {
        action.accept(event);
    }
}
