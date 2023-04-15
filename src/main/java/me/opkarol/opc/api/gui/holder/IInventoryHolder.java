package me.opkarol.opc.api.gui.holder;

import me.opkarol.opc.api.gui.OpInventory;
import me.opkarol.opc.api.gui.items.InventoryItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

public interface IInventoryHolder {
    OpInventory getInventory();

    static @NotNull InventoryItem getBlankItem() {
        InventoryItem item = new InventoryItem(Material.BLACK_STAINED_GLASS_PANE, e -> e.setCancelled(true));
        item.setName("&k");
        item.setFlags(ItemFlag.HIDE_ATTRIBUTES);
        return item;
    }

    default void registerToInventoriesHolder() {
        InventoriesHolder.add(getClass().getSimpleName(), this);
    }
}
