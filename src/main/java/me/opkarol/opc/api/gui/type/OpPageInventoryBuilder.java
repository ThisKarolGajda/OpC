package me.opkarol.opc.api.gui.type;

import me.opkarol.opc.api.files.Configuration;
import me.opkarol.opc.api.gui.misc.OpInventoryCache;
import me.opkarol.opc.api.gui.misc.OpInventoryObject;
import me.opkarol.opc.api.gui.misc.OpInventoryRows;
import me.opkarol.opc.api.gui.misc.OpInventorySpecialData;
import me.opkarol.opc.api.item.OpInventoryItem;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.utils.StringUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class OpPageInventoryBuilder extends OpInventoryBuilder {
    private int currentPage;
    private int maxItemsPerPage;

    private final OpMap<Integer, Inventory> lastInventories = new OpMap<>();
    private final OpMap<Integer, OpInventoryItem> finalItems = new OpMap<>();

    private List<OpMap<Integer, OpInventoryItem>> pages = new ArrayList<>();

    public OpPageInventoryBuilder(String title, OpInventoryRows rows) {
        super(title, rows);
    }

    public OpPageInventoryBuilder setItems(@NotNull List<OpInventoryItem> items) {
        List<OpMap<Integer, OpInventoryItem>> pages = new ArrayList<>();
        int iPage = 0;
        int i = 0;
        OpMap<Integer, OpInventoryItem> map = new OpMap<>();

        for (OpInventoryItem item : items) {
            if (i >= getMaxItemsPerPage()) {
                pages.add(iPage, map);
                iPage++;
                i = 0;
                map = new OpMap<>();
            }

            map.set(i, item);
            i++;
        }

        pages.add(iPage, map);

        this.pages = pages;
        return this;
    }

    public OpInventoryBuilder saveInventory(@NotNull Configuration configuration, String path) {
        FileConfiguration config = configuration.getConfig();
        path = (path.endsWith(".") ? path : path.concat(".")).concat(getInventoryName() + ".");
        config.set(path + "title", getTitle());
        config.set(path + "rows", StringUtil.getInt(getRows().toString()));
        config.set(path + "closeable", ((Consumer<InventoryCloseEvent>) e -> addPlayer((Player) e.getPlayer())).equals(getCloseEventConsumer()));
        for (int i : finalItems.keySet()) {
            OpInventoryItem item = finalItems.getOrDefault(i, null);
            String iPath = path.concat(".items." + item.getItem().getDisplayName() + ".");
            if (item.getItem().getDisplayName() == null || item.getItem().getDisplayName().length() == 0) {
                continue;
            }
            if (config.contains(iPath + "material")) {
                config.set(iPath + "slot", getConfigSlot(config.getString(iPath + "slot"), i));
            } else {
                config.set(iPath + "amount", item.getItem().getAmount());
                config.set(iPath + "material", item.getItem().getMaterial().toString());
                config.set(iPath + "slot", i);
                config.set(iPath + "lore", item.getItem().getLore());
                config.set(iPath + "enchantments", item.getItem().getConfigEnchantments());
                config.set(iPath + "flags", item.getItem().getConfigFlags());
                config.set(iPath + "specialData", item.getConfigSpecialData());
            }
        }
        configuration.save();
        return this;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public OpPageInventoryBuilder nextPage(Player player) {
        if (currentPage + 1 >= getPages().size()) {
            return this;
        }
        currentPage++;
        openPage(player, currentPage);
        return this;
    }

    public OpPageInventoryBuilder previousPage(Player player) {
        if (currentPage - 1 < 0) {
            return this;
        }
        currentPage--;
        openPage(player, currentPage);
        return this;
    }

    public boolean hasPreviousPage() {
        return currentPage - 1 > 0;
    }

    public List<OpMap<Integer, OpInventoryItem>> getPages() {
        return pages;
    }

    public OpMap<Integer, OpInventoryItem> getItemsFromPage(int page) {
        if (page - 1 > pages.size()) {
            return new OpMap<>();
        }
        OpMap<Integer, OpInventoryItem> map = pages.get(page);
        for (int slot : finalItems.keySet()) {
            map.set(slot, finalItems.getOrDefault(slot, null));
        }
        return map;
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }

    public OpPageInventoryBuilder setMaxItemsPerPage(int maxItemsPerPage) {
        if (getSize() - 1 > maxItemsPerPage) {
            this.maxItemsPerPage = maxItemsPerPage;
        }
        return this;
    }

    public OpPageInventoryBuilder openPage(@NotNull Player player, int page) {
        currentPage = page;
        player.openInventory(getLastInventory(page));
        OpInventoryCache.getCache().setLastInventory(player.getUniqueId(), new OpInventoryObject(this));
        return this;
    }

    public Inventory build(int page) {
        Inventory inventory = super.build();
        OpMap<Integer, OpInventoryItem> map = getItemsFromPage(page);
        for (int slot : map.keySet()) {
            OpInventoryItem item = map.getOrDefault(slot, null);
            if (item == null) {
                continue;
            }

            inventory.setItem(slot, item.getItemStack());
        }
        return inventory;
    }

    @Override
    public OpPageInventoryBuilder addPlayer(Player player) {
        return openPage(player, 0);
    }

    public Inventory getLastInventory(int page) {
        return lastInventories.getByKey(page).orElse(build(page));
    }

    public OpPageInventoryBuilder setLastInventory(Inventory inventory, int page) {
        lastInventories.set(page, inventory);
        return this;
    }

    public OpPageInventoryBuilder setItem(Material material, int page, int... slots) {
        return setItem(new OpInventoryItem(material), page, slots);
    }

    public OpPageInventoryBuilder setBlockItem(Material material, int page, int... slots) {
        OpInventoryItem item = new OpInventoryItem(material)
                .setClickAction(e -> e.setCancelled(true))
                .setDragAction(e -> e.setCancelled(true));
        return setItem(item, page, slots);
    }

    public OpPageInventoryBuilder setItem(OpInventoryItem item, int page, int... slots) {
        if (slots == null || slots.length == 0) {
            return this;
        }
        for (int slot : slots) {
            pages.get(page).set(slot, item);
        }
        return this;
    }

    public OpPageInventoryBuilder setBlockItem(@NotNull OpInventoryItem item, int page, int... slots) {
        item.setClickAction(e -> e.setCancelled(true))
                .setDragAction(e -> e.setCancelled(true));
        return setItem(item, page, slots);
    }

    public OpPageInventoryBuilder setFinalItem(@NotNull OpInventoryItem item, int @NotNull ... slots) {
        if (item.hasSpecialData(OpInventorySpecialData.PAGED_INVENTORY_PREVIOUS_PAGE)) {
            item.setClickAction(e -> {
                e.setCancelled(true);
                previousPage((Player) e.getWhoClicked());
            });
        }
        if (item.hasSpecialData(OpInventorySpecialData.PAGED_INVENTORY_NEXT_PAGE)) {
            item.setClickAction(e -> {
                e.setCancelled(true);
                nextPage((Player) e.getWhoClicked());
            });
        }
        for (int slot : slots) {
            finalItems.set(slot, item);
        }
        return this;
    }

    public OpPageInventoryBuilder setFinalItem(Material material, int @NotNull ... slots) {
        return setFinalItem(new OpInventoryItem(material), slots);
    }

    public Optional<OpInventoryItem> getItem(int page, int slot) {
        return getItemsFromPage(page).getByKey(slot);
    }

    public Optional<OpInventoryItem> getItem(int slot) {
        return getItem(currentPage, slot);
    }

    public OpPageInventoryBuilder setFinalBlockItem(@NotNull OpInventoryItem item, int... slots) {
        item.setClickAction(e -> e.setCancelled(true))
                .setDragAction(e -> e.setCancelled(true));
        return setFinalItem(item, slots);
    }

    public OpPageInventoryBuilder setFinalBlockItem(@NotNull Material material, int... slots) {
        OpInventoryItem item = new OpInventoryItem(material);
        item.setClickAction(e -> e.setCancelled(true))
                .setDragAction(e -> e.setCancelled(true));
        return setFinalItem(item, slots);
    }
}