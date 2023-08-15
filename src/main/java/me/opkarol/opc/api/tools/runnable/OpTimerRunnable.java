package me.opkarol.opc.api.tools.runnable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class OpTimerRunnable {

    public synchronized void runTaskTimesDown(BiConsumer<OpRunnable, Integer> onEachConsumer, Consumer<OpRunnable> onEndConsumer, int times) {
        final int[] i = {times};
        new OpRunnable(r -> {
            if (i[0] < 1) {
                onEndConsumer.accept(r);
                r.cancelTask();
            }
            onEachConsumer.accept(r, i[0]);
            i[0]--;
        }).runTaskTimer(0, 20);
    }

    public synchronized void runTaskTimesUp(BiConsumer<OpRunnable, Integer> onEachConsumer, Consumer<OpRunnable> onEndConsumer, int times) {
        final int[] i = {1};
        new OpRunnable(r -> {
            if (i[0] >= times) {
                onEndConsumer.accept(r);
                r.cancelTask();
            }
            onEachConsumer.accept(r, i[0]);
            i[0]++;
        }).runTaskTimer(0, 20);
    }

    public OpRunnable runTaskTimesDownAsynchronously(BiConsumer<OpRunnable, Integer> onEachConsumer, Consumer<OpRunnable> onEndConsumer, int times) {
        final int[] i = {times};
        return new OpRunnable(r -> {
            if (i[0] < 1) {
                onEndConsumer.accept(r);
                r.cancelTask();
            }
            onEachConsumer.accept(r, i[0]);
            i[0]--;
        }).runTaskTimerAsynchronously(0, 20);
    }

    public OpRunnable runTaskTimesDownAsynchronously(BiConsumer<OpRunnable, Integer> onEachConsumer, int times) {
        final int[] i = {times};
        return new OpRunnable(r -> {
            if (i[0] < 1) {
                r.cancelTask();
            }
            onEachConsumer.accept(r, i[0]);
            i[0]--;
        }).runTaskTimerAsynchronously(0, 20);
    }

    public synchronized void runTaskTimesUpAsynchronously(BiConsumer<OpRunnable, Integer> onEachConsumer, Consumer<OpRunnable> onEndConsumer, int times) {
        final int[] i = {1};
        new OpRunnable(r -> {
            if (i[0] > times) {
                onEndConsumer.accept(r);
                r.cancelTask();
            }
            onEachConsumer.accept(r, i[0]);
            i[0]++;
        }).runTaskTimerAsynchronously(0, 20);
    }

    public synchronized void runTaskTimesUpAsynchronously(BiConsumer<OpRunnable, Integer> onEachConsumer, int times) {
        final int[] i = {1};
        new OpRunnable(r -> {
            if (i[0] > times) {
                r.cancelTask();
            }
            onEachConsumer.accept(r, i[0]);
            i[0]++;
        }).runTaskTimerAsynchronously(0, 20);
    }
}
