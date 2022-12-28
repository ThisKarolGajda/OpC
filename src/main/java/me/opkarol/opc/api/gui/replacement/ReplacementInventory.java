package me.opkarol.opc.api.gui.replacement;

import me.opkarol.opc.api.map.OpMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReplacementInventory {
    private final OpMap<Integer, OpMap<String, List<Integer>>> replacements = new OpMap<>();

    public OpMap<Integer, OpMap<String, List<Integer>>> getReplacements() {
        return replacements;
    }

    public Optional<List<Integer>> getReplacements(int page, String string) {
        assureMapExistence(page);
        return replacements.getMap().get(page).getByKey(string);
    }

    public void addReplacement(int page, String string, int integer) {
        assureMapExistence(page);
        replacements.getMap().get(page).set(string, getFixedList(page, string, integer));
    }

    public void addReplacements(int page, String string, int... integer) {
        assureMapExistence(page);
        replacements.getMap().get(page).set(string, getFixedList(page, string, integer));
    }

    private @NotNull List<Integer> getFixedList(int page, String string, int integer) {
        List<Integer> list = getReplacements(page, string).orElse(new ArrayList<>());
        list.add(integer);
        return list;
    }

    public List<Integer> getFixedList(int page, String string, int... integers) {
        List<Integer> list = getReplacements(page, string).orElse(new ArrayList<>());
        if (integers == null) {
            return list;
        }

        for (int i : integers) {
            list.add(i);
        }
        return list;
    }

    public void assureMapExistence(int page) {
        if (!replacements.containsKey(page)) {
            replacements.set(page, new OpMap<>());
        }
    }
}
