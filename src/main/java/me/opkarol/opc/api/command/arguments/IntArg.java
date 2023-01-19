package me.opkarol.opc.api.command.arguments;

import me.opkarol.opc.api.command.types.IType;
import me.opkarol.opc.api.utils.StringUtil;
import org.jetbrains.annotations.Nullable;

public class IntArg<I extends IType> extends OpTypeArg<I> {
    public IntArg(I name) {
        super(name);
    }

    @Override
    public @Nullable Object getObject(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof String || object instanceof Double || object instanceof Float) {
            return StringUtil.getInt(object);
        }

        if (object instanceof Integer) {
            return object;
        }

        return null;
    }
}
