package me.opkarol.opc.api.command.arguments;

import me.opkarol.opc.api.command.types.IType;
import org.jetbrains.annotations.Nullable;

public abstract class OpTypeArg<I extends IType> {
    private final I type;

    public OpTypeArg(I type) {
        this.type = type;
    }

    @Nullable
    public abstract Object getObject(Object object);

    public I getName() {
        return type;
    }
}
