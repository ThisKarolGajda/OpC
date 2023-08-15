package me.opkarol.opc.api.tools.runnable;

import me.opkarol.opc.OpAPI;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.Consumer;

public class OpRunnable implements Serializable {
    private BukkitTask task;
    private final BukkitRunnable bukkitRunnable;

    public OpRunnable(Consumer<OpRunnable> consumer) {
        OpRunnable runnable = this;

        bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                consumer.accept(runnable);
            }
        };
    }

    public OpRunnable(Runnable runnable) {
        this(runnable1 -> runnable.run());
    }

    @Contract("_ -> new")
    public static @NotNull OpRunnable get(Consumer<OpRunnable> consumer) {
        return new OpRunnable(consumer);
    }

    public OpRunnable set(Consumer<OpRunnable> consumer) {
        return this;
    }

    public boolean cancelTask() {
        if (this.task != null && !this.task.isCancelled()) {
            this.task.cancel();
            return true;
        }
        return false;
    }

    @NotNull
    public synchronized OpRunnable runTaskLaterAsynchronously(long delay) throws IllegalArgumentException, IllegalStateException {
        this.task = bukkitRunnable.runTaskLaterAsynchronously(OpAPI.getPlugin(), delay);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTask() throws IllegalArgumentException, IllegalStateException {
        this.task = bukkitRunnable.runTask(OpAPI.getPlugin());
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskLater(long delay) throws IllegalArgumentException, IllegalStateException {
        this.task = bukkitRunnable.runTaskLater(OpAPI.getPlugin(), delay);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskTimerAsynchronously(long delay, long period) throws IllegalArgumentException, IllegalStateException {
        this.task = bukkitRunnable.runTaskTimerAsynchronously(OpAPI.getPlugin(), delay, period);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskTimerAsynchronously(long delay) throws IllegalArgumentException, IllegalStateException {
        this.task = bukkitRunnable.runTaskTimerAsynchronously(OpAPI.getPlugin(), delay, delay);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskTimer(long delay, long period) throws IllegalArgumentException, IllegalStateException {
        this.task = bukkitRunnable.runTaskTimer(OpAPI.getPlugin(), delay, period);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskTimer(long delay) throws IllegalArgumentException, IllegalStateException {
        this.task = bukkitRunnable.runTaskTimer(OpAPI.getPlugin(), delay, delay);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskAsynchronously() throws IllegalArgumentException, IllegalStateException {
        this.task = bukkitRunnable.runTaskAsynchronously(OpAPI.getPlugin());
        return this;
    }
}
