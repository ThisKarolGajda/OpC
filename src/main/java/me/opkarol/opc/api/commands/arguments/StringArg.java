package me.opkarol.opc.api.commands.arguments;

import me.opkarol.opc.api.commands.types.IType;
import org.jetbrains.annotations.Nullable;

public class StringArg<I extends IType> extends OpTypeArg<I> {

    public StringArg(I name) {
        super(name);
    }

    @Override
    public @Nullable Object getObject(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof String) {
            return object;
        }

        return null;
    }
}
