package me.opkarol.opc.api.commands.arguments;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class OpCommandArg {
    private final String name;

    public OpCommandArg(String name) {
        this.name = name;
    }

    public OpCommandArg() {
        this.name = UUID.randomUUID().toString();
    }

    @Nullable
    public abstract Object getObject(Object object);

    public String getName() {
        return name;
    }
}
