package me.opkarol.opc.api.gui;

import me.opkarol.opc.api.gui.database.InvHolderImpl;
import me.opkarol.opc.api.gui.events.chest.OnInventoryClose;
import me.opkarol.opc.api.gui.holder.inventory.InventoryEventExtender;
import me.opkarol.opc.api.gui.holder.inventory.InventoryEventHolder;
import me.opkarol.opc.api.gui.inventory.AbstractInventory;
import me.opkarol.opc.api.gui.inventory.IInventoryIdentification;
import me.opkarol.opc.api.gui.items.InventoryItem;
import me.opkarol.opc.api.gui.replacement.ReplacementInventoryImpl;

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

    public void setAutoBuild(boolean autoBuild) {
        getInventoryHolder().getHolder().setAutoBuild(autoBuild);
    }
}
