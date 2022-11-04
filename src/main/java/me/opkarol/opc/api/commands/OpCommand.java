package me.opkarol.opc.api.commands;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.commands.arguments.OpCommandArgument;
import me.opkarol.opc.api.commands.arguments.OpTypeArg;
import me.opkarol.opc.api.commands.suggestions.OpCommandSuggestion;
import me.opkarol.opc.api.commands.suggestions.OpSimpleSuggestion;
import me.opkarol.opc.api.commands.suggestions.StaticSuggestions;
import me.opkarol.opc.api.commands.types.IType;
import me.opkarol.opc.api.list.OpList;
import me.opkarol.opc.api.map.OpLinkedMap;
import me.opkarol.opc.api.utils.StringUtil;
import me.opkarol.opc.api.utils.VariableUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class OpCommand extends BukkitCommand {
    private boolean isMain;
    private OpList<Integer> argsNumber = OpList.asList(1);
    private final String name;
    private final OpList<OpCommand> list = new OpList<>();
    private final OpLinkedMap<IType, OpTypeArg<?>> args = new OpLinkedMap<>();
    private BiConsumer<OpCommandSender, OpCommandArgument<?>> biConsumer;
    private final OpList<OpCommandSuggestion> suggestions = new OpList<>();
    private final OpList<OpSimpleSuggestion> simpleSuggestions = new OpList<>();
    private boolean hasToBePlayer, removeDefaultCommandSuggestion;
    private OpCommandPermission seeTabComplete = new OpCommandPermission(null, null, OpCommandPermission.PERMISSION_TYPE.SEE_TAB_COMPLETE), useCommand = new OpCommandPermission(null, null, OpCommandPermission.PERMISSION_TYPE.USE_COMMAND);
    private OpTypeArg<?> globalCommandArg;
    private OpSimpleSuggestion globalSimpleSuggestion;
    private OpCommandSuggestion globalCommandSuggestion;

    public OpCommand(@NotNull String name) {
        super(name);
        this.name = name;
        this.list.add(this);
        this.isMain = true;
        addArgNumber();
    }

    public OpCommand addCommand(@NotNull OpCommand subCommand) {
        this.list.add(subCommand);
        subCommand.isMain = false;
        subCommand.addArgNumber();
        if (globalCommandArg != null) {
            subCommand.addArg(globalCommandArg);
        }
        if (globalSimpleSuggestion != null) {
            subCommand.addSuggestion(globalSimpleSuggestion);
        }
        if (globalCommandSuggestion != null) {
            subCommand.addSuggestion(globalCommandSuggestion);
        }
        return this;
    }

    public OpCommand addCommandWithoutGlobals(@NotNull OpCommand subCommand) {
        this.list.add(subCommand);
        subCommand.isMain = false;
        subCommand.addArgNumber();
        return this;
    }

    public <I extends IType> OpCommand setGlobalArgSuggestion(OpTypeArg<I> arg, String suggestion) {
        return setGlobalCommandArg(arg).setGlobalSimpleSuggestion(new OpSimpleSuggestion(suggestion));
    }

    public <I extends IType> OpCommand setGlobalArgSuggestion(OpTypeArg<I> arg, OpCommandSuggestion suggestion) {
        return setGlobalCommandArg(arg).setGlobalCommandSuggestion(suggestion);
    }

    public <I extends IType> OpCommand setGlobalArgSuggestion(OpTypeArg<I> arg, OpSimpleSuggestion suggestion) {
        return setGlobalCommandArg(arg).setGlobalSimpleSuggestion(suggestion);
    }

    public <I extends IType> OpCommand addArgSuggestion(OpTypeArg<I> arg, OpSimpleSuggestion suggestion) {
        return addArg(arg).addSuggestion(suggestion);
    }

    public <I extends IType> OpCommand addArgSuggestion(OpTypeArg<I> arg, OpCommandSuggestion suggestion) {
        return addArg(arg).addSuggestion(suggestion);
    }

    public <I extends IType> OpCommand addArgSuggestion(OpTypeArg<I> arg, StaticSuggestions.SUGGESTION_TYPE suggestion) {
        return addArg(arg).addSuggestion(suggestion);
    }

    public <I extends IType> OpCommand addArgSuggestion(OpTypeArg<I> arg, String suggestion) {
        return addArg(arg).addSuggestion(suggestion);
    }

    public <I extends IType> OpCommand addArgSuggestion(OpTypeArg<I> arg, OpList<String> suggestion) {
        return addArg(arg).addSuggestion(suggestion);
    }

    public OpCommand addSuggestion(OpCommandSuggestion suggestion) {
        suggestions.add(suggestion);
        return addArgNumber();
    }

    public OpCommand addSuggestion(OpSimpleSuggestion suggestion) {
        simpleSuggestions.add(suggestion);
        return addArgNumber();
    }

    public OpCommand addSuggestion(StaticSuggestions.SUGGESTION_TYPE suggestion) {
        return addSuggestion(StaticSuggestions.get(suggestion));
    }

    public OpCommand addSuggestion(String suggestion) {
        return addSuggestion(new OpSimpleSuggestion(suggestion));
    }

    public OpCommand addSuggestion(OpList<String> suggestions) {
        return addSuggestion(new OpSimpleSuggestion(suggestions));
    }

    public OpList<OpCommand> getCommands() {
        return list;
    }

    public <I extends IType> OpCommand addArg(OpTypeArg<I> arg) {
        args.set(arg.getName(), arg);
        return this;
    }

    public List<OpCommand> getCommandsForArg(int arg) {
        return list.stream().filter(command -> command.getArgsNumber().stream().anyMatch(integer -> integer.equals(arg - 1))).toList();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if (hasToBePlayer && !(sender instanceof Player)) {
            return false;
        }
        if (args.length > 1) {
            for (OpCommand command : getCommandsForArg(args.length)) {
                if (command.getName().equals(args[0]) || list.size() == 1) {
                    if (command.getUseCommand().hasPermission(sender)) {
                        command.executeFunction(sender, args);
                    }
                }
            }
        } else {
            executeFunction(sender, args);
        }
        return false;
    }

    public void executeFunction(@NotNull CommandSender sender, @NotNull String[] args) {
        try {
            biConsumer.accept(new OpCommandSender(sender), new OpCommandArgument(this.args, args, isMain ? -1 : 0));
        } catch (NullPointerException ignore) {}
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        OpList<String> strings = new OpList<>();
        for (OpCommand command : getCommandsForArg(args.length)) {
            if (command.getSeeTabComplete().hasPermission(sender)) {
                strings.addAll(command.tabCompleteFunction(sender, args));
            }
        }
        if (isMain) {
            strings.addAll(tabCompleteFunction(sender, args));
        }
        return StringUtil.copyPartialMatches(args[args.length - 1], strings);
    }

    public OpList<String> tabCompleteFunction(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        OpList<String> completions = new OpList<>();
        if (args.length > getHighestArgNumber()) {
            return completions;
        }

        if (isMain && args.length == 1) {
            if (simpleSuggestions.size() > 0) {
                completions.addAll(simpleSuggestions.get(0).getSuggestions());
            } else if (suggestions.size() > 0) {
                completions.addAll(suggestions.get(0).apply(new OpCommandSender(sender), args));
            }
        } else if (args.length > 1) {
            if (getName().equals(args[0])) {
                if (getSimpleSuggestionsCount() == args.length - 1) {
                    completions.addAll(simpleSuggestions.get(args.length - 2).getSuggestions());
                }
                if (getSuggestionSize() == args.length - 1) {
                    completions.addAll(suggestions.get(args.length - 2).apply(new OpCommandSender(sender), args));
                }
            }
        }
        return completions;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args, @Nullable Location location) throws IllegalArgumentException {
        return tabComplete(sender, alias, args);
    }

    public void register() {
        if (!removeDefaultCommandSuggestion) {
            OpList<String> names = new OpList<>();
            for (OpCommand command : list) {
                String name = command.name;
                if (!name.equals(this.name)) {
                    names.add(name);
                }
            }
            if (names.size() != 0) {
                addSuggestion(names);
            }
        }
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            Command command = commandMap.getCommand(this.getName());
            if (command == null || !command.isRegistered()) {
                commandMap.register(name, this);
                OpAPI.addCommand(this);
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
            ((Map<String, Command>) knownCommands.get(commandMap.get(Bukkit.getServer()))).remove(name);
            this.unregister((CommandMap) commandMap.get(Bukkit.getServer()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BiConsumer<OpCommandSender, OpCommandArgument<?>> getConsumer() {
        return biConsumer;
    }

    public OpCommand execute(BiConsumer<OpCommandSender, OpCommandArgument<?>> biConsumer) {
        this.biConsumer = biConsumer;
        return this;
    }

    public OpCommand executeAsPlayer(BiConsumer<OpCommandSender, OpCommandArgument<?>> biConsumer) {
        setHasToBePlayer(true);
        return execute(biConsumer);
    }

    public OpCommand executeAsPlayerWithArgLength(BiConsumer<OpCommandSender, OpCommandArgument<?>> biConsumer, int argLength) {
        setHasToBePlayer(true);
        return executeWithArgLength(biConsumer, argLength);
    }

    public OpCommand execute(Consumer<OpCommandSender> consumer) {
        this.biConsumer = (sender, args) -> consumer.accept(sender);
        return this;
    }

    public OpCommand executeWithArgLength(BiConsumer<OpCommandSender, OpCommandArgument<?>> biConsumer, int argLength) {
        this.biConsumer = ((sender, args) -> {
            if (args.getLength() == argLength) {
                biConsumer.accept(sender, args);
            }
        });
        return this;
    }

    public OpCommand executeWithArgLength(Consumer<OpCommandSender> consumer, int argLength) {
        this.biConsumer = ((sender, args) -> {
            if (args.getLength() == argLength) {
                consumer.accept(sender);
            }
        });
        return this;
    }

    public OpList<Integer> getArgsNumber() {
        return argsNumber;
    }

    public OpCommand setArgsNumber(OpList<Integer> argsNumber) {
        this.argsNumber = argsNumber;
        return this;
    }

    public OpCommand setArgsNumber(Integer... argsNumber) {
        this.argsNumber = OpList.asList(argsNumber);
        return this;
    }

    public OpCommand addArgNumber() {
        OpList<Integer> list = VariableUtil.getOrDefault(getArgsNumber(), new OpList<>());
        int last = -1;
        if (list.size() != 0) {
            last = list.get(list.size() - 1);
        }
        if (last == -1) {
            if (isMain) {
                list.set(0, 0);
            } else {
                list.set(0, 1);
            }
        } else {
            OpList<Integer> list2 = new OpList<>();
            list2.addAll(list);
            list2.add(last + 1);
            return setArgsNumber(list2);
        }
        return setArgsNumber(list);
    }

    public int getHighestArgNumber() {
        int highest = -1;
        for (int number : getArgsNumber()) {
            if (number > highest) {
                highest = number;
            }
        }
        return highest;
    }

    public boolean hasToBePlayer() {
        return hasToBePlayer;
    }

    public OpCommand setHasToBePlayer(boolean hasToBePlayer) {
        this.hasToBePlayer = hasToBePlayer;
        return this;
    }

    public boolean doesRemoveDefaultSuggestions() {
        return removeDefaultCommandSuggestion;
    }

    public OpCommand removeDefaultSuggestions(boolean removeDefaultCommandSuggestion) {
        this.removeDefaultCommandSuggestion = removeDefaultCommandSuggestion;
        return this;
    }

    public OpCommandPermission getSeeTabComplete() {
        return seeTabComplete;
    }

    public OpCommand setSeeTabComplete(String permission) {
        this.seeTabComplete = new OpCommandPermission(permission, "", OpCommandPermission.PERMISSION_TYPE.SEE_TAB_COMPLETE);
        return this;
    }

    public OpCommandPermission getUseCommand() {
        return useCommand;
    }

    public OpCommand setUseCommand(String permission, String noPermissionMessage) {
        this.useCommand = new OpCommandPermission(permission, noPermissionMessage, OpCommandPermission.PERMISSION_TYPE.USE_COMMAND);
        return this;
    }

    public int getSimpleSuggestionsCount() {
        int count = 0;
        for (OpSimpleSuggestion suggestion : simpleSuggestions) {
            count+=suggestion.getSuggestions().size();
        }
        return count;
    }

    public int getSuggestionSize() {
        return suggestions.size();
    }

    public OpTypeArg<?> getGlobalCommandArg() {
        return globalCommandArg;
    }

    public <I extends IType> OpCommand setGlobalCommandArg(OpTypeArg<I> globalCommandArg) {
        this.globalCommandArg = globalCommandArg;
        return this;
    }

    public OpSimpleSuggestion getGlobalSimpleSuggestion() {
        return globalSimpleSuggestion;
    }

    public OpCommand setGlobalSimpleSuggestion(OpSimpleSuggestion globalSimpleSuggestion) {
        this.globalSimpleSuggestion = globalSimpleSuggestion;
        return this;
    }

    public OpCommandSuggestion getGlobalCommandSuggestion() {
        return globalCommandSuggestion;
    }

    public OpCommand setGlobalCommandSuggestion(OpCommandSuggestion globalCommandSuggestion) {
        this.globalCommandSuggestion = globalCommandSuggestion;
        return this;
    }

    public OpCommand clearGlobalAdditions() {
        this.globalCommandSuggestion = null;
        this.globalSimpleSuggestion = null;
        this.globalCommandArg = null;
        return this;
    }
}