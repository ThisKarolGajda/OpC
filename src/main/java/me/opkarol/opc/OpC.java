package me.opkarol.opc;

import me.opkarol.opc.api.language.LanguageDatabase;
import me.opkarol.opc.api.plugin.OpPlugin;

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
}
