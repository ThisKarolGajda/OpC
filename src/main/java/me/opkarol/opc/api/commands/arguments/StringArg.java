package me.opkarol.opc.api.commands.arguments;

import org.jetbrains.annotations.Nullable;

public class StringArg extends OpCommandArg {

    public StringArg(String name) {
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
