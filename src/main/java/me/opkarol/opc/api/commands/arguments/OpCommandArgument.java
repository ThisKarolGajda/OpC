package me.opkarol.opc.api.commands.arguments;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.commands.types.IType;
import me.opkarol.opc.api.map.OpLinkedMap;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public record OpCommandArgument<I extends IType>(
        OpLinkedMap<IType, OpTypeArg<I>> args,
        String[] strings,
        int startPosition) {

    public OpTypeArg<I> getArg(IType name) {
        return args.getOrDefault(name, null);
    }

    public Object get(IType name, int arg) {
        return getArg(name).getObject(strings[arg]);
    }

    public int getLength() {
        return strings.length;
    }

    public Object get(IType name) {
        int i = 0;
        for (IType s : args.keySet()) {
            if (name.equals(s)) {
                if (startPosition == -1) {
                    return get(s, i);
                }
                return get(s, i + 1);
            }
            i++;
        }
        return name;
    }

    public String getString(@NotNull IType name) {
        return String.valueOf(get(name));
    }
}
