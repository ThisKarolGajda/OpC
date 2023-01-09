package me.opkarol.opc.api.gui.inventory;

import me.opkarol.opc.api.gui.database.InventoryHolderFactory;
import me.opkarol.opc.api.gui.items.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

public class InventoryFactory extends AbstractInventory<Integer, InventoryItem> implements IInventoryIdentification {
    public InventoryFactory(int inventorySlots, String inventoryTitle) {
        super(inventorySlots, () -> inventoryTitle, inventoryTitle);
    }

    public InventoryFactory(String inventoryTitle) {
        super(() -> inventoryTitle, inventoryTitle);
    }

    @Override
    public Inventory generate(int page) {
        Inventory inventory = Bukkit.createInventory(getInventoryHolder(), getInventorySlots(), getInventoryTitle());

        for (int i = 0; i < getInventorySlots(); i++) {
            Optional<InventoryItem> optional = get(i);
            if (optional.isPresent()) {
                inventory.setItem(i, optional.get().generate());
            }
        }

        return inventory;
    }

    @Override
    public InventoryHolderFactory.INVENTORY_HOLDER_TYPE getType() {
        return InventoryHolderFactory.INVENTORY_HOLDER_TYPE.DEFAULT;
    }
}
