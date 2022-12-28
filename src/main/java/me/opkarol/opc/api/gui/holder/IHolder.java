package me.opkarol.opc.api.gui.holder;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface IHolder extends InventoryHolder {
    String getName();

    @Override
    default Inventory getInventory() {
        return null;
    }
}
