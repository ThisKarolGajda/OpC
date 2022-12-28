import me.opkarol.opc.api.events.EventRegister;
import me.opkarol.opc.api.gui.OpInventory;
import me.opkarol.opc.api.gui.events.chest.OnItemClicked;
import me.opkarol.opc.api.gui.inventory.InventoryFactory;
import me.opkarol.opc.api.gui.inventory.PagedInventoryFactory;
import me.opkarol.opc.api.gui.items.InventoryItem;
import me.opkarol.opc.api.gui.items.InventoryItemSpecialData;
import me.opkarol.opc.api.gui.replacement.ReplacementInventoryImpl;
import me.opkarol.opc.api.misc.Tuple;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.Arrays;
import java.util.List;

public class TestInventory {
    public void pagedInventoryImpl() {
        PagedInventoryFactory factory = new PagedInventoryFactory(27, "&bHI!");

        InventoryItem item2 = new InventoryItem(Material.STONE, OnItemClicked::close)
                .lore(Arrays.asList("%test1%", "Second line should be the same!!!", "and third should have this %replace% replaced", "%test1%"))
                .name("%That_Is_Amazing% I know!!!");

        InventoryItem nextPageItem = new InventoryItem(Material.BARRIER)
                .name("&bNext page");
        nextPageItem.addSpecialData(InventoryItemSpecialData.PAGED_INVENTORY_BUTTON_NEXT);

        InventoryItem previousPageItem = new InventoryItem(Material.BARRIER)
                .name("&bPrevious page");
        previousPageItem.addSpecialData(InventoryItemSpecialData.PAGED_INVENTORY_BUTTON_PREVIOUS);

        OpInventory inventory1 = new OpInventory(factory, e -> e.getPlayer().sendMessage("CLOSED BECAUSE: " + e.getReason() + " PAGE: " + e.getInventory().getPagedHolder().getCurrentPage()));

        InventoryItem item = new InventoryItem(Material.WRITTEN_BOOK, event -> {
            event.getPlayer().sendMessage("You clicked: " + event.getSlot() + " -- PAGE: " + inventory1.getCurrentPage());
            event.setCancelled(true);
        }).lore(List.of("Current page: %current%"));

        inventory1.set(0, item2, 2);
        inventory1.set(1, item2, 2);
        inventory1.set(2, item2, 2);


        inventory1.setGlobalItem(previousPageItem, 18);
        inventory1.setGlobalItem(nextPageItem, 26);
        inventory1.setGlobalItem(item, 0);

        EventRegister.registerEvent(PlayerChatEvent.class,
                e -> {
                    inventory1.openWithFunction(e.getPlayer(), Tuple.of("%current%", ReplacementInventoryImpl::getCurrentPage));
                    e.getPlayer().sendMessage("PAGE: " + inventory1.getCurrentPage());
                });
    }

    public void defaultInventoryImpl() {
        InventoryFactory inventory = new InventoryFactory(27, "Title!!!");

        InventoryItem item2 = new InventoryItem(Material.STONE, OnItemClicked::close)
                .lore(Arrays.asList("%test1%", "Second line should be the same!!!", "and third should have this %replace% replaced", "%test1%"))
                .name("%That_Is_Amazing% I know!!!");

        InventoryItem nextPageItem = new InventoryItem(Material.BARRIER)
                .name("&bNext page");

        InventoryItem previousPageItem = new InventoryItem(Material.BARRIER)
                .name("&bPrevious page");

        OpInventory inventory1 = new OpInventory(inventory, e -> e.getPlayer().sendMessage("CLOSED BECAUSE: " + e.getReason() + " PAGE: " + e.getInventory().getPagedHolder().getCurrentPage()));

        InventoryItem item = new InventoryItem(Material.WRITTEN_BOOK, event -> {
            event.getPlayer().sendMessage("You clicked: " + event.getSlot() + " -- PAGE: " + inventory1.getCurrentPage());
            event.setCancelled(true);
        }).lore(List.of("Current page: %current%"));

        inventory1.set(0, item2, 2);
        inventory1.set(1, item2, 2);
        inventory1.set(2, item2, 2);

        inventory1.setGlobalItem(item, 0);

        EventRegister.registerEvent(PlayerChatEvent.class,
                e -> {
                    inventory1.openWithFunction(e.getPlayer(), Tuple.of("%current%", ReplacementInventoryImpl::getCurrentPage));
                    e.getPlayer().sendMessage("PAGE: " + inventory1.getCurrentPage());
                });
    }
}
