package objectinventory;

import me.opkarol.opc.api.gui.InventoryCache;
import me.opkarol.opc.api.gui.OpInventory;
import me.opkarol.opc.api.gui.inventory.PagedInventoryFactory;
import me.opkarol.opc.api.gui.items.InventoryItem;
import me.opkarol.opc.api.gui.items.InventoryItemSpecialData;
import me.opkarol.opc.api.misc.Tuple;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PagedTestInventory {
    public void openInventory(Player player, @NotNull List<TestObject> objects) {
        PagedInventoryFactory factory = new PagedInventoryFactory(InventoryCache.getCache(), 27, "&bOBJECTS!!");

        OpInventory inventory = new OpInventory(factory, e -> e.getPlayer().sendMessage("You have closed inventory!"));
        inventory.setAutoBuild(false);

        InventoryItem barrier = new InventoryItem(Material.BLACK_STAINED_GLASS_PANE, e -> e.setCancelled(true))
                .name(ChatColor.RESET + "")
                .enchantments(Tuple.of(Enchantment.OXYGEN, 1))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

        InventoryItem nextPageItem = new InventoryItem(Material.BARRIER)
                .name("&bNext page");
        nextPageItem.addSpecialData(InventoryItemSpecialData.PAGED_INVENTORY_BUTTON_NEXT);

        InventoryItem previousPageItem = new InventoryItem(Material.BARRIER)
                .name("&bPrevious page");
        previousPageItem.addSpecialData(InventoryItemSpecialData.PAGED_INVENTORY_BUTTON_PREVIOUS);

        inventory.setGlobalItem(nextPageItem, 26);
        inventory.setGlobalItem(previousPageItem, 18);
        inventory.setGlobalItems(barrier, 19, 20, 21, 22, 23, 24, 25);

        inventory.setInventoryObjects(objects);

        inventory.openBestInventory(player);
    }
}
