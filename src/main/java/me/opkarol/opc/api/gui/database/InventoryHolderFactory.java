package me.opkarol.opc.api.gui.database;

import me.opkarol.opc.api.gui.events.InventoryActionManager;
import me.opkarol.opc.api.gui.InventoryCache;
import me.opkarol.opc.api.gui.holder.IHolder;
import me.opkarol.opc.api.gui.holder.inventory.InventoryEventHolder;
import me.opkarol.opc.api.gui.inventory.AbstractInventory;
import me.opkarol.opc.api.gui.inventory.InventoryPage;
import me.opkarol.opc.api.gui.items.InventoryItem;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class InventoryHolderFactory {
    private final InventoryHolder<?> holder;
    private final INVENTORY_HOLDER_TYPE type;
    private final InventoryActionManager manager;

    public InventoryHolderFactory(InventoryHolder<?> holder, INVENTORY_HOLDER_TYPE type, InventoryEventHolder closeAction) {
        this.holder = holder;
        this.type = type;
        this.manager = new InventoryActionManager(this, closeAction);
    }

    public InventoryHolderFactory(@NotNull INVENTORY_HOLDER_TYPE type, AbstractInventory<?, ?> inventory, InventoryEventHolder closeAction) {
        if (type.equals(INVENTORY_HOLDER_TYPE.DEFAULT)) {
            this.holder = new DefaultInventoryHolder((AbstractInventory<Integer, InventoryItem>) inventory);
        } else {
            this.holder = new PagedInventoryHolder((AbstractInventory<Integer, InventoryPage<Integer, InventoryItem>>) inventory);
        }
        this.type = type;
        this.manager = new InventoryActionManager(this, closeAction);
    }

    public InventoryHolderFactory(DefaultInventoryHolder holder, InventoryEventHolder closeAction) {
        this.holder = holder;
        this.type = INVENTORY_HOLDER_TYPE.DEFAULT;
        this.manager = new InventoryActionManager(this, closeAction);
    }

    public InventoryHolderFactory(PagedInventoryHolder holder, InventoryEventHolder closeAction) {
        this.holder = holder;
        this.type = INVENTORY_HOLDER_TYPE.PAGED;
        this.manager = new InventoryActionManager(this, closeAction);
    }

    public InventoryHolderFactory(AbstractInventory<Integer, InventoryItem> inventory, InventoryEventHolder closeAction) {
        this(new DefaultInventoryHolder(inventory), closeAction);
    }

    public InventoryHolder<?> getHolder() {
        return holder;
    }

    public DefaultInventoryHolder getDefaultHolder() {
        return (DefaultInventoryHolder) holder;
    }

    public PagedInventoryHolder getPagedHolder() {
        return (PagedInventoryHolder) holder;
    }

    public INVENTORY_HOLDER_TYPE getType() {
        return type;
    }

    public int getInventorySlots() {
        return getHolder().getInventory().getInventorySlots();
    }

    public InventoryActionManager getActionManager() {
        return manager;
    }

    public enum INVENTORY_HOLDER_TYPE {
        PAGED,
        DEFAULT
    }

    public void set(int pageNumber, InventoryItem item, int... ints) {
        if (ints == null) {
            return;
        }

        switch (type) {
            case PAGED -> {
                InventoryPage<Integer, InventoryItem> page = getPagedHolder().getPage(pageNumber);
                for (int i : ints) {
                    page.set(i, item);
                }
            }
            case DEFAULT -> {
                for (int i : ints) {
                    getDefaultHolder().getInventory().set(i, item);
                }
            }
        }
    }

    public void set(InventoryItem item, int... slots) {
        set(0, item, slots);
    }

    public boolean isBuilt(int page) {
        return getHolder().isBuilt(page);
    }

    public boolean isBuilt() {
        return isBuilt(0);
    }

    public Inventory getBuiltInventory(int page) {
        return getHolder().getInventoriesBuilt().getByKey(page).orElse(null);
    }

    public Inventory getBuiltInventory() {
        return getBuiltInventory(0);
    }

    public void addBuiltInventory(int page, Inventory inventory) {
        getHolder().addInventoryBuilt(page, inventory);
    }

    public void addBuiltInventory(Inventory inventory) {
        addBuiltInventory(0, inventory);
    }

    public Inventory build(int page) {
        return getHolder().build(page);
    }

    public Inventory build() {
        return build(0);
    }

    public InventoryCache getCache() {
        return getHolder().getInventory().getCache();
    }

    public IHolder getIHolder() {
        return getHolder().getInventory().getInventoryHolder();
    }

    public Optional<InventoryItem> getItem(int slot) {
        return getItem(0, slot);
    }

    public List<Integer> getAllSlots(int page) {
        return switch (getType()) {
            case PAGED -> getPagedHolder().getInventory().getMap().getMap().get(page).getMap().keySet().stream().toList();
            case DEFAULT -> getDefaultHolder().getInventory().getMap().keySet().stream().toList();
        };
    }

    public Optional<InventoryItem> getItem(int page, int slot) {
        switch (getType()) {
            case PAGED -> {
                return getPagedHolder().getInventory().get(page).orElse(new InventoryPage<>()).get(slot);
            }
            case DEFAULT -> {
                return getDefaultHolder().getInventory().get(slot);
            }
            default -> throw new IllegalStateException("Unexpected value: " + getType());
        }
    }
}