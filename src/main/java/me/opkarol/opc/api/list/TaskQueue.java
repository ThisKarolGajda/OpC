package me.opkarol.opc.api.list;

import me.opkarol.opc.api.tools.runnable.OpRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TaskQueue<A> {
    private final List<A> queue = new ArrayList<>();

    public void addFirst(A a) {
        queue.add(0, a);
    }

    public void addLast(A a) {
        queue.add(a);
    }

    public A peek() {
        return queue.get(0);
    }

    public A pop() {
        return queue.remove(0);
    }

    public A getLast() {
        return queue.get(size() - 1);
    }

    public A popLast() {
        return queue.remove(size() - 1);
    }

    public boolean hasFirst() {
        return size() > 0;
    }

    public void useWithDelay(long delay, @NotNull Consumer<A> onEachDelayConsumer, Runnable onEndRunnable) {
        new OpRunnable(r -> {
            if (!hasFirst()) {
                if (onEndRunnable != null) {
                    onEndRunnable.run();
                }
                r.cancel();
            } else {
                onEachDelayConsumer.accept(pop());
            }
        }).runTaskTimer(delay);
    }

    public void useWithDelay(long delay, Consumer<A> onEachDelayConsumer) {
        useWithDelay(delay, onEachDelayConsumer, null);
    }

    public int size() {
        return queue.size();
    }

    public List<A> getQueue() {
        return queue;
    }
}