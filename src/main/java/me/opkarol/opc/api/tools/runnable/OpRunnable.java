package me.opkarol.opc.api.tools.runnable;

import me.opkarol.opc.OpAPI;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.Consumer;

public class OpRunnable implements Serializable {
    private int taskId;
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

    public void cancelTask() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    @NotNull
    public synchronized OpRunnable runTaskLaterAsynchronously(long delay) throws IllegalArgumentException, IllegalStateException {
        this.taskId = bukkitRunnable.runTaskLaterAsynchronously(OpAPI.getPlugin(), delay).getTaskId();
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTask() throws IllegalArgumentException, IllegalStateException {
        this.taskId = bukkitRunnable.runTask(OpAPI.getPlugin()).getTaskId();
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskLater(long delay) throws IllegalArgumentException, IllegalStateException {
        this.taskId = bukkitRunnable.runTaskLater(OpAPI.getPlugin(), delay).getTaskId();
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskTimerAsynchronously(long delay, long period) throws IllegalArgumentException, IllegalStateException {
        this.taskId = bukkitRunnable.runTaskTimerAsynchronously(OpAPI.getPlugin(), delay, period).getTaskId();
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskTimerAsynchronously(long delay) throws IllegalArgumentException, IllegalStateException {
        this.taskId = bukkitRunnable.runTaskTimerAsynchronously(OpAPI.getPlugin(), delay, delay).getTaskId();
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskTimer(long delay, long period) throws IllegalArgumentException, IllegalStateException {
        this.taskId = bukkitRunnable.runTaskTimer(OpAPI.getPlugin(), delay, period).getTaskId();
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskTimer(long delay) throws IllegalArgumentException, IllegalStateException {
        this.taskId = bukkitRunnable.runTaskTimer(OpAPI.getPlugin(), delay, delay).getTaskId();
        return this;
    }

    @NotNull
    public synchronized OpRunnable runTaskAsynchronously() throws IllegalArgumentException, IllegalStateException {
        this.taskId = bukkitRunnable.runTaskAsynchronously(OpAPI.getPlugin()).getTaskId();
        return this;
    }
}
