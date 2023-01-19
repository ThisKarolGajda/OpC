package me.opkarol.opc.api.misc.opobjects;

import me.opkarol.opc.api.tools.runnable.OpRunnable;
import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public class OpBossBar implements Serializable {
    private final BossBar bossBar;
    private final String originalTitle;
    private OpRunnable task;

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

    public void call(int time, int speed) {
        setVisible(true);
        task = new OpRunnable(r -> {
            double t = time;
            t = t - 1d / speed;
            if (t < 1) {
                removePlayers();
                r.cancel();
            }
            bossBar.setProgress(t / time);
            setTitle(originalTitle.replace("%time%", String.valueOf((int) t)));
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