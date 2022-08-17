package me.opkarol.opc.api.commands.arguments;

import me.opkarol.opc.api.utils.StringUtil;
import org.jetbrains.annotations.Nullable;

public class IntArg extends OpCommandArg {
    public IntArg(String name) {
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
