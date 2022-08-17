package me.opkarol.opc.api.commands.arguments;

import me.opkarol.opc.api.utils.StringUtil;
import org.jetbrains.annotations.Nullable;

public class DoubleArg extends OpCommandArg {
    public DoubleArg(String name) {
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
