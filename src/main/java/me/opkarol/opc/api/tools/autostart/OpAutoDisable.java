package me.opkarol.opc.api.tools.autostart;

import me.opkarol.opc.api.utils.VariableUtil;

import java.util.ArrayList;
import java.util.List;

public class OpAutoDisable {
    private static final List<IDisable> tasks = new ArrayList<>();
    private static OpAutoDisable autoStart;

    public OpAutoDisable() {
        autoStart = this;
    }

    public static OpAutoDisable getInstance() {
        return VariableUtil.getOrDefault(autoStart, new OpAutoDisable());
    }

    public static void add(IDisable runnable) {
        tasks.add(runnable);
    }

    public static void registerDisable() {
        for (IDisable iDisable : tasks) {
            iDisable.onDisable();
        }
    }
}
