package me.opkarol.opc;

import me.opkarol.opc.api.commands.OpCommand;
import me.opkarol.opc.api.language.LanguageDatabase;
import me.opkarol.opc.api.plugin.OpPlugin;
import me.opkarol.opc.api.teleport.OpTeleport;
import me.opkarol.opc.api.teleport.TeleportRegistration;

public final class OpC extends OpPlugin {
    private LanguageDatabase langDb;

    @Override
    public void onEnable() {
        OpAPI.init(this);
        langDb = new LanguageDatabase();
        // OpPluginLanguage language = LanguageDatabase.addPluginLanguage(new OpPluginLanguage(getInstance(), LanguageType.en_US, LanguageType.pl_PL));
        super.onEnable();

        OpTeleport teleport = new OpTeleport(15, "world", 2370, -1780).setRegistration(new TeleportRegistration());
        teleport.saveInConfiguration(this.getConfiguration(), "teleport-test-1");
        new OpCommand("test")
                .executeAsPlayer((sender, args) -> teleport.teleportCopied(sender))
                .register();
    }

    @Override
    public void onDisable() {
        OpAPI.unregisterCommands();
        langDb.getLanguageTool().onDisable();
        langDb = null;
        super.onDisable();
    }
}
