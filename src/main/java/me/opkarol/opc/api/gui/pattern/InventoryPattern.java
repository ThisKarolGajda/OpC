package me.opkarol.opc.api.gui.pattern;

import me.opkarol.opc.api.gui.items.InventoryItem;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.misc.Tuple;
import me.opkarol.opc.api.utils.VariableUtil;

import java.util.List;
import java.util.Optional;

public class InventoryPattern {
    private final String patternWords;
    private final OpMap<String, InventoryItem> map;

    @SafeVarargs
    public InventoryPattern(String patternWords, Tuple<String, InventoryItem>... tuples) {
        this.patternWords = patternWords;
        this.map = VariableUtil.getMapFromTuples(tuples);
    }

    public InventoryPattern(String patternWords, OpMap<String, InventoryItem> map) {
        this.patternWords = patternWords;
        this.map = map;
    }

    public InventoryPattern(String patternWords) {
        this.patternWords = patternWords;
        this.map = new OpMap<>();
    }

    public String getPattern() {
        return patternWords;
    }

    public String[] getPatternWords() {
        return patternWords.split(" ");
    }

    public OpMap<String, InventoryItem> getMap() {
        return map;
    }

    public void setItem(String pattern, InventoryItem item) {
        map.set(pattern, item);
    }

    public Optional<InventoryItem> getItem(String pattern) {
        return map.getByKey(pattern);
    }

    public void removeItem(String pattern) {
        map.remove(pattern);
    }

    public List<String> getMapWords() {
        return map.keySet().stream().toList();
    }
}
