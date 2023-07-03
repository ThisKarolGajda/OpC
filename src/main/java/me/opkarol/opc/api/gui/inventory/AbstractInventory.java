package me.opkarol.opc.api.gui.inventory;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.gui.InventoryCache;
import me.opkarol.opc.api.gui.holder.IHolder;
import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

public abstract class AbstractInventory<K, V> extends InventoryPage<K, V> {
    private final InventoryCache cache;
    private final IHolder inventoryHolder;
    private final String inventoryTitle;
    private final int inventorySlots;

    protected AbstractInventory(int inventorySlots, IHolder inventoryHolder, String inventoryTitle) {
        this.inventorySlots = inventorySlots;
        this.inventoryHolder = inventoryHolder;
        this.inventoryTitle = inventoryTitle;
        this.cache = OpAPI.getInstance().getInventoryCache();
    }

    protected AbstractInventory(IHolder inventoryHolder, String inventoryTitle) {
        this(27, inventoryHolder, inventoryTitle);
    }

    public Optional<V> get(K k) {
        return super.getMap().getByKey(k);
    }

    public boolean contains(K k) {
        return super.getMap().containsKey(k);
    }

    public InventoryCache getCache() {
        return cache;
    }

    public IHolder getInventoryHolder() {
        return inventoryHolder;
    }

    public int getInventorySlots() {
        return inventorySlots;
    }

    public String getInventoryTitle() {
        return FormatUtils.formatMessage(inventoryTitle);
    }

    public abstract Inventory generate(int page);

    public Inventory generate() {
        return generate(0);
    }
}
