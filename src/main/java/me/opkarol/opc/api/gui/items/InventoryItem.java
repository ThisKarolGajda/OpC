package me.opkarol.opc.api.gui.items;

import me.opkarol.opc.api.gui.events.chest.OnItemClicked;
import me.opkarol.opc.api.gui.holder.item.InventoryItemEventHolder;
import me.opkarol.opc.api.gui.holder.item.InventoryItemExtender;
import me.opkarol.opc.api.item.OpItemBuilder;
import me.opkarol.opc.api.utils.VariableUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class InventoryItem extends OpItemBuilder<InventoryItem> {
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

    public InventoryItem(String path, InventoryItemEventHolder itemEventHolder) {
        super(path);
        this.itemEventHolder = itemEventHolder;
    }

    public InventoryItem(@NotNull ItemStack item) {
        super(item);
        this.itemEventHolder = null;
    }

    public InventoryItem(Material material) {
        super(material);
        this.itemEventHolder = null;
    }

    public InventoryItem(String path) {
        super(path);
        this.itemEventHolder = null;
    }

    public InventoryItemEventHolder getItemEventHolder() {
        return itemEventHolder;
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

    public void setItemEventHolder(InventoryItemEventHolder itemEventHolder) {
        this.itemEventHolder = itemEventHolder;
    }
}
