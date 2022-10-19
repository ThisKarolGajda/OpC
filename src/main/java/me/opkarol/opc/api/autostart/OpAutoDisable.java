package me.opkarol.opc.api.autostart;

import me.opkarol.opc.api.plugin.OpPlugin;
import me.opkarol.opc.api.utils.VariableUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OpAutoDisable {
    private static OpAutoDisable autoStart;
    private static final List<Consumer<OpPlugin>> tasks = new ArrayList<>();

    public OpAutoDisable() {
        autoStart = this;
    }

    public static OpAutoDisable getInstance() {
        return VariableUtil.getOrDefault(autoStart, new OpAutoDisable());
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
