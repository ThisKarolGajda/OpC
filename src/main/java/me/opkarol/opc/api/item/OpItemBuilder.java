package me.opkarol.opc.api.item;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.utils.PDCUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import me.opkarol.opc.api.list.OpList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.opkarol.opc.api.utils.FormatUtils.formatList;
import static me.opkarol.opc.api.utils.FormatUtils.formatMessage;

public class OpItemBuilder {
    private String displayName;
    private int amount = 1;
    private Material material;
    private List<String> lore;
    private OpMap<Enchantment, Integer> enchantments;
    private OpMap<String, String> pdc;
    private HashSet<ItemFlag> flags;

    public OpItemBuilder(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        this.displayName = meta.getDisplayName();
        this.amount = item.getAmount();
        this.material = item.getType();
        this.lore = meta.getLore();
        this.enchantments = getEnchants(meta.getEnchants());
        this.pdc = PDCUtils.getAllValues(item);
        this.flags = new HashSet<>(meta.getItemFlags());
    }

    public OpItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public String getDisplayName() {
        return displayName;
    }

    public OpItemBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public Material getMaterial() {
        return material;
    }

    public OpItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public List<String> getLore() {
        return lore;
    }

    public OpItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public OpMap<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public OpList<String> getConfigEnchantments() {
        OpList<String> list = new OpList<>();
        for (Enchantment enchantment : getEnchantments().keySet()) {
            list.add(enchantment.toString() + ":" + getEnchantments().getOrDefault(enchantment, 0));
        }
        return list;
    }

    public OpItemBuilder setEnchantments(OpMap<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public OpMap<String, String> getPdc() {
        return pdc;
    }

    public OpItemBuilder setPdc(OpMap<String, String> pdc) {
        this.pdc = pdc;
        return this;
    }

    public ItemStack generate() {
        ItemStack item = new ItemStack(material, amount);
        enchantItem(item);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(formatMessage(displayName));
            meta.setLore(formatList(lore));
            setFlags(item);
            item.setItemMeta(meta);
        }
        return applyPdc(item);
    }

    public OpItemBuilder enchantItem(ItemStack item) {
        getEnchantments().keySet()
                .forEach(e -> item.addUnsafeEnchantment(e, getEnchantments().getMap().get(e)));
        return this;
    }

    public OpItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    private ItemStack applyPdc(ItemStack item) {
        if (pdc != null) {
            for (String s : pdc.keySet()) {
                PDCUtils.addNBT(item, new NamespacedKey(OpAPI.getInstance(), s), pdc.getOrDefault(s, null));
            }
        }
        return item;
    }

    public OpItemBuilder setFlags(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            flags.forEach(meta::addItemFlags);
        }
        return this;
    }

    public OpItemBuilder setFlags(HashSet<ItemFlag> flags) {
        this.flags = flags;
        return this;
    }

    private @NotNull OpMap<Enchantment, Integer> getEnchants(@NotNull Map<Enchantment, Integer> map) {
        OpMap<Enchantment, Integer> enchants = new OpMap<>();
        map.forEach(enchants::put);
        return enchants;
    }

    public int getAmount() {
        return amount;
    }

    public HashSet<ItemFlag> getFlags() {
        return flags;
    }

    public List<String> getConfigFlags() {
        return flags.stream()
                .map(ItemFlag::toString)
                .collect(Collectors.toList());
    }
}