package me.opkarol.opc.api.gui.replacement;

import me.opkarol.opc.api.gui.database.InventoryHolderFactory;
import me.opkarol.opc.api.gui.items.InventoryItem;
import me.opkarol.opc.api.gui.pattern.InventoryPattern;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.misc.Tuple;
import me.opkarol.opc.api.utils.VariableUtil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplacementInventoryImpl extends ReplacementInventory {
    private final static String REGEX = "%[A-Za_-z0-9_]+%";
    private final InventoryHolderFactory inventoryHolder;
    private OpMap<String, Function<ReplacementInventoryImpl, Object>> defaultTranslations = new OpMap<>();

    private boolean saveDefaultTranslations;

    public ReplacementInventoryImpl(InventoryHolderFactory inventoryHolder) {
        this.inventoryHolder = inventoryHolder;
    }

    public InventoryHolderFactory getInventoryHolder() {
        return inventoryHolder;
    }

    public int getMaxPages() {
        return switch (getInventoryHolder().getType()) {
            case PAGED -> getInventoryHolder().getPagedHolder().DEFAULT_PAGES_MAX_LIMIT;
            case DEFAULT -> 0;
        };
    }

    public int getCurrentPage() {
        return switch (getInventoryHolder().getType()) {
            case PAGED -> getInventoryHolder().getPagedHolder().getCurrentPage();
            case DEFAULT -> 0;
        };
    }

    @SafeVarargs
    public final void replace(int page, Tuple<String, String>... replacements) {
        replace(page, VariableUtil.getMapFromTuples(replacements));
    }

    @SafeVarargs
    public final Inventory replaceBuilt(int page, Tuple<String, String>... replacements) {
        return replaceBuilt(page, VariableUtil.getMapFromTuples(replacements));
    }

    public final void replace(int page, OpMap<String, String> replacements) {
        if (getInventoryHolder().isBuilt(page)) {
            getInventoryHolder().addBuiltInventory(page, replaceBuilt(page, replacements));
        } else {
            replaceNotBuilt(page, replacements);
        }
    }

    public final void replaceNotBuilt(int page, OpMap<String, String> replacements) {
        assureMapExistence(page);

        OpMap<Integer, InventoryItem> temp = new OpMap<>();
        for (String replace : getReplacements().unsafeGet(page).keySet()) {
            Optional<String> optional = replacements.getByKey(replace);
            if (optional.isEmpty()) {
                continue;
            }
            String replacement = optional.get();
            for (int i : getReplacements().unsafeGet(page).unsafeGet(replace)) {
                if (temp.containsKey(i)) {
                    InventoryItem item = temp.unsafeGet(i);
                    item.addReplacement(replace, replacement);
                    temp.set(i, item);
                } else {
                    getInventoryHolder().getItem(page, i).ifPresent(item -> {
                        item.addReplacement(replace, replacement);
                        temp.set(i, item);
                    });
                }
            }
        }

        for (int i : temp.keySet()) {
            InventoryItem item = temp.unsafeGet(i);
            set(i, item);
        }
    }

    public final void replaceNotBuiltWithFunction(int page, OpMap<String, Function<ReplacementInventoryImpl, Object>> replacements) {
        assureMapExistence(page);
        for (String replace : getReplacements().unsafeGet(page).keySet()) {
            for (int i : getReplacements().unsafeGet(page).unsafeGet(replace)) {
                getInventoryHolder().getItem(page, i).ifPresent(item ->
                        replacements.getByKey(replace)
                                .ifPresent(replacement -> item.addReplacement(replace, String.valueOf(replacement.apply(this)))));
            }
        }
    }

    public final Inventory replaceBuilt(int page, OpMap<String, String> replacements) {
        Inventory inventory = getInventoryHolder().getBuiltInventory(page);

        OpMap<Integer, InventoryItem> pageMap = getInventoryHolder().getDefaultHolder().getInventory().getMap();
        OpMap<Integer, InventoryItem> tempItems = new OpMap<>();

        for (String replace : getReplacements().unsafeGet(page).keySet()) {
            List<Integer> integers = getReplacements().unsafeGet(page).unsafeGet(replace);
            for (int i : integers) {
                InventoryItem item = tempItems.containsKey(i) ? tempItems.unsafeGet(i) : pageMap.unsafeGet(i);

                replacements.getByKey(replace)
                        .ifPresent(replacement -> {
                            item.addReplacement(replace, replacement);
                            tempItems.set(i, item);
                        });
            }
        }

        for (int i : tempItems.keySet()) {
            InventoryItem builder = tempItems.unsafeGet(i);
            inventory.setItem(i, builder.generate());
        }

        return inventory;
    }

    public final Inventory replaceBuiltWithFunction(int page, OpMap<String, Function<ReplacementInventoryImpl, Object>> replacements) {
        Inventory inventory = getInventoryHolder().getBuiltInventory(page);

        OpMap<Integer, InventoryItem> pageMap = getInventoryHolder().getDefaultHolder().getInventory().getMap();
        OpMap<Integer, InventoryItem> tempItems = new OpMap<>();

        for (String replace : getReplacements().unsafeGet(page).keySet()) {
            List<Integer> integers = getReplacements().unsafeGet(page).unsafeGet(replace);
            for (int i : integers) {
                InventoryItem item = tempItems.containsKey(i) ? tempItems.unsafeGet(i) : pageMap.unsafeGet(i);

                replacements.getByKey(replace)
                        .ifPresent(replacement -> {
                            item.addReplacement(replace, replacement.apply(this).toString());
                            tempItems.set(i, item);
                        });
            }
        }

        for (int i : tempItems.keySet()) {
            InventoryItem builder = tempItems.unsafeGet(i);
            inventory.setItem(i, builder.generate());
        }

        return inventory;
    }

    public void set(int page, @NotNull InventoryItem item, int slot) {
        for (String replacement : addRegex(item.getDisplayName())) {
            addReplacement(page, replacement, slot);
        }
        for (String replacement : addRegex(item.getLore())) {
            addReplacement(page, replacement, slot);
        }
        getInventoryHolder().set(page, item, slot);
    }

    public void setInPagesRange(int range, InventoryItem item, int slot) {
        for (int i = 0; i < range; i++) {
            set(i, item, slot);
        }
    }

    public void set(@NotNull InventoryItem item, int slot) {
        set(0, item, slot);
    }

    public void setNextEmpty(InventoryItem item) {
        for (int page = 0; page < getMaxPages(); page++) {
            for (int slot = 0; slot < getInventoryHolder().getInventorySlots(); slot++) {
                if (isSlotEmpty(page, slot)) {
                    set(page, item, slot);
                    return;
                }
            }
        }
    }

    public void safeSet(int page, InventoryItem item, int slot) {
        if (isSlotEmpty(page, slot)) {
            set(page, item, slot);
        }
    }

    public boolean isSlotEmpty(int page, int slot) {
        return getInventoryHolder().getItem(page, slot).isEmpty();
    }

    public void safeSet(int page, InventoryItem item, int... slots) {
        if (slots == null) {
            return;
        }

        for (int i : slots) {
            safeSet(page, item, i);
        }
    }

    public void set(int page, @NotNull InventoryItem item, int... slots) {
        if (slots == null) {
            return;
        }

        for (int i : slots) {
            set(page, item, i);
        }
    }

    public void setAll(int page, InventoryItem item) {
        for (int i = 0; i < getInventoryHolder().getInventorySlots(); i++) {
            set(page, item, i);
        }
    }

    public void setAllUnused(int page, InventoryItem item) {
        for (int i = 0; i < getInventoryHolder().getInventorySlots(); i++) {
            safeSet(page, item, i);
        }
    }

    public void setAllUnused(InventoryItem item) {
        setAllUnused(0, item);
    }

    public void setAllExcept(int page, InventoryItem item, int... except) {
        if (except == null) {
            setAll(page, item);
            return;
        }
        List<Integer> list = Arrays.stream(except).boxed().toList();

        for (int i = 0; i < getInventoryHolder().getInventorySlots(); i++) {
            if (!(list.contains(i))) {
                set(page, item, i);
            }
        }
    }

    public void setAllUnusedExcept(int page, InventoryItem item, int... except) {
        if (except == null) {
            setAllUnused(page, item);
            return;
        }
        List<Integer> list = Arrays.stream(except).boxed().toList();

        for (int i = 0; i < getInventoryHolder().getInventorySlots(); i++) {
            if (!(list.contains(i))) {
                safeSet(page, item, i);
            }
        }
    }

    public void openInventory(int page, Player player) {
        openInventory(page, (HumanEntity) player);
    }

    public void openInventory(Player player) {
        openInventory(getCurrentPage(), player);
    }

    public void openInventory(int page, HumanEntity player) {
        getInventoryHolder().getActionManager().openInventory(page, player, this);
    }

    public void openInventory(HumanEntity player) {
        openInventory(getCurrentPage(), player);
    }

    public void openInventory(@NotNull Player player, Inventory inventory) {
        player.openInventory(inventory);
        getInventoryHolder().getCache().setActiveInventory(player.getUniqueId(), this);
    }

    public void replaceAndOpenBuiltInventory(int page, @NotNull Player player, OpMap<String, String> translations) {
        player.openInventory(replaceBuilt(page, translations));
        getInventoryHolder().getCache().setActiveInventory(player.getUniqueId(), this);
    }

    public void replaceAndOpenBuiltInventoryWithFunction(int page, @NotNull Player player, OpMap<String, Function<ReplacementInventoryImpl, Object>> translations) {
        player.openInventory(replaceBuiltWithFunction(page, translations));
        getInventoryHolder().getCache().setActiveInventory(player.getUniqueId(), this);
    }

    @SafeVarargs
    public final void replaceAndOpenBuiltInventory(int page, @NotNull Player player, Tuple<String, String>... translations) {
        player.openInventory(replaceBuilt(page, translations));
        getInventoryHolder().getCache().setActiveInventory(player.getUniqueId(), this);
    }

    public void build(int page) {
        getInventoryHolder().build(page);
    }

    public void build() {
        getInventoryHolder().build(0);
    }

    public void open(int page, Player player, OpMap<String, String> translations) {
        if (getInventoryHolder().isBuilt(page)) {
            replaceAndOpenBuiltInventory(page, player, translations);
        } else {
            replaceNotBuilt(page, translations);
            openInventory(page, player);
        }
    }

    @SafeVarargs
    public final void open(int page, Player player, Tuple<String, String>... translations) {
        open(page, player, VariableUtil.getMapFromTuples(translations));
    }

    @SafeVarargs
    public final void openWithFunction(int page, Player player, Tuple<String, Function<ReplacementInventoryImpl, Object>>... translations) {
        openWithFunction(page, player, VariableUtil.getMapFromTuples(translations));
    }

    public final void openWithFunction(int page, Player player, OpMap<String, Function<ReplacementInventoryImpl, Object>> translations) {
        if (saveDefaultTranslations) {
            this.defaultTranslations = translations;
        }
        if (getInventoryHolder().isBuilt(page)) {
            replaceAndOpenBuiltInventoryWithFunction(page, player, translations);
        } else {
            replaceNotBuiltWithFunction(page, translations);
            openInventory(page, player);
        }
    }

    public final void openWithFunctionDefault(int page, Player player) {
        openWithFunction(page, player, defaultTranslations);
    }

    public void openBestInventory(HumanEntity player) {
        openBestInventory((Player) player);
    }

    public void openBestInventory(Player player) {
        if (!defaultTranslations.keySet().isEmpty()) {
            openWithFunctionDefault(getCurrentPage(), player);
        } else {
            openInventory(getCurrentPage(), player);
        }
    }

    @SafeVarargs
    public final void openWithFunction(Player player, Tuple<String, Function<ReplacementInventoryImpl, Object>>... translations) {
        openWithFunction(getCurrentPage(), player, translations);
    }

    public final void openWithFunction(Player player, OpMap<String, Function<ReplacementInventoryImpl, Object>> translations) {
        openWithFunction(getCurrentPage(), player, translations);
    }

    public void open(Player player, OpMap<String, String> translations) {
        open(getCurrentPage(), player, translations);
    }

    @SafeVarargs
    public final void open(Player player, Tuple<String, String>... translations) {
        open(getCurrentPage(), player, translations);
    }

    public void open(int page, HumanEntity player, OpMap<String, String> translations) {
        open(page, (Player) player, translations);
    }

    @SafeVarargs
    public final void open(int page, HumanEntity player, Tuple<String, String>... translations) {
        open(page, (Player) player, translations);
    }

    @SafeVarargs
    public final void open(HumanEntity player, Tuple<String, String>... translations) {
        open(getCurrentPage(), player, translations);
    }

    public void open(HumanEntity player, OpMap<String, String> translations) {
        open(getCurrentPage(), player, translations);
    }

    public void setPattern(int page, @NotNull InventoryPattern inventoryPattern) {
        String[] patternWords = inventoryPattern.getPatternWords();
        for (int i = 0, patternWordsLength = patternWords.length; i < patternWordsLength; i++) {
            int finalI = i;
            inventoryPattern.getItem(patternWords[i]).ifPresent(inventoryItem -> set(page, inventoryItem, finalI));
        }
    }

    public String replaceRegex(String string, OpMap<String, String> replacements) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            String group = matcher.group();
            Optional<String> optional = replacements.getByKey(group);
            if (optional.isPresent()) {
                string = string.replaceAll(matcher.group(), optional.get());
            }
        }
        return string;
    }

    public @NotNull List<String> replaceRegex(@NotNull List<String> list, OpMap<String, String> replacements) {
        List<String> tempList = new ArrayList<>();
        for (String string : list) {
            tempList.add(replaceRegex(string, replacements));
        }
        return tempList;
    }

    public List<String> addRegex(String string) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(string);
        List<String> list = new ArrayList<>();

        while (matcher.find()) {
            String group = matcher.group();
            list.add(group);
        }
        return list;
    }

    public List<String> addRegex(List<String> list) {
        List<String> tempList = new ArrayList<>();
        if (list != null) {
            for (String string : list) {
                tempList.addAll(addRegex(string));
            }
        }
        return tempList;
    }

    public boolean isSaveDefaultTranslations() {
        return saveDefaultTranslations;
    }

    public void setSaveDefaultTranslations(boolean saveDefaultTranslations) {
        this.saveDefaultTranslations = saveDefaultTranslations;
    }
}