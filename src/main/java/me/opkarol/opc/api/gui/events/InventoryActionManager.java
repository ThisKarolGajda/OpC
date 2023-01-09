package me.opkarol.opc.api.gui.events;

import me.opkarol.opc.api.gui.database.InventoryHolderFactory;
import me.opkarol.opc.api.gui.holder.inventory.InventoryEventHolder;
import me.opkarol.opc.api.gui.replacement.ReplacementInventoryImpl;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public record InventoryActionManager(InventoryHolderFactory holder,
                                     InventoryEventHolder closeAction) {

    public InventoryHolderFactory getHolder() {
        return holder;
    }

    public InventoryEventHolder getCloseAction() {
        return closeAction;
    }

    public void openInventory(HumanEntity player, ReplacementInventoryImpl inventory) {
        openInventory(0, player, inventory);
    }

    public void openInventory(Player player, ReplacementInventoryImpl inventory) {
        openInventory(0, (HumanEntity) player, inventory);
    }

    public void openInventory(int page, Player player, ReplacementInventoryImpl inventory) {
        openInventory(page, (HumanEntity) player, inventory);
    }

    public void openInventory(int page, HumanEntity player, ReplacementInventoryImpl inventory) {
        if (holder.isBuilt(page)) {
            player.openInventory(holder.getBuiltInventory(page));
        } else {
            Inventory inventory1 = holder.getHolder().getInventory().generate(page);
            if (holder.getHolder().isAutoBuild()) {
                holder.addBuiltInventory(page, inventory1);
            }
            player.openInventory(inventory1);
        }
        holder.getCache().setActiveInventory(player.getUniqueId(), inventory);
    }
}
