package me.opkarol.opc.api.gui.inventory;

import me.opkarol.opc.api.gui.events.OnItemClicked;
import me.opkarol.opc.api.gui.items.InventoryItem;
import org.bukkit.Material;

import java.util.List;
import java.util.function.Consumer;

public interface IInventoryObject {
    String getName();

    List<String> getLore();

    Material getMaterial();

    default Consumer<InventoryItem> getAdditionalAction() {
        return null;
    }

    Consumer<OnItemClicked> getAction();
}
