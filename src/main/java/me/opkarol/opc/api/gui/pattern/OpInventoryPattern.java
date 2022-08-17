package me.opkarol.opc.api.gui.pattern;

import me.opkarol.opc.api.item.OpInventoryItem;
import me.opkarol.opc.api.map.OpMap;
import org.bukkit.Material;

public class OpInventoryPattern {
    private final String pattern;
    private final OpMap<String, OpInventoryItem> map = new OpMap<>();

    public OpInventoryPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public String[] getSplitPattern() {
        return pattern.split(" ");
    }

    public OpMap<String, OpInventoryItem> getMap() {
        return map;
    }

    public OpInventoryItem getItem(String pattern, OpInventoryItem defaultValue) {
        return getMap().getOrDefault(pattern, defaultValue);
    }

    public OpInventoryItem getItem(String pattern, Material defaultValue) {
        return getItem(pattern, new OpInventoryItem(defaultValue));
    }

    public OpInventoryPattern addPatternValue(String patternName, OpInventoryItem item) {
        map.put(patternName, item);
        return this;
    }

    public OpInventoryPattern addPatternValue(String patternName, Material material) {
        map.put(patternName, new OpInventoryItem(material, patternName));
        return this;
    }
}
