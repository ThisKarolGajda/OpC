package me.opkarol.opc;

import me.opkarol.opc.api.gui.misc.OpInventoryRows;
import me.opkarol.opc.api.gui.pattern.OpInventoryPattern;
import me.opkarol.opc.api.gui.type.OpInventoryBuilder;
import me.opkarol.opc.api.language.LanguageDatabase;
import me.opkarol.opc.api.plugin.OpPlugin;
import me.opkarol.opc.api.utils.CommandUtil;
import org.bukkit.Material;

public final class OpC extends OpPlugin {
    private LanguageDatabase langDb;

    @Override
    public void onEnable() {
        OpAPI.init(this);
        langDb = new LanguageDatabase();
        // OpPluginLanguage language = LanguageDatabase.addPluginLanguage(new OpPluginLanguage(getInstance(), LanguageType.en_US, LanguageType.pl_PL));
        super.onEnable();

        new OpInventoryBuilder("inv", "Title", OpInventoryRows.ROWS_1)
                .setPattern(new OpInventoryPattern("X O X O C O X O X")
                        .addPatternValue("X", Material.BLACK_STAINED_GLASS_PANE)
                        .addPatternValue("O", Material.WRITTEN_BOOK)
                        .addPatternValue("C", Material.MAGENTA_TERRACOTTA))
                .saveInventory(OpC.getInstance().getConfiguration(), "inventories");

    }

    @Override
    public void onDisable() {
        CommandUtil.onDisable();
        langDb.getLanguageTool().onDisable();
        langDb = null;
        super.onDisable();
    }
}
