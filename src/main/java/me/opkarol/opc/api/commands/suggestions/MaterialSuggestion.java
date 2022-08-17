package me.opkarol.opc.api.commands.suggestions;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MaterialSuggestion implements ISuggestion {
    private final List<String> materialSuggestion = Arrays.stream(Material.values())
            .map(Material::toString)
            .collect(Collectors.toList());

    @Override
    public List<String> getSuggestion() {
        return materialSuggestion;
    }
}
