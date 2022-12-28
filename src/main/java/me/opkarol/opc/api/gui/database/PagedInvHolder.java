package me.opkarol.opc.api.gui.database;

import me.opkarol.opc.api.gui.inventory.AbstractInventory;
import me.opkarol.opc.api.gui.inventory.InventoryPage;
import me.opkarol.opc.api.gui.items.InventoryItem;

import java.util.Optional;

public class PagedInvHolder extends InvHolder<InventoryPage<Integer, InventoryItem>> {
    private int currentPage;
    public int DEFAULT_PAGES_MAX_LIMIT = 100;
    public int DEFAULT_PAGES_MIN_LIMIT = 0;

    public PagedInvHolder(AbstractInventory<Integer, InventoryPage<Integer, InventoryItem>> inventory) {
        super(inventory);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void nextPage() {
        if (this.currentPage < DEFAULT_PAGES_MAX_LIMIT) {
            this.currentPage++;
        }
    }

    public void previousPage() {
        if (this.currentPage > DEFAULT_PAGES_MIN_LIMIT) {
            this.currentPage--;
        }
    }

    public InventoryPage<Integer, InventoryItem> getPage(int page) {
        Optional<InventoryPage<Integer, InventoryItem>> optional = getInventory().get(page);
        if (optional.isPresent()) {
            return optional.get();
        }
        InventoryPage<Integer, InventoryItem> page1 = new InventoryPage<>();
        getInventory().set(page, page1);
        return page1;
    }
}
