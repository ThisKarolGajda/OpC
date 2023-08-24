package me.opkarol.opc.api.commands;

import me.opkarol.opc.api.commands.annotations.Default;
import me.opkarol.opc.api.commands.annotations.Subcommand;
import me.opkarol.opc.api.map.OpMap;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class Command extends BukkitCommand {
    private final Object classObject;
    private final Class<?> clazz;
    private final OpMap<String, Method> subcommands = new OpMap<>();
    private Method commandMethod;

    @Contract(pure = true)
    public Command(@NotNull Object classObject) {
        super(classObject.getClass().getAnnotation(me.opkarol.opc.api.commands.annotations.Command.class).value());
        this.classObject = classObject;
        this.clazz = classObject.getClass();

        // Set command default method
        Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(Default.class)).findAny().ifPresent(method -> {
            this.commandMethod = method;
        });

        // Set command subcommands
        for (Method method : Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Subcommand.class))
                .toList()) {
            Subcommand subcommand = method.getAnnotation(Subcommand.class);
            subcommands.set(subcommand.value(), method);
        }

    }

    public void register() {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            org.bukkit.command.Command command = commandMap.getCommand(this.getName());
            if (command == null || !command.isRegistered()) {
                commandMap.register(getName(), this);
            } else {
                throw new IllegalStateException("This command is already registered.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void unregister() {
        try {
            Field commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            Field knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            commandMap.setAccessible(true);
            knownCommands.setAccessible(true);
            ((Map<String, Command>) knownCommands.get(commandMap.get(Bukkit.getServer()))).remove(getName());
            this.unregister((CommandMap) commandMap.get(Bukkit.getServer()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        if (args.length == 0 && commandMethod != null) {
            try {
                commandMethod.invoke(classObject, sender);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return true;
        }

        if (args.length > 0) {
            Method subcommandMethod = subcommands.unsafeGet(String.join(" ", args));
            if (subcommandMethod != null) {
                try {
                    subcommandMethod.invoke(classObject, sender);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return subcommands.keySet().stream()
                    .map(string -> string.contains(" ") ? string.split(" ")[0] : string)
                    .filter(subcommand -> subcommand.startsWith(args[0]))
                    .collect(Collectors.toList());
        }

        if (args.length > 1) {
            return getAllSubcommandsForLength(args, args.length - 1);
        }

        return Collections.emptyList();
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Object getClassObject() {
        return classObject;
    }

    public Method getCommandMethod() {
        return commandMethod;
    }

    public List<String> getAllSubcommandsForLength(String[] args, int length) {
        List<String> list = new ArrayList<>();
        for (String subcommand : subcommands.keySet()) {
            String[] parts = subcommand.split(" ");
            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                // Check previous parts
                boolean arePreviousPartsEqual = true;
                if (i > 0) {
                    for (int j = i; j != 0; j--) {
                        if (!args[j - 1].equals(parts[j - 1])) {
                            arePreviousPartsEqual = false;
                            break;
                        }
                    }
                }

                if (!arePreviousPartsEqual) {
                    break;
                }

                if (i == length) {
                    list.add(part);
                }
            }
        }
        return list;
    }
}
