package me.opkarol.opc.api.item;

import me.opkarol.opc.api.configuration.CustomConfiguration;
import me.opkarol.opc.api.configuration.IEmptyConfiguration;
import me.opkarol.opc.api.gui.misc.OpInventorySpecialData;
import me.opkarol.opc.api.list.OpList;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

public class OpInventoryItem implements Serializable, IEmptyConfiguration {
    private OpItemBuilder item;
    private Consumer<InventoryClickEvent> clickAction = e -> {};
    private Consumer<InventoryDragEvent> dragAction = e -> {};
    private List<OpInventorySpecialData> specialData;

    public OpInventoryItem(Material material) {
        this.item = new OpItemBuilder(new ItemStack(material));
    }

    public OpInventoryItem(Material material, String displayName) {
        this.item = new OpItemBuilder(new ItemStack(material)).setDisplayName(displayName);
    }

    public OpInventoryItem(Material material, List<OpInventorySpecialData> specialData) {
        this.item = new OpItemBuilder(new ItemStack(material));
        this.specialData = specialData;
    }

    public OpInventoryItem(Material material, OpInventorySpecialData specialData) {
        this.item = new OpItemBuilder(new ItemStack(material));
        this.specialData = List.of(specialData);
    }

    public OpInventoryItem(OpItemBuilder item) {
        this.item = item;
    }

    public OpInventoryItem(OpItemBuilder item, OpInventorySpecialData specialData) {
        this.item = item;
        this.specialData = List.of(specialData);
    }

    public OpInventoryItem(OpItemBuilder item, List<OpInventorySpecialData> specialData) {
        this.item = item;
        this.specialData = specialData;
    }

    public OpInventoryItem(OpItemBuilder item, Consumer<InventoryClickEvent> clickAction, Consumer<InventoryDragEvent> dragAction) {
        this.item = item;
        this.clickAction = clickAction;
        this.dragAction = dragAction;
    }

    public OpInventoryItem(OpItemBuilder item, Consumer<InventoryClickEvent> clickAction) {
        this.item = item;
        this.clickAction = clickAction;
    }

    public OpItemBuilder getItem() {
        return item;
    }

    public OpInventoryItem getItemConsumer(@NotNull Consumer<OpItemBuilder> consumer) {
        OpItemBuilder builder = getItem();
        consumer.accept(builder);
        return setItem(builder);
    }

    public ItemStack getItemStack() {
        return item.generate();
    }

    public Consumer<InventoryClickEvent> getClickAction() {
        return clickAction;
    }

    public OpInventoryItem setClickAction(Consumer<InventoryClickEvent> clickAction) {
        this.clickAction = clickAction;
        return this;
    }

    public Consumer<InventoryDragEvent> getDragAction() {
        return dragAction;
    }

    public OpInventoryItem setDragAction(Consumer<InventoryDragEvent> dragAction) {
        this.dragAction = dragAction;
        return this;
    }

    public OpInventoryItem setBlockItem() {
        setClickAction(e -> e.setCancelled(true))
                .setDragAction(e -> e.setCancelled(true));
        return this;
    }

    public OpInventoryItem setItem(OpItemBuilder item) {
        this.item = item;
        return this;
    }

    public List<OpInventorySpecialData> getSpecialData() {
        return specialData;
    }

    public OpList<String> getConfigSpecialData() {
        if (specialData == null) {
            return new OpList<>();
        }
        return specialData.stream()
                .map(OpInventorySpecialData::toString)
                .collect(OpList.getCollector());
    }

    public OpInventoryItem setSpecialData(List<OpInventorySpecialData> specialData) {
        this.specialData = specialData;
        return this;
    }

    public boolean hasSpecialData(OpInventorySpecialData data) {
        if (getSpecialData() == null) {
            return false;
        }
        return getSpecialData().contains(data);
    }

    @Override
    public Consumer<CustomConfiguration> get() {
        return c -> item = new OpItemBuilder(c.getDefaultPath());
    }

    @Override
    public Consumer<CustomConfiguration> save() {
        return c -> item.save(c.getDefaultPath());
    }

    @Override
    public boolean isEmpty() {
        return item == null;
    }
}
