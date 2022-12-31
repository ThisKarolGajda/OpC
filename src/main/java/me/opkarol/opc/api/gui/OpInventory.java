package me.opkarol.opc.api.gui;

import me.opkarol.opc.api.gui.inventory.IInventoryObject;
import me.opkarol.opc.api.gui.database.InvHolderImpl;
import me.opkarol.opc.api.gui.events.chest.OnInventoryClose;
import me.opkarol.opc.api.gui.holder.inventory.InventoryEventExtender;
import me.opkarol.opc.api.gui.holder.inventory.InventoryEventHolder;
import me.opkarol.opc.api.gui.holder.item.InventoryItemExtender;
import me.opkarol.opc.api.gui.inventory.AbstractInventory;
import me.opkarol.opc.api.gui.inventory.IInventoryIdentification;
import me.opkarol.opc.api.gui.items.InventoryItem;
import me.opkarol.opc.api.gui.replacement.ReplacementInventoryImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public final class OpInventory extends ReplacementInventoryImpl {
    public <K extends AbstractInventory<?, ?> & IInventoryIdentification> OpInventory(K factory, Consumer<OnInventoryClose> action) {
        super(new InvHolderImpl(factory.getType(), factory, new InventoryEventExtender(action)));
    }

    public <K extends AbstractInventory<?, ?> & IInventoryIdentification> OpInventory(K factory, InventoryEventHolder action) {
        super(new InvHolderImpl(factory.getType(), factory, action));
    }

    public <K extends AbstractInventory<?, ?> & IInventoryIdentification> OpInventory(K factory) {
        super(new InvHolderImpl(factory.getType(), factory, null));
    }

    public void setGlobalItem(InventoryItem item, int slot) {
        setInPagesRange(getMaxPages(), item, slot);
    }

    public void setGlobalItem(int globalRange, InventoryItem item, int slot) {
        setInPagesRange(globalRange, item, slot);
    }

    public void setGlobalItems(int globalRange, InventoryItem item, int... slots) {
        if (slots == null) {
            return;
        }

        for (int slot : slots) {
            setGlobalItem(globalRange, item, slot);
        }
    }

    public void setGlobalItems(InventoryItem item, int... slots) {
        setGlobalItems(getMaxPages(), item, slots);
    }

    public void setAutoBuild(boolean autoBuild) {
        getInventoryHolder().getHolder().setAutoBuild(autoBuild);
    }

    public void setInventoryObject(@NotNull IInventoryObject object) {
        InventoryItem item = new InventoryItem(object.getMaterial());
        item.name(object.getName());
        item.lore(object.getLore());
        if (object.getAdditionalAction() != null) {
            object.getAdditionalAction().accept(item);
        }
        if (object.getAction() != null) {
            item.setItemEventHolder(new InventoryItemExtender(object.getAction()));
        }
        setNextEmpty(item);
    }

    public <K extends IInventoryObject> void setInventoryObjects(List<K> objects) {
        if (objects == null || objects.size() == 0) {
            return;
        }

        objects.forEach(this::setInventoryObject);
    }
}
