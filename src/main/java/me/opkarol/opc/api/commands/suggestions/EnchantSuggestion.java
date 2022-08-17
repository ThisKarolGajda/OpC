package me.opkarol.opc.api.commands.suggestions;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnchantSuggestion implements ISuggestion {
    private final List<String> enchantSuggestion = Arrays.stream(Enchantment.values())
            .map(Enchantment::getKey).map(NamespacedKey::getKey)
            .map(s -> s.replace("minecraft:", ""))
            .collect(Collectors.toList());

    @Override
    public List<String> getSuggestion() {
        return enchantSuggestion;
    }
}