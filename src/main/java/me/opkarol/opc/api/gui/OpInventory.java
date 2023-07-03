package me.opkarol.opc.api.gui;

import me.opkarol.opc.api.gui.database.InventoryHolderFactory;
import me.opkarol.opc.api.gui.events.OnInventoryClose;
import me.opkarol.opc.api.gui.holder.inventory.InventoryEventExtender;
import me.opkarol.opc.api.gui.holder.inventory.InventoryEventHolder;
import me.opkarol.opc.api.gui.holder.item.InventoryItemExtender;
import me.opkarol.opc.api.gui.inventory.AbstractInventory;
import me.opkarol.opc.api.gui.inventory.IInventoryIdentification;
import me.opkarol.opc.api.gui.inventory.IInventoryObject;
import me.opkarol.opc.api.gui.items.InventoryItem;
import me.opkarol.opc.api.gui.replacement.ReplacementInventoryImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.function.Consumer;

public final class OpInventory extends ReplacementInventoryImpl {
    public <K extends AbstractInventory<?, ?> & IInventoryIdentification> OpInventory(K factory, Consumer<OnInventoryClose> action) {
        super(new InventoryHolderFactory(factory.getType(), factory, new InventoryEventExtender(action)));
    }

    public <K extends AbstractInventory<?, ?> & IInventoryIdentification> OpInventory(K factory, InventoryEventHolder action) {
        super(new InventoryHolderFactory(factory.getType(), factory, action));
    }

    public <K extends AbstractInventory<?, ?> & IInventoryIdentification> OpInventory(K factory) {
        super(new InventoryHolderFactory(factory.getType(), factory, null));
    }

    public void setGlobalItem(InventoryItem item, @Range(from = 0, to = 53) int slot) {
        setInPagesRange(getMaxPages(), item, slot);
    }

    public void setGlobalItem(int globalRange, InventoryItem item, @Range(from = 0, to = 53) int slot) {
        setInPagesRange(globalRange, item, slot);
    }

    public void setGlobalItems(int globalRange, InventoryItem item, @Range(from = 0, to = 53) int... slots) {
        if (slots == null) {
            return;
        }

        for (int slot : slots) {
            setGlobalItem(globalRange, item, slot);
        }
    }

    public void setGlobalItems(InventoryItem item, @Range(from = 0, to = 53) int... slots) {
        setGlobalItems(getMaxPages(), item, slots);
    }

    public void setAutoBuild(boolean autoBuild) {
        getInventoryHolder().getHolder().setAutoBuild(autoBuild);
    }

    public void setInventoryObject(@NotNull IInventoryObject object) {
        InventoryItem item = new InventoryItem(object.getMaterial());
        item.setName(object.getName());
        item.setLore(object.getLore());
        if (object.getAdditionalAction() != null) {
            object.getAdditionalAction().accept(item);
        }
        item.setItemEventHolder(new InventoryItemExtender(object.getAction()));

        setNextEmpty(item);
    }

    public void setInventoryObjects(List<? extends IInventoryObject> objects) {
        if (objects == null || objects.size() == 0) {
            return;
        }

        objects.forEach(this::setInventoryObject);
    }

    public void clear() {
        getDefaultTranslations().clear();
        getInventoryHolder().getBuiltInventory().clear();
        getInventoryHolder().getIHolder().getInventory().clear();
        getInventoryHolder().getHolder().getInventory().getMap().clear();
    }
}
