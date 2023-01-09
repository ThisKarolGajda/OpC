package me.opkarol.opc.api.command.arguments;

import me.opkarol.opc.api.command.types.IType;
import me.opkarol.opc.api.utils.StringUtil;
import org.jetbrains.annotations.Nullable;

public class DoubleArg <I extends IType> extends OpTypeArg<I> {
    public DoubleArg(I name) {
        super(name);
    }

    @Override
    public @Nullable Object getObject(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof String || object instanceof Integer || object instanceof Float) {
            return StringUtil.getDouble(object);
        }

        if (object instanceof Double) {
            return object;
        }

        return null;
    }
}
