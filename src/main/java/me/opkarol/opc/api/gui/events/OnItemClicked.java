package me.opkarol.opc.api.gui.events;

import me.opkarol.opc.api.gui.database.InventoryHolderFactory;
import me.opkarol.opc.api.gui.items.InventoryItem;
import org.bukkit.entity.HumanEntity;

public final class OnItemClicked extends CancelableEvent {
    private final InventoryHolderFactory inventory;
    private final HumanEntity player;
    private final int slot;
    private final InventoryItem item;

    public OnItemClicked(InventoryHolderFactory inventory,
                         HumanEntity player,
                         int slot, InventoryItem item) {
        this.inventory = inventory;
        this.player = player;
        this.slot = slot;
        this.item = item;
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
        getPlayer().closeInventory();
    }

    public InventoryItem getItem() {
        return item;
    }
}
