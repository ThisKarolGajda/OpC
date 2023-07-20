package me.opkarol.opc.api.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.Orientable;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import me.opkarol.opc.api.item.ItemBuilder;
import me.opkarol.opc.api.tools.HeadManager;
import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;

import static java.util.stream.Collectors.toCollection;

public class ItemPaletteGUI<M> extends ChestGui {
    private static final int ITEMS_PER_PAGE = 45;
    private static final int ROWS = 6;
    private final Predicate<M> itemFilter;
    private final Function<M, GuiItem> itemTransformer;
    private PaginatedPane itemsPane;
    private final List<M> values;
    private Consumer<OutlinePane> controlPaneConsumer;
    private List<GuiItem> displayItems;

    private ItemPaletteGUI(@NotNull Builder<M> builder, List<M> values) {
        super(ROWS, FormatUtils.formatMessage(builder.title));

        this.itemTransformer = builder.itemTransformer;
        this.itemFilter = builder.itemFilter;
        this.values = values;

        setOnTopClick(event -> event.setCancelled(true));
    }

    private void build() {
        addPane(this.itemsPane = createItemsPane());
        addPane(InventoryUtils.createRectangle(Pane.Priority.LOWEST, 1, 5, 8, 1, new GuiItem(InventoryUtils.createWall(Material.BLACK_STAINED_GLASS_PANE))));
        addPane(createControlPane());
        update();
    }

    /*
     * Panes
     */
    public @NotNull Pane createControlPane() {
        OutlinePane pane = new OutlinePane(0, 5, 9, 1, Pane.Priority.LOW);
        pane.setOrientation(Orientable.Orientation.HORIZONTAL);

        if (controlPaneConsumer != null) {
            pane.addItem(PageController.PREVIOUS.toItemStack(this, "&7Previous", this.itemsPane));
            controlPaneConsumer.accept(pane);
            pane.addItem(PageController.NEXT.toItemStack(this, "&7Next", this.itemsPane));
        } else {
            pane.setGap(7);
            pane.addItem(PageController.PREVIOUS.toItemStack(this, "&7Previous", this.itemsPane));
            pane.addItem(PageController.NEXT.toItemStack(this, "&7Next", this.itemsPane));
        }

        return pane;
    }

    public @NotNull PaginatedPane createItemsPane() {
        Deque<GuiItem> itemsToDisplay = new ArrayDeque<>();

        if (displayItems != null) {
            itemsToDisplay.addAll(displayItems);
        }

        if (values != null) {
            LinkedList<GuiItem> list = values.stream()
                    .filter(this.itemFilter != null ? this.itemFilter : predicate -> true)
                    .map(this.itemTransformer)
                    .collect(toCollection(LinkedList::new));
            itemsToDisplay.addAll(list);
        }

        PaginatedPane pane = new PaginatedPane(0, 0, 9, ROWS, Pane.Priority.LOWEST);

        for (int i = 0, pagesAmount = (itemsToDisplay.size() / ITEMS_PER_PAGE) + 1; i < pagesAmount; i++) {
            pane.addPane(i, createPage(itemsToDisplay));
        }

        pane.setPage(0);

        return pane;
    }

    public @NotNull Pane createPage(Deque<GuiItem> items) {
        OutlinePane page = new OutlinePane(0, 0, 9, ROWS, Pane.Priority.LOWEST);
        page.setOrientation(Orientable.Orientation.HORIZONTAL);

        for (int i = 1; i <= ITEMS_PER_PAGE; i++) {
            if (!items.isEmpty()) {
                page.addItem(items.removeLast());
            }
        }

        return page;
    }

    public void setControlPaneConsumer(Consumer<OutlinePane> controlPaneConsumer) {
        this.controlPaneConsumer = controlPaneConsumer;
    }

    public Consumer<OutlinePane> getControlPaneConsumer() {
        return controlPaneConsumer;
    }

    public Function<M, GuiItem> getItemTransformer() {
        return itemTransformer;
    }

    public static int getItemsPerPage() {
        return ITEMS_PER_PAGE;
    }

    public static int getROWS() {
        return ROWS;
    }

    public List<M> getValues() {
        return values;
    }

    public PaginatedPane getItemsPane() {
        return itemsPane;
    }

    public Predicate<M> getItemFilter() {
        return itemFilter;
    }

    public List<GuiItem> getDisplayItems() {
        return displayItems;
    }

    public void setDisplayItems(List<GuiItem> displayItems) {
        this.displayItems = displayItems;
    }

    public enum PageController {
        PREVIOUS(HeadManager.getHeadFromMinecraftValueUrl("bd69e06e5dadfd84e5f3d1c21063f2553b2fa945ee1d4d7152fdc5425bc12a9"), (page, itemsPane) -> page > 0, page -> --page),
        NEXT(HeadManager.getHeadFromMinecraftValueUrl("19bf3292e126a105b54eba713aa1b152d541a1d8938829c56364d178ed22bf"), (page, itemsPane) -> page < (itemsPane.getPages() - 1), page -> ++page);

        private final ItemStack skull;
        private final BiPredicate<Integer, PaginatedPane> shouldContinue;
        private final IntUnaryOperator nextPageSupplier;

        PageController(ItemStack skull, BiPredicate<Integer, PaginatedPane> shouldContinue, IntUnaryOperator nextPageSupplier) {
            this.skull = skull;
            this.shouldContinue = shouldContinue;
            this.nextPageSupplier = nextPageSupplier;
        }

        public @NotNull GuiItem toItemStack(ChestGui gui, String itemName, PaginatedPane itemsPane) {
            return new GuiItem(new ItemBuilder(skull.clone()).setName(itemName), event -> {
                int currentPage = itemsPane.getPage();

                if (!this.shouldContinue.test(currentPage, itemsPane))
                    return;

                itemsPane.setPage(this.nextPageSupplier.applyAsInt(currentPage));
                gui.update();
            });
        }
    }


    public static class Builder<M> {
        private final String title;
        private Function<M, GuiItem> itemTransformer;
        private Predicate<M> itemFilter;
        private Consumer<OutlinePane> controlPaneConsumer;
        private List<GuiItem> displayItems;

        public Builder(String title) {
            this.title = title;
        }

        public Builder<M> as(Function<M, GuiItem> itemTransformer) {
            this.itemTransformer = itemTransformer;
            return this;
        }

        public Builder<M> show(Predicate<M> itemFilter) {
            this.itemFilter = itemFilter;
            return this;
        }

        public ItemPaletteGUI<M> build(List<M> values) {
            ItemPaletteGUI<M> paletteGUI = new ItemPaletteGUI<>(this, values);
            paletteGUI.setControlPaneConsumer(this.controlPaneConsumer);
            paletteGUI.setDisplayItems(this.displayItems);
            paletteGUI.build();
            return paletteGUI;
        }

        public ItemPaletteGUI<M> build(@NotNull Supplier<List<M>> values) {
            return build(values.get());
        }

        public ItemPaletteGUI<M> build(M value) {
            return build(List.of(value));
        }

        @Contract(pure = true)
        @SafeVarargs
        public final @NotNull ItemPaletteGUI<M> build(M... values) {
            return build(Arrays.asList(values));
        }

        public Builder<M> controlPane(Consumer<OutlinePane> controlPaneConsumer) {
            this.controlPaneConsumer = controlPaneConsumer;
            return this;
        }

        public Builder<M> displayItems(List<GuiItem> displayItems) {
            this.displayItems = displayItems;
            return this;
        }
    }
}
