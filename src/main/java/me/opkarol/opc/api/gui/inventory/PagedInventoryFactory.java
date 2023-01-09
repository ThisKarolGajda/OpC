package me.opkarol.opc.api.gui.inventory;

import me.opkarol.opc.api.gui.database.InventoryHolderFactory;
import me.opkarol.opc.api.gui.items.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

public class PagedInventoryFactory extends AbstractInventory<Integer, InventoryPage<Integer, InventoryItem>> implements IInventoryIdentification {
    public PagedInventoryFactory(int inventorySlots, String inventoryTitle) {
        super(inventorySlots, () -> inventoryTitle, inventoryTitle);
    }

    public PagedInventoryFactory(String inventoryTitle) {
        super(() -> inventoryTitle, inventoryTitle);
    }

    @Override
    public Inventory generate(int page) {
        Inventory inventory = Bukkit.createInventory(getInventoryHolder(), getInventorySlots(), getInventoryTitle());

        InventoryPage<Integer, InventoryItem> inventoryPage;
        Optional<InventoryPage<Integer, InventoryItem>> optional = get(page);
        if (optional.isPresent()) {
            inventoryPage = optional.get();
        } else {
            inventoryPage = new InventoryPage<>();
            set(page, inventoryPage);
        }

        for (int slot = 0; slot < getInventorySlots(); slot++) {
            Optional<InventoryItem> optional1 = inventoryPage.get(slot);
            if (optional1.isPresent()) {
                inventory.setItem(slot, optional1.get().generate());
            }

        }

        return inventory;
    }

    @Override
    public InventoryHolderFactory.INVENTORY_HOLDER_TYPE getType() {
        return InventoryHolderFactory.INVENTORY_HOLDER_TYPE.PAGED;
    }
}
