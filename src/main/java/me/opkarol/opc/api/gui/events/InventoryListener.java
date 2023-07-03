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
import org.jetbrains.annotations.Range;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class InventoryListener {

    public InventoryListener(InventoryCache cache) {
        // Disables minecraft`s glitch (maybe it's on purpose) where you use and consume item while having open inventory
        EventRegister.registerEvent(PlayerItemConsumeEvent.class, e -> e.setCancelled(e.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof IHolder));

        // Disables inventory bypass where you can collect items which are the same (bypassing normal inventory click event)
        EventRegister.registerEvent(InventoryClickEvent.class, e -> e.setCancelled(e.getAction().equals(InventoryAction.COLLECT_TO_CURSOR) && e.getInventory().getHolder() instanceof IHolder));

        // Registers a click event for both PAGED and NOT_PAGED (DEFAULT) inventory type
        EventRegister.registerEvent(InventoryClickEvent.class, e -> {

            // Checks if the clicked inventory is not a "holder", if it is not, it returns and does nothing
            if (isNotHolder(e.getClickedInventory())) {
                return;
            }

            UUID uuid = e.getWhoClicked().getUniqueId();
            int slot = e.getSlot();

            // Gets the active inventory for the player from a cache using the UUID
            Optional<ReplacementInventoryImpl> optional = cache.getActiveInventory(uuid);
            if (optional.isEmpty()) {
                return;
            }

            ReplacementInventoryImpl inventory = optional.get();
            // Switches on the type of inventory holder for the clicked inventory
            switch (inventory.getInventoryHolder().getType()) {
                case DEFAULT -> {
                    // If the inventory holder is a DEFAULT type, gets the item in the clicked slot from the default holder's inventory
                    // and if it is present, creates an OnItemClicked event and passes it to the interact method of the item's InventoryItemEventHolder.
                    // If the event is cancelled, sets the cancellation of the original event (e) to true as well.
                    inventory.getInventoryHolder().getDefaultHolder().getInventory().get(slot).ifPresent(item -> {
                        OnItemClicked event = new OnItemClicked(inventory.getInventoryHolder(), e.getWhoClicked(), slot, item, e);
                        InventoryItemEventHolder eventHolder = item.getItemEventHolder();
                        if (eventHolder != null) {
                            eventHolder.interact(event);
                        }
                        if (event.isCancelled()) {
                            e.setCancelled(true);
                        }
                    });
                }
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
            new OpRunnable(() -> {
                HumanEntity player = e.getPlayer();

                // Avoid spam when player is dead
                if (player.isDead() || isNotHolder(e.getInventory())) {
                    return;
                }

                UUID uuid = player.getUniqueId();

                // Check if there is an active inventory for the player in the cache
                cache.getActiveInventory(uuid).ifPresent(inventory -> {
                    // Check if there wasn't inventory changed in between
                    if (!Objects.equals(e.getInventory().getHolder(), inventory.getInventoryHolder().getHolder().getInventory().getInventoryHolder())) {
                        return;
                    }

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
            }).runTaskLater(1 / 20L);
        });
    }

    /**
     * Checks if the clicked inventory is a "holder" inventory, meaning that it was created by the inventory framework
     *
     * @param inventory the clicked inventory
     * @return true if the inventory is a "holder" inventory, false otherwise
     */
    private boolean isNotHolder(Inventory inventory) {
        if (inventory == null) {
            return false;
        }
        return !(inventory.getHolder() instanceof IHolder);
    }

    /**
     * Handles the behavior when an item in a paged inventory is clicked
     * If the inventory holder is a PAGED type, gets the current page of the paged holder
     * and then gets the item in the clicked slot from the current page's inventory.
     * If the item is present, it creates an OnItemClicked event and passes it to the interact method of the item's InventoryItemEventHolder,
     * and sets the cancellation of the original event (e) to true if the event is cancelled.
     * It then checks if the item has any data, and if it is a "next" or "previous" button for a paged inventory.
     * If it is a "next" button, it advances to the next page in the paged holder.
     * If it is a "previous" button, it goes back to the previous page. In either case, it reopens the inventory for the player.
     *
     * @param inventory the ReplacementInventoryImpl instance for the active inventory
     * @param event the InventoryClickEvent that triggered this method
     * @param slot the slot index of the clicked item
     * @param item the ItemStack of the clicked item
     */
    private void handlePagedItemClick(@NotNull ReplacementInventoryImpl inventory, @NotNull InventoryClickEvent event, @Range(from = 0, to = 53) int slot, @NotNull InventoryItem item) {
        OnItemClicked onItemClicked = new OnItemClicked(inventory.getInventoryHolder(), event.getWhoClicked(), slot, item, event);
        InventoryItemEventHolder eventHolder = item.getItemEventHolder();
        if (eventHolder != null) {
            eventHolder.interact(onItemClicked);
        }
        if (onItemClicked.isCancelled()) {
            event.setCancelled(true);
        }
        if (!item.hasAnyData()) {
            return;
        }
        if (item.isPagedInventoryButtonNext()) {
            inventory.getInventoryHolder().getPagedHolder().nextPage();
        } else if (item.isPagedInventoryButtonPrevious()) {
            inventory.getInventoryHolder().getPagedHolder().previousPage();
        }
        inventory.openBestInventory(event.getWhoClicked());
    }
}
