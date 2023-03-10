package me.opkarol.opc.api.wrappers;

import me.opkarol.opc.api.tools.runnable.OpRunnable;
import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class OpBossBar implements Serializable {
    private BossBar bossBar;
    private final String originalTitle;
    private String title;
    private BarStyle barStyle;
    private BarColor barColor;
    private OpRunnable runnable;

    public OpBossBar(String title) {
        this.originalTitle = title;
        this.title = title;
    }

    public OpBossBar() {
        this.originalTitle = null;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public OpBossBar build() {
        bossBar = Bukkit.createBossBar(getTitle(), getBarColor(), getBarStyle());
        return this;
    }

    public OpBossBar setTitle(String title) {
        this.title = title;
        return build();
    }

    public OpBossBar setBarColor(BarColor barColor) {
        this.barColor = barColor;
        return this;
    }

    public OpBossBar setBarStyle(BarStyle barStyle) {
        this.barStyle = barStyle;
        return this;
    }

    public OpBossBar setBossBar(BossBar bossBar) {
        this.bossBar = bossBar;
        return this;
    }

    public OpBossBar setVisible(boolean visible) {
        if (bossBar != null) {
            bossBar.setVisible(visible);
        }
        return this;
    }

    public OpBossBar display(Player player) {
        bossBar.addPlayer(player);
        setVisible(true);
        return this;
    }

    public OpBossBar removeDisplay(Player player) {
        bossBar.removePlayer(player);
        return this;
    }

    public OpBossBar loopAndDisplay(int time, int speed, Consumer<OpBossBar> onEndConsumer) {
        setVisible(true);
        AtomicReference<Double> current = new AtomicReference<>((double) time);
        setRunnable(new OpRunnable((r) -> {
            current.set(current.get() - 1);
            if (current.get() < 1) {
                removeAllPlayers();
                setVisible(false);
                onEndConsumer.accept(this);
                r.cancelTask();
            }
            setProgress(current.get() * speed / time);
        }).runTaskTimerAsynchronously(0, 20/speed));
        return this;
    }

    private OpBossBar setProgress(double v) {
        bossBar.setProgress(v);
        return this;
    }

    public BarColor getBarColor() {
        return barColor == null ? BarColor.WHITE : barColor;
    }

    public BarStyle getBarStyle() {
        return barStyle == null ? BarStyle.SOLID : barStyle;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public String getTitle() {
        return FormatUtils.formatMessage(title);
    }

    public OpRunnable getRunnable() {
        return runnable;
    }

    public void setRunnable(OpRunnable runnable) {
        this.runnable = runnable;
    }

    public OpBossBar removeAllPlayers() {
        bossBar.getPlayers().forEach(this::removeDisplay);
        return this;
    }
}