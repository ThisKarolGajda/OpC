package me.opkarol.opc.api.command.suggestions;

import me.opkarol.opc.api.list.OpList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OnlinePlayerSuggestion implements ISuggestion {
    @Override
    public OpList<String> getSuggestion() {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(OpList.getCollector());
    }
}
