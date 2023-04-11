package me.opkarol.opc.api.gui.items;

import me.opkarol.opc.api.gui.events.OnItemClicked;
import me.opkarol.opc.api.gui.holder.item.InventoryItemEventHolder;
import me.opkarol.opc.api.tools.HeadManager;

import java.util.function.Consumer;

public class HeadInventoryItem extends InventoryItem {
    public HeadInventoryItem(String minecraftValue, InventoryItemEventHolder itemEventHolder) {
        super(HeadManager.getHeadFromMinecraftValueUrl(minecraftValue), itemEventHolder);
    }

    public HeadInventoryItem(String minecraftValue, Consumer<OnItemClicked> itemEventHolder) {
        super(HeadManager.getHeadFromMinecraftValueUrl(minecraftValue), itemEventHolder);
    }

    public HeadInventoryItem(String minecraftValue) {
        super(HeadManager.getHeadFromMinecraftValueUrl(minecraftValue));
    }
}
