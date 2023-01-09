package me.opkarol.opc.api.gui.events;

import me.opkarol.opc.api.event.EventRegister;
import me.opkarol.opc.api.gui.InventoryCache;
import me.opkarol.opc.api.gui.database.InventoryHolderFactory;
import me.opkarol.opc.api.gui.database.PagedInventoryHolder;
import me.opkarol.opc.api.gui.holder.IHolder;
import me.opkarol.opc.api.gui.holder.inventory.InventoryEventHolder;
import me.opkarol.opc.api.gui.holder.item.InventoryItemEventHolder;
import me.opkarol.opc.api.gui.items.InventoryItem;
import me.opkarol.opc.api.gui.replacement.ReplacementInventoryImpl;
import me.opkarol.opc.api.tools.runnable.OpRunnable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class InventoryListener {

    public InventoryListener(InventoryCache cache) {
        // Disables minecraft`s glitch where you use and consume item while having open inventory
        EventRegister.registerEvent(PlayerItemConsumeEvent.class, e -> e.setCancelled(e.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof IHolder));

        // Disables inventory bypass where you can collect items which are the same (bypassing normal inventory click event)
        EventRegister.registerEvent(InventoryClickEvent.class, e -> e.setCancelled(e.getAction().equals(InventoryAction.COLLECT_TO_CURSOR) && e.getInventory().getHolder() instanceof IHolder));

        /*
          The first thing this code does is checking if the inventory that was clicked on is not a "holder" using the isNotHolder method.
          If it is not a holder, the code immediately returns and does nothing else.

          Otherwise, the code gets the unique ID of the player who clicked (e.getWhoClicked().getUniqueId()) and the slot of the item that was clicked (e.getSlot()).
          It then looks up the active inventory for that player in a cache using the unique ID, and if it finds one, it stores it in the inventory variable.

          Next, the code checks the type of the inventory holder for the inventory using inventory.getInventoryHolder().getType().
          Depending on the type, it will do different things.

          If the type is DEFAULT, it gets the item in the clicked slot from the default holder's inventory using inventory.getInventoryHolder().getDefaultHolder().getInventory().get(slot),
          and if it is present, it creates an OnItemClicked event and passes it to the interact method of the item's InventoryItemEventHolder.
          If the event is cancelled, it sets the cancellation of the original event (e) to true as well.

          If the type is PAGED, it gets the current page of the paged holder using invHolder.getCurrentPage(),
          and then gets the item in the clicked slot from the current page's inventory using invHolder.getInventory().get(page).flatMap(inventoryPage -> inventoryPage.get(slot)).
          If the item is present, it creates an OnItemClicked event and passes it to the interact method of the item's InventoryItemEventHolder, and sets the cancellation of the original event (e) to true if the event is cancelled.
          It then checks if the item has any data, and if it is a "next" or "previous" button for a paged inventory. If it is a "next" button, it advances to the next page in the paged holder.
          If it is a "previous" button, it goes back to the previous page. In either case, it reopens the inventory for the player using inventory.openInventory(e.getWhoClicked()).
         */
        EventRegister.registerEvent(InventoryClickEvent.class, e -> {
            if (isNotHolder(e.getClickedInventory())) {
                return;
            }

            UUID uuid = e.getWhoClicked().getUniqueId();
            int slot = e.getSlot();
            Optional<ReplacementInventoryImpl> optional = cache.getActiveInventory(uuid);
            if (optional.isEmpty()) {
                return;
            }

            ReplacementInventoryImpl inventory = optional.get();

            switch (inventory.getInventoryHolder().getType()) {
                case DEFAULT -> inventory.getInventoryHolder().getDefaultHolder().getInventory().get(slot).ifPresent(item -> {
                    OnItemClicked event = new OnItemClicked(inventory.getInventoryHolder(), e.getWhoClicked(), slot, item);
                    InventoryItemEventHolder eventHolder = item.getItemEventHolder();
                    if (eventHolder != null) {
                        eventHolder.interact(event);
                    }
                    if (event.isCancelled()) {
                        e.setCancelled(true);
                    }
                });
                case PAGED -> {
                    PagedInventoryHolder invHolder = inventory.getInventoryHolder().getPagedHolder();
                    int page = invHolder.getCurrentPage();
                    invHolder.getInventory().get(page)
                            .flatMap(inventoryPage -> inventoryPage.get(slot))
                            .ifPresent(item -> handlePagedItemClick(inventory, e, slot, item));
                }
            }
        });

        EventRegister.registerEvent(InventoryCloseEvent.class, e -> {
            // Delay inventory close, so if there is a new one that will be opened, they won't be bugged out together
            new OpRunnable((opRunnable) -> {
                HumanEntity player = e.getPlayer();

                // Avoid spam when player is dead
                if (player.isDead() || isNotHolder(e.getInventory())) {
                    return;
                }

                // Get the player's UUID
                UUID uuid = player.getUniqueId();

                // Check if there is an active inventory for the player in the cache
                cache.getActiveInventory(uuid).ifPresent(inventory -> {
                    if (inventory.getInventoryHolder().getType().equals(InventoryHolderFactory.INVENTORY_HOLDER_TYPE.PAGED)) {
                        return;
                    }

                    // Create an OnInventoryClose event with information about the inventory,
                    // the reason for closing, and the player who closed it
                    InventoryCloseReason closeReason = cache.hasSavedInventory(uuid) ? InventoryCloseReason.PLAYER : InventoryCloseReason.NOT_PLAYER;
                    OnInventoryClose event = new OnInventoryClose(inventory.getInventoryHolder(), closeReason, e.getPlayer());

                    // Get the action to be taken when the inventory is closed
                    InventoryEventHolder eventHolder = inventory.getInventoryHolder().getActionManager().getCloseAction();
                    if (eventHolder != null) {
                        // Perform the action
                        eventHolder.close(event);
                    }

                    // Remove the active inventory from the cache and mark that the player's inventory is no longer open
                    cache.removeActiveInventory(uuid);
                    cache.removeInventoryOpen(uuid);

                    // If the event was cancelled, reopen the inventory for the player
                    if (event.isCancelled()) {
                        inventory.openInventory(player);
                    }
                });
            }).runTaskLater(1/20L);
        });
    }

    private boolean isNotHolder(Inventory inventory) {
        if (inventory == null) {
            return false;
        }
        return !(inventory.getHolder() instanceof IHolder);
    }

    private void handlePagedItemClick(@NotNull ReplacementInventoryImpl inventory, @NotNull InventoryClickEvent e, int slot, @NotNull InventoryItem item) {
        OnItemClicked event = new OnItemClicked(inventory.getInventoryHolder(), e.getWhoClicked(), slot, item);
        InventoryItemEventHolder eventHolder = item.getItemEventHolder();
        if (eventHolder != null) {
            eventHolder.interact(event);
        }
        if (event.isCancelled()) {
            e.setCancelled(true);
        }
        if (!item.hasAnyData()) {
            return;
        }
        if (item.isPagedInventoryButtonNext()) {
            inventory.getInventoryHolder().getPagedHolder().nextPage();
        }
        else if (item.isPagedInventoryButtonPrevious()) {
            inventory.getInventoryHolder().getPagedHolder().previousPage();
        }
        inventory.openBestInventory(e.getWhoClicked());
    }
}
