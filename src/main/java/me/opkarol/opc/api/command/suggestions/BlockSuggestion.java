package me.opkarol.opc.api.command.suggestions;

import me.opkarol.opc.api.list.OpList;
import org.bukkit.Material;

import java.util.Arrays;

public class BlockSuggestion implements ISuggestion {
    private final OpList<String> blockSuggestion = Arrays.stream(Material.values())
            .filter(Material::isBlock)
            .map(Material::toString)
            .collect(OpList.getCollector());

    @Override
    public OpList<String> getSuggestion() {
        return blockSuggestion;
    }
}
