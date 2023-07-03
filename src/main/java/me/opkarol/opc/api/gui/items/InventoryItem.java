package me.opkarol.opc.api.gui.items;

import me.opkarol.opc.api.gui.events.OnItemClicked;
import me.opkarol.opc.api.gui.holder.item.InventoryItemEventHolder;
import me.opkarol.opc.api.gui.holder.item.InventoryItemExtender;
import me.opkarol.opc.api.item.ItemBuilder;
import me.opkarol.opc.api.utils.VariableUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class InventoryItem extends ItemBuilder implements Cloneable {
    private InventoryItemEventHolder itemEventHolder;
    private List<InventoryItemSpecialData> specialData = new ArrayList<>();

    public InventoryItem(@NotNull ItemStack item, InventoryItemEventHolder itemEventHolder) {
        super(item);
        this.itemEventHolder = itemEventHolder;
    }

    public InventoryItem(Material material, InventoryItemEventHolder itemEventHolder) {
        super(material);
        this.itemEventHolder = itemEventHolder;
    }

    public InventoryItem(Material material, Consumer<OnItemClicked> itemEventHolder) {
        super(material);
        this.itemEventHolder = new InventoryItemExtender(itemEventHolder);
    }

    public InventoryItem(@NotNull ItemStack item, Consumer<OnItemClicked> itemEventHolder) {
        super(item);
        this.itemEventHolder = new InventoryItemExtender(itemEventHolder);
    }

    public InventoryItem(@NotNull ItemStack item) {
        super(item);
        this.itemEventHolder = null;
    }

    public InventoryItem(Material material) {
        super(material);
        this.itemEventHolder = null;
    }

    public InventoryItemEventHolder getItemEventHolder() {
        return itemEventHolder;
    }

    public void setItemEventHolder(InventoryItemEventHolder itemEventHolder) {
        this.itemEventHolder = itemEventHolder;
    }

    public List<InventoryItemSpecialData> getSpecialData() {
        return specialData;
    }

    public void addSpecialData(InventoryItemSpecialData data) {
        specialData = VariableUtil.getWith(specialData, data);
    }

    public boolean isPagedInventoryButtonNext() {
        return specialData != null && specialData.contains(InventoryItemSpecialData.PAGED_INVENTORY_BUTTON_NEXT);
    }

    public boolean isPagedInventoryButtonPrevious() {
        return specialData != null && specialData.contains(InventoryItemSpecialData.PAGED_INVENTORY_BUTTON_PREVIOUS);
    }

    public boolean hasAnyData() {
        return specialData != null && specialData.size() > 0;
    }

    @Override
    public @NotNull InventoryItem clone() {
        InventoryItem clone = (InventoryItem) super.clone();
        clone.itemEventHolder = this.itemEventHolder != null ? this.itemEventHolder.clone() : null;
        clone.specialData = new ArrayList<>(this.specialData);
        return clone;
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "itemEventHolder=" + itemEventHolder +
                ", specialData=" + specialData +
                ", item=" + super.toString() +
                '}';
    }
}
