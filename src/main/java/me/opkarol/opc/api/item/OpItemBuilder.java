package me.opkarol.opc.api.item;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.configuration.CustomConfiguration;
import me.opkarol.opc.api.configuration.IEmptyConfiguration;
import me.opkarol.opc.api.list.OpList;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.misc.HashCreator;
import me.opkarol.opc.api.misc.Tuple;
import me.opkarol.opc.api.utils.PDCUtils;
import me.opkarol.opc.api.utils.VariableUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static me.opkarol.opc.api.utils.FormatUtils.formatList;
import static me.opkarol.opc.api.utils.FormatUtils.formatMessage;

public class OpItemBuilder<K extends OpItemBuilder<?>> implements IEmptyConfiguration {
    private String displayName;
    private int amount = 1;
    private Material material;
    private List<String> lore;
    private OpMap<Enchantment, Integer> enchantments;
    private OpMap<String, String> pdc;
    private HashSet<ItemFlag> flags;

    private String defaultDisplayName;
    private List<String> defaultLore;

    public OpItemBuilder(ItemStack item) {
        if (item == null) {
            return;
        }
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
        this.defaultDisplayName = this.displayName;
        this.defaultLore = this.lore;
    }

    public OpItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public OpItemBuilder(String path) {
        get(path);
    }

    public String getDisplayName() {
        return VariableUtil.getOrDefault(displayName, getUuid());
    }

    public K name(String displayName) {
        this.displayName = displayName;
        if (defaultDisplayName == null) {
            defaultDisplayName = displayName;
        }
        return (K) this;
    }

    public Material getMaterial() {
        return material;
    }

    public K material(Material material) {
        this.material = material;
        return (K) this;
    }

    public List<String> getLore() {
        return lore;
    }

    public K lore(List<String> lore) {
        this.lore = lore;
        if (defaultLore == null) {
            defaultLore = lore;
        }
        return (K) this;
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

    public K enchantments(OpMap<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return (K) this;
    }

    @SafeVarargs
    public final K enchantments(Tuple<Enchantment, Integer>... enchantments) {
        this.enchantments = VariableUtil.getMapFromTuples(enchantments);
        return (K) this;
    }

    public OpMap<String, String> getPdc() {
        return pdc;
    }

    public K pdc(OpMap<String, String> pdc) {
        this.pdc = pdc;
        return (K) this;
    }

    public ItemStack generate() {
        ItemStack item = new ItemStack(material, amount);
        enchantItem(item);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(formatMessage(displayName));
            meta.setLore(formatList(lore));
            item.setItemMeta(meta);
            item = applyFlags(item);
        }
        return applyPdc(item);
    }

    public K enchantItem(ItemStack item) {
        getEnchantments().keySet()
                .forEach(e -> item.addUnsafeEnchantment(e, getEnchantments().getMap().get(e)));
        return (K) this;
    }

    public K amount(int amount) {
        this.amount = amount;
        return (K) this;
    }

    private ItemStack applyPdc(ItemStack item) {
        if (pdc != null) {
            for (String s : pdc.keySet()) {
                PDCUtils.addNBT(item, new NamespacedKey(OpAPI.getInstance(), s), pdc.getOrDefault(s, null));
            }
        }
        return item;
    }

    public ItemStack applyFlags(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            flags.forEach(meta::addItemFlags);
        }
        item.setItemMeta(meta);
        return item;
    }

    public K flags(HashSet<ItemFlag> flags) {
        this.flags = flags;
        return (K) this;
    }

    public K flags(ItemFlag... flags) {
        if (flags != null) {
            HashSet<ItemFlag> set = VariableUtil.getOrDefault(this.flags, new HashSet<>());
            set.addAll(Arrays.asList(flags));
            this.flags = set;
        }
        return (K) this;
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

    @Override
    public Consumer<CustomConfiguration> get() {
        return c -> {

        };
    }

    @Override
    public Consumer<CustomConfiguration> save() {
        return c -> {
            if (getDisplayName() == null || getDisplayName().isBlank()) {
                c.addPath(getUuid());
            }
            c.setInt("amount", amount);
            c.setMaterial("material", material);
            c.setStringList("lore", lore);
        };
    }

    @Override
    public boolean isEmpty() {
        return amount < 1 || amount > 64 || material == null;
    }

    public String getUuid() {
        return HashCreator.getSha1Uuid(amount * material.hashCode() + getMaterial().toString()).toString();
    }

    public void replaceItem(String replace, String replacement) {
        if (defaultLore != null) {
            List<String> tempLore = new ArrayList<>();
            for (String s : defaultLore) {
                tempLore.add(s.replace(replace, replacement));
            }
            lore(tempLore);
        }
        if (defaultDisplayName != null) {
            name(defaultDisplayName.replace(replace, replacement));
        }
    }
}