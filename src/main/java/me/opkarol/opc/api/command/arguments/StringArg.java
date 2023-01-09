package me.opkarol.opc.api.command.arguments;

import me.opkarol.opc.api.command.types.IType;
import me.opkarol.opc.api.command.types.OP16;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public class StringArg<I extends IType> extends OpTypeArg<I> {

    public StringArg(I name) {
        super(name);
    }

    @SuppressWarnings("all")
    public StringArg(@Range(from = 0, to = 15) int i) {
        super((I) OP16.values()[i]);
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
