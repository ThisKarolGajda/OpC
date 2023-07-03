package me.opkarol.opc.api.gui.events;

import me.opkarol.opc.api.gui.database.InventoryHolderFactory;
import me.opkarol.opc.api.gui.items.InventoryItem;
import me.opkarol.opc.api.tools.runnable.OpRunnable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

public final class OnItemClicked extends CancelableEvent {
    private final InventoryHolderFactory inventory;
    private final HumanEntity player;
    private final int slot;
    private final InventoryItem item;
    private final InventoryClickEvent inventoryClickEvent;

    public OnItemClicked(InventoryHolderFactory inventory,
                         HumanEntity player,
                         int slot, InventoryItem item, InventoryClickEvent inventoryClickEvent) {
        this.inventory = inventory;
        this.player = player;
        this.slot = slot;
        this.item = item;
        this.inventoryClickEvent = inventoryClickEvent;
    }

    public InventoryHolderFactory getInventory() {
        return inventory;
    }

    public HumanEntity getPlayer() {
        return player;
    }

    public int getSlot() {
        return slot;
    }

    public void close() {
        getInventory().getCache().removeInventoryOpen(getPlayer().getUniqueId());
        new OpRunnable(() -> getPlayer().closeInventory()).runTask();
    }

    public InventoryItem getItem() {
        return item;
    }

    public InventoryClickEvent getInventoryClickEvent() {
        return inventoryClickEvent;
    }
}
