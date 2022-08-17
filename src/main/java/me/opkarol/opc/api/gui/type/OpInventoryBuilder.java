package me.opkarol.opc.api.gui.type;

import me.opkarol.opc.api.files.Configuration;
import me.opkarol.opc.api.gui.misc.*;
import me.opkarol.opc.api.gui.pattern.OpInventoryPattern;
import me.opkarol.opc.api.item.OpInventoryItem;
import me.opkarol.opc.api.item.OpItemBuilder;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.utils.FormatUtils;
import me.opkarol.opc.api.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class OpInventoryBuilder {
    private String inventoryName;
    private OpInventoryRows rows;
    private String title;

    private Consumer<InventoryClickEvent> clickEventConsumer = e -> {};
    private Consumer<InventoryCloseEvent> closeEventConsumer = e -> {};
    private Consumer<InventoryDragEvent> dragEventConsumer = e -> {};

    private Inventory lastInventory;

    private final OpMap<Integer, OpInventoryItem> items = new OpMap<>();

    public OpInventoryBuilder(String title, OpInventoryRows rows) {
        this.title = title;
        this.rows = rows;
        this.inventoryName = FormatUtils.scrapMessage(title);
    }

    public OpInventoryBuilder(String inventoryName, String title, OpInventoryRows rows) {
        this.title = title;
        this.rows = rows;
        this.inventoryName = inventoryName;
    }

    public OpInventoryRows getRows() {
        return rows;
    }

    public int getSize() {
        return getRows().getSize();
    }

    public OpInventoryBuilder setRows(OpInventoryRows rows) {
        this.rows = rows;
        return this;
    }

    public String getTitle() {
        return FormatUtils.formatMessage(title);
    }

    public OpInventoryBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public Inventory build() {
        Inventory inventory = Bukkit.createInventory((IHolder) () -> inventoryName, getSize(), getTitle());
        for (int slot : items.keySet()) {
            if (slot >= getSize()) {
                continue;
            }
            ItemStack item = items.getMap().get(slot).getItemStack();
            inventory.setItem(slot, item);
        }
        setLastInventory(inventory);
        return inventory;
    }

    public Consumer<InventoryClickEvent> getClickEventConsumer() {
        return clickEventConsumer;
    }

    public OpInventoryBuilder setClickEvent(Consumer<InventoryClickEvent> clickEventConsumer) {
        this.clickEventConsumer = clickEventConsumer;
        return this;
    }

    public Consumer<InventoryCloseEvent> getCloseEventConsumer() {
        return closeEventConsumer;
    }

    public OpInventoryBuilder setCloseEvent(Consumer<InventoryCloseEvent> closeEventConsumer) {
        this.closeEventConsumer = closeEventConsumer;
        return this;
    }

    public Consumer<InventoryDragEvent> getDragEventConsumer() {
        return dragEventConsumer;
    }

    public OpInventoryBuilder setDragEvent(Consumer<InventoryDragEvent> dragEventConsumer) {
        this.dragEventConsumer = dragEventConsumer;
        return this;
    }

    public OpInventoryBuilder setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
        return this;
    }

    public OpInventoryBuilder addPlayer(Player player) {
        if (player == null) {
            return this;
        }
        UUID uuid = player.getUniqueId();
        OpInventoryCache.getCache().setLastInventory(uuid, new OpInventoryObject(this));
        player.openInventory(getLastInventory());
        return this;
    }

    public OpInventoryBuilder addPlayers(@NotNull List<Player> players) {
        players.forEach(this::addPlayer);
        return this;
    }

    public Inventory getLastInventory() {
        return lastInventory == null ? build() : lastInventory;
    }

    public OpInventoryBuilder setLastInventory(Inventory lastInventory) {
        this.lastInventory = lastInventory;
        return this;
    }

    public OpInventoryBuilder setNoCloseable() {
        closeEventConsumer = e -> addPlayer((Player) e.getPlayer());
        return this;
    }

    public Optional<OpInventoryItem> getItem(int slot) {
        return items.getByKey(slot);
    }

    public OpInventoryBuilder setItem(OpInventoryItem item, int... slots) {
        if (slots == null || slots.length == 0) {
            return this;
        }
        for (int slot : slots) {
            items.set(slot, item);
        }
        return this;
    }

    public OpInventoryBuilder setItem(Material material, int... slots) {
        return setItem(new OpInventoryItem(material), slots);
    }

    public OpInventoryBuilder setBlockItem(Material material, int... slots) {
        OpInventoryItem item = new OpInventoryItem(material)
                .setClickAction(e -> e.setCancelled(true))
                .setDragAction(e -> e.setCancelled(true));
        return setItem(item, slots);
    }

    public OpInventoryBuilder setBlockItem(@NotNull OpInventoryItem item, int... slots) {
        item.setClickAction(e -> e.setCancelled(true))
                .setDragAction(e -> e.setCancelled(true));
        return setItem(item, slots);
    }

    public OpInventoryBuilder setPattern(@NotNull OpInventoryPattern pattern) {
        String[] strings = pattern.getSplitPattern();
        int i = 0;
        for (String key : strings) {
            setItem(pattern.getItem(key, Material.AIR), i);
            i++;
        }
        return this;
    }

    public OpInventoryBuilder getInventory(@NotNull Configuration configuration, String path) {
        path = (path.endsWith(".") ? path : path.concat("."));
        FileConfiguration config = configuration.getConfig();
        String title = config.getString(path + "title");
        if (title != null) {
            this.title = title;
        }
        String rows = "ROWS_" + config.getInt(path + "rows");
        this.rows = OpInventoryRows.valueOf(rows);
        boolean closeable = config.getBoolean(path + "closeable");
        if (!closeable) {
            setNoCloseable();
        }
        String finalPath = path;
        OpMap<Integer, OpInventoryItem> items = new OpMap<>();
        configuration.useSectionKeys(path, strings -> {
            for (String displayName : strings) {
                String iPath = finalPath.concat(displayName + ".");
                int amount = StringUtil.getIntFromString(config.getString(iPath + "amount"));
                Material material = StringUtil.getMaterialFromString(config.getString(iPath + "material"));
                String slot = config.getString(iPath + "slot");
                List<Integer> slots = new ArrayList<>();
                if (slot != null) {
                    if (slot.contains(";")) {
                        slots.addAll(Arrays.stream(slot.split(";")).map(StringUtil::getInt).collect(Collectors.toList()));
                    } else {
                        slots.add(StringUtil.getIntFromString(slot));
                    }
                }
                List<String> lore = config.getStringList(iPath + "lore");
                List<String> enchantmentsString = config.getStringList(iPath + "enchantments");
                List<String> flagsString = config.getStringList(iPath + "flags");
                List<String> specialDataString = config.getStringList(iPath + "specialData");

                OpMap<Enchantment, Integer> enchantments = new OpMap<>();
                for (String s : enchantmentsString) {
                    String[] strings1 = s.split(";");
                    if (strings1.length == 2) {
                        Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(strings1[0]));
                        int level = StringUtil.getIntFromString(strings1[1]);
                        enchantments.set(enchantment, level);
                    }
                }

                HashSet<ItemFlag> flags = flagsString.stream()
                        .filter(s -> StringUtil.getEnumValue(s, ItemFlag.class).isPresent())
                        .map(ItemFlag::valueOf)
                        .collect(Collectors.toCollection(HashSet::new));

                List<OpInventorySpecialData> specialData = specialDataString.stream()
                        .filter(s -> StringUtil.getEnumValue(s, OpInventorySpecialData.class).isPresent())
                        .map(OpInventorySpecialData::valueOf)
                        .collect(Collectors.toList());

                OpInventoryItem item = new OpInventoryItem(new OpItemBuilder(material)
                        .setDisplayName(displayName)
                        .setAmount(amount)
                        .setEnchantments(enchantments)
                        .setFlags(flags)
                        .setLore(lore), specialData);
                slots.forEach(slot1 -> items.set(slot1, item));
            }
        });
        return this;
    }

    public OpInventoryBuilder saveInventory(@NotNull Configuration configuration, String path) {
        FileConfiguration config = configuration.getConfig();
        path = (path.endsWith(".") ? path : path.concat(".")).concat(inventoryName + ".");
        config.set(path + "title", title);
        config.set(path + "rows", StringUtil.getInt(getRows().toString()));
        config.set(path + "closeable", ((Consumer<InventoryCloseEvent>) e -> addPlayer((Player) e.getPlayer())).equals(getCloseEventConsumer()));
        for (int i : items.keySet()) {
            OpInventoryItem item = items.getOrDefault(i, null);
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

    @NotNull
    public String getConfigSlot(String slots, int slot) {
        if (slots == null) {
            return String.valueOf(slot);
        }
        List<String> map = Arrays.stream(slots.split(";")).collect(Collectors.toList());
        String s = String.valueOf(slot);
        if (map.stream().noneMatch(s1 -> s1.equals(s))) {
            map.add(s);
        }
        return String.join(";", map);
    }

    public String getInventoryName() {
        return inventoryName;
    }
}
