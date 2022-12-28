package me.opkarol.opc.api.gui.events.chest;

import me.opkarol.opc.api.gui.database.InvHolderImpl;
import me.opkarol.opc.api.gui.events.CancelableEvent;
import org.bukkit.entity.HumanEntity;

public class OnInventoryClose extends CancelableEvent {
    private final InvHolderImpl inventory;
    private final InventoryCloseReason reason;
    private final HumanEntity player;

    public OnInventoryClose(InvHolderImpl inventory,
                            InventoryCloseReason reason,
                            HumanEntity player) {
        this.inventory = inventory;
        this.reason = reason;
        this.player = player;
    }

    public InvHolderImpl getInventory() {
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
