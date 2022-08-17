package me.opkarol.opc.api.commands.arguments;

import me.opkarol.opc.api.map.OpLinkedMap;
import org.bukkit.Bukkit;

public record OpCommandArgument(
        OpLinkedMap<String, OpCommandArg> args,
        String[] strings,
        int startPosition) {

    public OpCommandArg getArg(String name) {
        return args.getOrDefault(name, null);
    }

    public Object get(String name, int arg) {
        return getArg(name).getObject(strings[arg]);
    }

    public int getLength() {
        return strings.length;
    }

    public Object get(String name) {
        int i = 0;
        for (String s : args.keySet()) {
            if (s.equals(name)) {
                if (startPosition == -1) {
                    return get(name, i);
                }
                return get(name, i + 1);
            }
            i++;
        }
        return name;
    }
}
