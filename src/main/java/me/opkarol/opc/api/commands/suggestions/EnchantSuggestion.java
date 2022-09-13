package me.opkarol.opc.api.commands.suggestions;

import me.opkarol.opc.api.list.OpList;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;

public class EnchantSuggestion implements ISuggestion {
    private final OpList<String> enchantSuggestion = Arrays.stream(Enchantment.values())
            .map(Enchantment::getKey).map(NamespacedKey::getKey)
            .map(s -> s.replace("minecraft:", ""))
            .collect(OpList.getCollector());

    @Override
    public OpList<String> getSuggestion() {
        return enchantSuggestion;
    }
}