package me.opkarol.opc.api.tools.runnable;

import me.opkarol.opc.OpAPI;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.Consumer;

public class OpRunnable extends BukkitRunnable implements Serializable {
    private Consumer<OpRunnable> consumer;
    private BukkitTask task;

    public OpRunnable(Consumer<OpRunnable> consumer) {
        this.consumer = consumer;
    }

    public OpRunnable(Runnable runnable) {
        this.consumer = (r) -> {
            runnable.run();
        };
    }

    @Contract("_ -> new")
    public static @NotNull OpRunnable get(Consumer<OpRunnable> consumer) {
        return new OpRunnable(consumer);
    }

    @Override
    public void run() {
        this.consumer.accept(this);
    }

    public OpRunnable set(Consumer<OpRunnable> consumer) {
        this.consumer = consumer;
        return this;
    }

    public boolean cancelTask() {
        if (task != null && !task.isCancelled()) {
            task.cancel();
            return true;
        }
        return false;
    }

    @NotNull
    public synchronized OpRunnable runTaskLaterAsynchronously(long delay) throws IllegalArgumentException, IllegalStateException {
        this.task = super.runTaskLaterAsynchronously(OpAPI.getPlugin(), delay);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTask() throws IllegalArgumentException, IllegalStateException {
        this.task = super.runTask(OpAPI.getPlugin());
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskLater(long delay) throws IllegalArgumentException, IllegalStateException {
        this.task = super.runTaskLater(OpAPI.getPlugin(), delay);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskTimerAsynchronously(long delay, long period) throws IllegalArgumentException, IllegalStateException {
        this.task = super.runTaskTimerAsynchronously(OpAPI.getPlugin(), delay, period);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskTimerAsynchronously(long delay) throws IllegalArgumentException, IllegalStateException {
        this.task = super.runTaskTimerAsynchronously(OpAPI.getPlugin(), delay, delay);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskTimer(long delay, long period) throws IllegalArgumentException, IllegalStateException {
        this.task = super.runTaskTimer(OpAPI.getPlugin(), delay, period);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskTimer(long delay) throws IllegalArgumentException, IllegalStateException {
        this.task = super.runTaskTimer(OpAPI.getPlugin(), delay, delay);
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskAsynchronously() throws IllegalArgumentException, IllegalStateException {
        this.task = super.runTaskAsynchronously(OpAPI.getPlugin());
        return this;
    }
}
