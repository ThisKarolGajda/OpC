package me.opkarol.opc.api.autostart;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.plugin.OpPlugin;
import me.opkarol.opc.api.utils.VariableUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OpAutoStart {
    private static OpAutoStart autoStart;
    private static final List<Consumer<OpPlugin>> tasks = new ArrayList<>();
    private static boolean activated;

    public OpAutoStart() {
        autoStart = this;
    }

    public static OpAutoStart getInstance() {
        return VariableUtil.getOrDefault(autoStart, new OpAutoStart());
    }

    public static void register(Consumer<OpPlugin> runnable) {
        if (activated) {
            runnable.accept(OpAPI.getInstance());
        } else {
            tasks.add(runnable);
        }
    }

    public static void activate(OpPlugin plugin) {
        activated = true;
        for (Consumer<OpPlugin> consumer : tasks) {
            consumer.accept(plugin);
        }
    }

    @SafeVarargs
    public static void register(Consumer<OpPlugin> @NotNull ... consumers) {
        for (Consumer<OpPlugin> pluginConsumer : consumers) {
            register(pluginConsumer);
        }
    }

    public static void registerRunnable(Runnable @NotNull ... consumers) {
        for (Runnable pluginConsumer : consumers) {
            register(plugin -> pluginConsumer.run());
        }
    }

}
