package me.opkarol.opc.api.item;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.list.OpList;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.map.OpMapBuilder;
import me.opkarol.opc.api.misc.Tuple;
import me.opkarol.opc.api.misc.hash.HashCreator;
import me.opkarol.opc.api.serialization.Serialize;
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
import java.util.stream.Collectors;

import static me.opkarol.opc.api.utils.FormatUtils.formatList;
import static me.opkarol.opc.api.utils.FormatUtils.formatMessage;

public class OpItemBuilder<K extends OpItemBuilder<?>> extends Serialize {
    private final OpMap<String, String> tempReplacements = new OpMap<>();
    private String displayName;
    private int amount = 1;
    private Material material;
    private List<String> lore;
    private OpMap<Enchantment, Integer> enchantments;
    private OpMap<String, String> pdc;
    private HashSet<ItemFlag> flags;

    public OpItemBuilder(ItemStack item) {
        super(null);
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
    }

    public OpItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public OpItemBuilder(@NotNull OpMapBuilder<String, Object> objects) {
        super(null);
        objects.getByKey("amount").ifPresent(amount -> amount((Integer) amount));
        objects.getByKey("material").ifPresent(material -> material((Material) material));
        objects.getByKey("lore").ifPresent(lore -> lore((List<String>) lore));
    }

    public String getDisplayName() {
        return VariableUtil.getOrDefault(displayName, getUuid());
    }

    public void setDisplayName(ItemMeta meta) {
        if (displayName == null) {
            return;
        }

        String tempName = displayName;
        for (String replace : tempReplacements.keySet()) {
            tempName = tempName.replace(replace, tempReplacements.unsafeGet(replace));
        }
        meta.setDisplayName(formatMessage(tempName));

    }

    public K name(String displayName) {
        this.displayName = displayName;
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

    public void setLore(ItemMeta meta) {
        if (lore == null) {
            return;
        }

        List<String> tempLore = new ArrayList<>();
        for (String line : lore) {
            for (String replace : tempReplacements.keySet()) {
                line = line.replace(replace, tempReplacements.unsafeGet(replace));
            }
            tempLore.add(line);
        }
        meta.setLore(formatList(tempLore));
    }

    public K lore(List<String> lore) {
        this.lore = lore;
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
            setDisplayName(meta);
            setLore(meta);
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

    public String getUuid() {
        return HashCreator.getSha1Uuid(amount * material.hashCode() + getMaterial().toString()).toString();
    }

    public OpItemBuilder<K> addReplacement(String replace, String replacement) {
        tempReplacements.set(replace, replacement);
        return this;
    }

    public OpItemBuilder<K> addReplacement(OpMap<String, String> map) {
        tempReplacements.addAll(map);
        return this;
    }

    @SafeVarargs
    public final OpItemBuilder<K> addReplacement(Tuple<String, String>... map) {
        tempReplacements.addAll(VariableUtil.getMapFromTuples(map));
        return this;
    }

    @Override
    public @NotNull OpMap<String, Object> serialize() {
        return getMapBuilder()
                .setValue("amount", amount)
                .setValue("material", material)
                .setValue("lore", lore);
    }

    @Override
    public String toString() {
        return "OpItemBuilder{" +
                "tempReplacements=" + tempReplacements +
                ", displayName='" + displayName + '\'' +
                ", amount=" + amount +
                ", material=" + material +
                ", lore=" + lore +
                ", enchantments=" + enchantments +
                ", pdc=" + pdc +
                ", flags=" + flags +
                '}';
    }
}