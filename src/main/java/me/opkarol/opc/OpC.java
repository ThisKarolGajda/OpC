package me.opkarol.opc;

import me.opkarol.opc.api.commands.OpCommand;
import me.opkarol.opc.api.commands.arguments.StringArg;
import me.opkarol.opc.api.language.LanguageDatabase;
import me.opkarol.opc.api.plugin.OpPlugin;

import java.util.Arrays;

public final class OpC extends OpPlugin {
    private LanguageDatabase langDb;

    @Override
    public void onEnable() {
        OpAPI.init(this);
        langDb = new LanguageDatabase();
        // OpPluginLanguage language = LanguageDatabase.addPluginLanguage(new OpPluginLanguage(getInstance(), LanguageType.en_US, LanguageType.pl_PL));
        super.onEnable();

    }

    @Override
    public void onDisable() {
        OpAPI.unregisterCommands();
        langDb.getLanguageTool().onDisable();
        langDb = null;
        super.onDisable();
    }

    void test() {
        new OpCommand("command-name")
                // We need to remove default command suggestion because we have to override it
                .setRemoveDefaultCommandSuggestion(true)
                // Add String argument with name arg and suggestion with list of animals names
                .addArgSuggestion(new StringArg("arg"), Arrays.asList("Cat", "Dog", "Bird"))
                .execute((sender, args) -> {
                    String arg = (String) args.get("arg");
                    if (arg.equalsIgnoreCase("Cat")) {
                        sender.sender().sendMessage("I love cats!!!");
                    } else {
                        sender.sender().sendMessage("You have chosen: " + args.get("arg"));
                    }
                })
                // Don't forget to register it!!!
                .register();
    }
}
