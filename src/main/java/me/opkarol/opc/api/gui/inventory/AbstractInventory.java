package me.opkarol.opc.api.gui.inventory;

import me.opkarol.opc.api.gui.InventoryCache;
import me.opkarol.opc.api.gui.holder.IHolder;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

public abstract class AbstractInventory<K, V> extends InventoryPage<K, V> {
    private final InventoryCache cache = InventoryCache.getCache();
    private final IHolder inventoryHolder;
    private final String inventoryTitle;
    private int inventorySlots;

    protected AbstractInventory(int inventorySlots, IHolder inventoryHolder, String inventoryTitle) {
        this.inventorySlots = inventorySlots;
        this.inventoryHolder = inventoryHolder;
        this.inventoryTitle = inventoryTitle;
    }

    protected AbstractInventory(IHolder inventoryHolder, String inventoryTitle) {
        this.inventoryHolder = inventoryHolder;
        this.inventoryTitle = inventoryTitle;
    }

    public OpMap<K, V> getMap() {
        return super.getMap();
    }

    public Optional<V> get(K k) {
        return super.getMap().getByKey(k);
    }

    public void set(K k, V v) {
        super.set(k, v);
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
