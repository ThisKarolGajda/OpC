package me.opkarol.opc.api.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.opkarol.opc.api.item.ItemBuilder;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.misc.Tuple;
import me.opkarol.opc.api.utils.VariableUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

import static me.opkarol.opc.api.utils.VariableUtil.getRandomSolidMaterial;

public interface IGuiItemBuilder {

    default Material getItemMaterial() {
        return getRandomSolidMaterial(getItemName() != null ? ChatColor.stripColor(getItemName()) : "");
    }

    String getItemName();

    List<String> getItemLore();

    default Tuple<Enchantment, Integer>[] getItemEnchants() {
        return null;
    }

    default ItemFlag[] getItemFlags() {
        return null;
    }

    default GuiItem to(Consumer<InventoryClickEvent> action) {
        ItemBuilder itemBuilder = new ItemBuilder(getItemMaterial())
                .setName(getItemName())
                .setLore(getItemLore())
                .setEnchants(getItemEnchants() != null ? VariableUtil.getMapFromTuples(getItemEnchants()) : new OpMap<>());
        if (getItemFlags() != null) {
            itemBuilder.setFlags(getItemFlags());
        }
        return new GuiItem(itemBuilder, action);
    }

    default GuiItem to(ItemStack parent, Consumer<InventoryClickEvent> action) {
        ItemBuilder itemBuilder = new ItemBuilder(parent)
                .setName(getItemName())
                .setLore(getItemLore())
                .setEnchants(getItemEnchants() != null ? VariableUtil.getMapFromTuples(getItemEnchants()) : new OpMap<>());
        if (getItemFlags() != null) {
            itemBuilder.setFlags(getItemFlags());
        }
        return new GuiItem(itemBuilder, action);
    }


    default GuiItem to() {
        // Looks like InventoryFramework doesn't have null check, so it's better to keep consumer like this
        return to(event -> {});
    }
}
