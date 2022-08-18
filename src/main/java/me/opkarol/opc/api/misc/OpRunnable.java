package me.opkarol.opc.api.misc;

import me.opkarol.opc.OpAPI;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.Consumer;

public class OpRunnable extends BukkitRunnable implements Serializable {
    private Consumer<OpRunnable> consumer;
    private BukkitTask task;

    @Override
    public void run() {
        this.consumer.accept(this);
    }

    public OpRunnable(Consumer<OpRunnable> consumer) {
        this.consumer = consumer;
    }

    public OpRunnable set(Consumer<OpRunnable> consumer) {
        this.consumer = consumer;
        return this;
    }

    public static OpRunnable get(Consumer<OpRunnable> consumer) {
        return new OpRunnable(consumer);
    }

    public void cancel() {
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }

    @NotNull
    public synchronized OpRunnable runTaskLaterAsynchronously(long delay) throws IllegalArgumentException, IllegalStateException {
        this.task = super.runTaskLaterAsynchronously(OpAPI.getInstance(), delay);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTask() throws IllegalArgumentException, IllegalStateException {
        this.task = super.runTask(OpAPI.getInstance());
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskLater(long delay) throws IllegalArgumentException, IllegalStateException {
        this.task = super.runTaskLater(OpAPI.getInstance(), delay);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskTimerAsynchronously(long delay, long period) throws IllegalArgumentException, IllegalStateException {
        this.task = super.runTaskTimerAsynchronously(OpAPI.getInstance(), delay, period);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskTimerAsynchronously(long delay) throws IllegalArgumentException, IllegalStateException {
        this.task = super.runTaskTimerAsynchronously(OpAPI.getInstance(), delay, delay);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskTimer(long delay, long period) throws IllegalArgumentException, IllegalStateException {
        this.task = super.runTaskTimer(OpAPI.getInstance(), delay, period);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskTimer(long delay) throws IllegalArgumentException, IllegalStateException {
        this.task = super.runTaskTimer(OpAPI.getInstance(), delay, delay);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskAsynchronously() throws IllegalArgumentException, IllegalStateException {
        this.task = super.runTaskAsynchronously(OpAPI.getInstance());
        return this;
    }
}
