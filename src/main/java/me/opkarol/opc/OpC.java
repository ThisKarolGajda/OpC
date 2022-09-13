package me.opkarol.opc;

import me.opkarol.opc.api.plugin.OpPlugin;

public class OpC extends OpPlugin {

    @Override
    public void enable() {
        OpAPI.init(this);

    }

    @Override
    public void disable() {
        
    }
}
