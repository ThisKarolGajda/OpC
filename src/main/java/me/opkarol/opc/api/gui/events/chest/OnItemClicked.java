package me.opkarol.opc.api.gui.events.chest;

import me.opkarol.opc.api.gui.database.InvHolderImpl;
import me.opkarol.opc.api.gui.events.CancelableEvent;
import me.opkarol.opc.api.gui.items.InventoryItem;
import org.bukkit.entity.HumanEntity;

public final class OnItemClicked extends CancelableEvent {
    private final InvHolderImpl inventory;
    private final HumanEntity player;
    private final int slot;
    private final InventoryItem item;

    public OnItemClicked(InvHolderImpl inventory,
                         HumanEntity player,
                         int slot, InventoryItem item) {
        this.inventory = inventory;
        this.player = player;
        this.slot = slot;
        this.item = item;
    }

    public InvHolderImpl getInventory() {
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
