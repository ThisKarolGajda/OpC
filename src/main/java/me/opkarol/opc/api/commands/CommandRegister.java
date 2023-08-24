package me.opkarol.opc.api.commands;

import java.util.ArrayList;
import java.util.List;

public class CommandRegister {
    private final List<Command> commandList = new ArrayList<>();

    public void registerCommand(Command command) {
        commandList.add(command);
        command.register();
    }

    public void register(Object object) {
        registerCommand(new Command(object));
    }

    public void unregisterCommands() {
        for (Command command : commandList) {
            command.unregister();
            commandList.remove(command);
        }
    }

    public List<Command> getCommandList() {
        return commandList;
    }
}
