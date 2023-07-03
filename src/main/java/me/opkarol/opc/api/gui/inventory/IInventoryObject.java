package me.opkarol.opc.api.gui.inventory;

import me.opkarol.opc.api.gui.events.OnItemClicked;
import me.opkarol.opc.api.gui.items.InventoryItem;
import me.opkarol.opc.api.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public interface IInventoryObject {
    String getName();

    List<String> getLore();

    Material getMaterial();

    default Consumer<InventoryItem> getAdditionalAction() {
        return null;
    }

    default Consumer<OnItemClicked> getAction() {
        return null;
    }

    default ItemStack toItemStack() {
        ItemBuilder itemBuilder = new ItemBuilder(getMaterial());
        itemBuilder.setName(getName());
        itemBuilder.setLore(getLore());
        return itemBuilder;
    }
}
