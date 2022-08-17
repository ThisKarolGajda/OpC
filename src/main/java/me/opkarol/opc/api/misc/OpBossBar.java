package me.opkarol.opc.api.misc;

import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OpBossBar {
    private final BossBar bossBar;
    private OpRunnable task;
    private final String originalTitle;

    public OpBossBar(String title, BarColor color) {
        this.bossBar = Bukkit.createBossBar(FormatUtils.formatMessage(title), color, BarStyle.SOLID);
        this.originalTitle = title;
        setVisible(true);
    }

    public boolean hasPlayer(Player player) {
        return bossBar.getPlayers().stream().anyMatch(player1 -> player1.getUniqueId().equals(player.getUniqueId()));
    }

    public void addPlayers(@NotNull List<Player> players) {
        players.forEach(this::addPlayer);
    }

    public void addPlayer(Player player) {
        if (getPlayers() != null && hasPlayer(player)) {
            return;
        }
        bossBar.addPlayer(player);
    }

    public void removePlayer(Player player) {
        bossBar.removePlayer(player);
    }

    public void removePlayers() {
        bossBar.removeAll();
        setVisible(false);
    }

    public void setVisible(boolean b) {
        bossBar.setVisible(b);
    }

    public List<Player> getPlayers() {
        return bossBar.getPlayers();
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public void call(int time) {
        setVisible(true);
        int speed = 1;
        task = new OpRunnable(r -> {
            double d = time;
            d = d - 1d / speed;
            if (d < 1) {
                removePlayers();
                r.cancel();
            }
            bossBar.setProgress(d / time);
            setTitle(originalTitle.replace("%time%", String.valueOf((int) d)));
        }).runTaskTimerAsynchronously(20L / speed);
    }

    public synchronized void stop() {
        task.cancel();
        removePlayers();
    }

    public void setTitle(String s) {
        bossBar.setTitle(FormatUtils.formatMessage(s));
    }

    public @NotNull OpRunnable getTask() {
        return task;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }
}