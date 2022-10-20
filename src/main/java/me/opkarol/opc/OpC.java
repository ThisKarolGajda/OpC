package me.opkarol.opc;

import me.opkarol.opc.api.plugin.OpPlugin;

public class OpC extends OpPlugin {

    @Override
    public void enable() {
        // try {
        //     new TestDatabase(new SingleDatabase<>(new OpMConnection("jdbc:mysql://localhost:3306/test", "opkar", "password"), new TestObject(1)));
        // } catch (InstantiationException e) {
        //     e.printStackTrace();
        // }
    }
}
