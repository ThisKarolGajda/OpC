package me.opkarol.opc.api.commands.suggestions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class OnlinePlayerSuggestion implements ISuggestion {
    @Override
    public List<String> getSuggestion() {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }
}
