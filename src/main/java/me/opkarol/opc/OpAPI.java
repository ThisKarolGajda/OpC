package me.opkarol.opc;

import me.opkarol.opc.api.plugin.OpPlugin;

public class OpAPI {
    private static OpPlugin plugin;

    public static OpPlugin getInstance() {
        return plugin;
    }

    public static void init(OpPlugin plugin) {
        OpAPI.plugin = plugin;
    }
}