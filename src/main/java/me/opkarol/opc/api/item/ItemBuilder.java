package me.opkarol.opc.api.item;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.misc.Tuple;
import me.opkarol.opc.api.utils.FormatUtils;
import me.opkarol.opc.api.utils.PDCUtils;
import me.opkarol.opc.api.utils.VariableUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("all")
public class ItemBuilder extends ItemStack implements Serializable, Cloneable {
    private final OpMap<String, String> tempReplacements = new OpMap<>();

    public ItemBuilder() {
    }

    /**
     * Defaults stack size to 1, with no extra data.
     * <p>
     * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain
     * <i>items</i>. Do not use this class to encapsulate Materials for which
     * {@link Material#isItem()} returns false.</b>
     *
     * @param type item material
     */
    public ItemBuilder(@NotNull Material type) {
        super(type);
    }

    /**
     * An item stack with no extra data.
     * <p>
     * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain
     * <i>items</i>. Do not use this class to encapsulate Materials for which
     * {@link Material#isItem()} returns false.</b>
     *
     * @param type   item material
     * @param amount stack size
     */
    public ItemBuilder(@NotNull Material type, int amount) {
        super(type, amount);
    }

    /**
     * Creates a new item stack derived from the specified stack
     *
     * @param stack the stack to copy
     * @throws IllegalArgumentException if the specified stack is null or
     *                                  returns an item meta not created by the item factory
     */
    public ItemBuilder(@NotNull ItemStack stack) throws IllegalArgumentException {
        super(stack);
    }

    public void checkItemMeta() {
        if (getItemMeta() == null) {
            throw new RuntimeException("Item meta is invalid. May enable exploits.");
        }
    }

    public ItemBuilder setLore(@NotNull List<String> lore) {
        useItemMeta(meta -> {
            List<String> tempLore = new ArrayList<>();
            for (String line : lore) {
                for (String replace : tempReplacements.keySet()) {
                    line = line.replace(replace, tempReplacements.unsafeGet(replace));
                }
                tempLore.add(line);
            }
            meta.setLore(FormatUtils.formatList(tempLore));
        });
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        return setLore(List.of(lore));
    }

    public ItemBuilder setName(String name) {
        useItemMeta(meta -> {
            String tempName = name;
            for (String replace : tempReplacements.keySet()) {
                tempName = tempName.replace(replace, tempReplacements.unsafeGet(replace));
            }
            meta.setDisplayName(FormatUtils.formatMessage(tempName));
        });
        return this;
    }

    public void applyPDC(String key, String value) {
        PDCUtils.addNBT(this, new NamespacedKey(OpAPI.getInstance(), key), value);

    }

    public ItemBuilder setEnchants(@NotNull OpMap<Enchantment, Integer> enchantments) {
        checkItemMeta();
        enchantments.getMap().forEach((enchantment, level) ->
                useItemMeta(meta ->
                        meta.addEnchant(enchantment, level, true)));
        return this;
    }

    @SafeVarargs
    public final ItemBuilder setEnchants(Tuple<Enchantment, Integer>... enchantments) {
        return setEnchants(VariableUtil.getMapFromTuples(enchantments));
    }

    public ItemBuilder setFlags(ItemFlag... itemFlag) {
        useItemMeta(meta -> meta.addItemFlags(itemFlag));
        return this;
    }

    private void useItemMeta(@NotNull Consumer<ItemMeta> action) {
        checkItemMeta();
        ItemMeta meta = getItemMeta();
        action.accept(meta);
        setItemMeta(meta);
    }

    public void updateLoreView() {
        if (getLore() != null) {
            setLore(getLore());
        }
    }

    /**
     * Replacements
    /*/

    public ItemBuilder addReplacement(String replace, String replacement) {
        tempReplacements.set(replace, replacement);
        return this;
    }

    public ItemBuilder addReplacement(@NotNull OpMap<String, String> map) {
        map.getMap().forEach((replace, replacement) -> {
            addReplacement(replace, replacement);
        });
        return this;
    }

    @SafeVarargs
    public final ItemBuilder addReplacement(Tuple<String, String>... map) {
        return addReplacement(VariableUtil.getMapFromTuples(map));
    }

    /**
     * Getters
     */

    public String getName() {
        checkItemMeta();
        return getItemMeta().getDisplayName();
    }

    public List<String> getLore() {
        checkItemMeta();
        return getItemMeta().getLore();
    }

    public OpMap<String, String> getTempReplacements() {
        return tempReplacements;
    }

    public ItemStack generate() {
        ItemBuilder copy = (ItemBuilder) super.clone();
        if (copy.getLore() != null && tempReplacements.keySet().size() != 0) {
            ItemMeta meta = copy.getItemMeta();
            List<String> tempLore = new ArrayList<>();
            for (String line : copy.getLore()) {
                for (String replace : tempReplacements.keySet()) {
                    line = line.replace(replace, tempReplacements.unsafeGet(replace));
                }
                tempLore.add(line);
            }
            meta.setLore(FormatUtils.formatList(tempLore));
            copy.setItemMeta(meta);
        }
        return copy;
    }
}
