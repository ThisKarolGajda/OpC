package me.opkarol.opc.api.commands.arguments;

import me.opkarol.opc.api.utils.StringUtil;
import org.jetbrains.annotations.Nullable;

public class FloatArg extends OpCommandArg {
    public FloatArg(String name) {
        super(name);
    }

    @Override
    public @Nullable Object getObject(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof String || object instanceof Double || object instanceof Integer) {
            return StringUtil.getFloat(object);
        }

        if (object instanceof Float) {
            return object;
        }

        return null;
    }
}
