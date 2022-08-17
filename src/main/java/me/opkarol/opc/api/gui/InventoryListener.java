package me.opkarol.opc.api.gui;

import me.opkarol.opc.api.events.OpEvent;
import me.opkarol.opc.api.gui.misc.IHolder;
import me.opkarol.opc.api.gui.misc.OpInventoryCache;
import me.opkarol.opc.api.gui.misc.OpInventoryObject;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Optional;
import java.util.UUID;

public class InventoryListener {

    public InventoryListener() {
        OpEvent.registerEvent(PlayerItemConsumeEvent.class, e -> {
            InventoryHolder holder = e.getPlayer().getOpenInventory().getTopInventory().getHolder();
            e.setCancelled(holder instanceof IHolder);
        });

        OpEvent.registerEvent(InventoryClickEvent.class, e -> {
            if (e.getAction().equals(InventoryAction.COLLECT_TO_CURSOR) && e.getInventory().getHolder() instanceof IHolder) {
                e.setCancelled(true);
            }
        });

        OpEvent.registerEvent(InventoryClickEvent.class, e -> {
            Inventory inventory = e.getClickedInventory();
            if (inventory != null) {
                if (inventory.getHolder() instanceof IHolder) {
                    UUID uuid = e.getWhoClicked().getUniqueId();
                    OpInventoryCache.getCache().getLastInventory(uuid).ifPresent(builder -> {
                        builder.getBuilder().getClickEventConsumer().accept(e);
                        if (builder.isMainBuilder()) {
                            builder.getBuilder().getItem(e.getSlot()).ifPresent(item -> item.getClickAction().accept(e));
                        } else {
                            builder.getPageBuilder().getItem(e.getSlot()).ifPresent(item -> item.getClickAction().accept(e));
                        }
                    });
                }
            }
        });

        OpEvent.registerEvent(InventoryDragEvent.class, e -> {
            if (e.getInventory().getHolder() instanceof IHolder) {
                UUID uuid = e.getWhoClicked().getUniqueId();
                OpInventoryCache.getCache().getLastInventory(uuid).ifPresent(builder -> {
                    builder.getBuilder().getDragEventConsumer().accept(e);
                    if (builder.isMainBuilder()) {
                        e.getInventorySlots().forEach(slot -> builder.getBuilder().getItem(slot).ifPresent(item ->
                                item.getDragAction().accept(e)));
                    } else {
                        e.getInventorySlots().forEach(slot -> builder.getPageBuilder().getItem(slot).ifPresent(item ->
                                item.getDragAction().accept(e)));
                    }
                });
            }
        });

        OpEvent.registerEvent(InventoryCloseEvent.class, e -> {
            UUID uuid = e.getPlayer().getUniqueId();
            OpInventoryCache cache = OpInventoryCache.getCache();
            Optional<OpInventoryObject> optional = cache.getLastInventory(uuid);
            cache.removeLastInventory(uuid);
            optional.ifPresent(builder -> builder.getBuilder().getCloseEventConsumer().accept(e));
        });
    }
}
