package me.opkarol.opc.api.autostart;

import me.opkarol.opc.api.plugin.OpPlugin;
import me.opkarol.opc.api.utils.VariableUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OpAutoStart {
    private static OpAutoStart autoStart;
    private static final List<Consumer<OpPlugin>> tasks = new ArrayList<>();

    public OpAutoStart() {
        autoStart = this;
    }

    public static OpAutoStart getInstance() {
        return VariableUtil.getOrDefault(autoStart, new OpAutoStart());
    }

    public static void register(Consumer<OpPlugin> runnable) {
        tasks.add(runnable);
    }

    public static void activate(OpPlugin plugin) {
        for (Consumer<OpPlugin> consumer : tasks) {
            consumer.accept(plugin);
        }
    }
}
