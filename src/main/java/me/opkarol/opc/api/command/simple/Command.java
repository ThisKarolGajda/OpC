package me.opkarol.opc.api.command.simple;

import me.opkarol.opc.api.list.OpList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Command extends BukkitCommand implements SubCommand {
    private final List<SubCommand> subcommands = new ArrayList<>();
    private final ArgumentMatcher argumentMatcher = new StartingWithStringArgumentMatcher();

    public Command(@NotNull String name) {
        super(name);
        register();
    }

    public Command(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
        register();
    }

    private static List<String> getMatchingStrings(List<String> tabCompletions, String arg, ArgumentMatcher argumentMatcher) {
        if (tabCompletions == null || arg == null) {
            return new ArrayList<>();
        }

        List<String> result = argumentMatcher.filter(tabCompletions, arg);
        Collections.sort(result);
        return result;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            if (hasPermissionOnSubCommand(this, sender)) {
                execute(sender, OpList.asList(args));
                return true;
            }
            return false;
        }

        SubCommand chosenSubcommand = subcommands.stream()
                .filter(sc -> sc.getName().equalsIgnoreCase(args[0]))
                .findAny().orElse(this);

        // If the sender has permission it executes the selected subcommand
        if (hasPermissionOnSubCommand(chosenSubcommand, sender)) {
            chosenSubcommand.execute(sender, OpList.asList(args));
            return true;
        }

        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) {
        if (args.length == 0)
            return new ArrayList<>();

        // Default tab completion for any subcommands
        if (args.length == 1 && !hasCustomTabCompletion()) {
            List<String> subcommandsNames = subcommands.stream()
                    .filter(subCommand -> hasPermissionOnSubCommand(subCommand, sender))
                    .map(SubCommand::getName)
                    .collect(Collectors.toList());
            return getMatchingStrings(subcommandsNames, args[args.length - 1], argumentMatcher);
        }

        // Gets the mainSubcommand by the name in first argument.
        SubCommand chosenSubcommand = subcommands.stream()
                .filter(sc -> sc.getName().equalsIgnoreCase(args[0]))
                .findAny().orElse(this);

        // Gets the tabCompletion from the subCommand.
        List<String> tabCompleteSubCommand = chosenSubcommand.tabComplete(args.length, args);

        return getMatchingStrings(tabCompleteSubCommand, args[args.length - 1], argumentMatcher);
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

    @SuppressWarnings({"unchecked", "deprecated"})
    public void unregister() {
        try {
            Field commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            Field knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            commandMap.setAccessible(true);
            knownCommands.setAccessible(true);
            ((Map<String, org.bukkit.command.Command>) knownCommands.get(commandMap.get(Bukkit.getServer()))).remove(getName());
            this.unregister((CommandMap) commandMap.get(Bukkit.getServer()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasPermissionOnSubCommand(SubCommand subcommand, CommandSender sender) {
        if (sender == null || subcommand == null) {
            return false;
        }

        if (subcommand.requiresPermission()) {
            if (subcommand.requiredPermission() == null) {
                return false;
            }
            return sender.hasPermission(subcommand.requiredPermission());
        }

        return true;
    }

    public void add(SubCommand subcommand) {
        subcommands.add(subcommand);
    }
}