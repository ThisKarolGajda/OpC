package me.opkarol.opc.api.gui.events;

import me.opkarol.opc.api.gui.database.InventoryHolderFactory;
import org.bukkit.entity.HumanEntity;

public class OnInventoryClose extends CancelableEvent {
    private final InventoryHolderFactory inventory;
    private final InventoryCloseReason reason;
    private final HumanEntity player;

    public OnInventoryClose(InventoryHolderFactory inventory,
                            InventoryCloseReason reason,
                            HumanEntity player) {
        this.inventory = inventory;
        this.reason = reason;
        this.player = player;
    }

    public InventoryHolderFactory getInventory() {
        return inventory;
    }

    public InventoryCloseReason getReason() {
        return reason;
    }

    public HumanEntity getPlayer() {
        return player;
    }

    public void close() {
        getInventory().getCache().removeInventoryOpen(getPlayer().getUniqueId());
        getPlayer().closeInventory();
    }
}
