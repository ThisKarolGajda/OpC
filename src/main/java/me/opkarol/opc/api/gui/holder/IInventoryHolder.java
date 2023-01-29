package me.opkarol.opc.api.gui.holder;

import me.opkarol.opc.api.gui.OpInventory;
import me.opkarol.opc.api.gui.items.InventoryItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

public interface IInventoryHolder {
    OpInventory getInventory();

    default InventoryItem getBlankItem() {
        return new InventoryItem(Material.BLACK_STAINED_GLASS_PANE, e -> e.setCancelled(true))
                .name("&k")
                .flags(ItemFlag.HIDE_ATTRIBUTES);
    }
}
