package me.opkarol.opc.api.command.suggestions;

import me.opkarol.opc.api.list.OpList;
import org.bukkit.Material;

import java.util.Arrays;

public class MaterialSuggestion implements ISuggestion {
    private final OpList<String> materialSuggestion = Arrays.stream(Material.values())
            .map(Material::toString)
            .collect(OpList.getCollector());

    @Override
    public OpList<String> getSuggestion() {
        return materialSuggestion;
    }
}
