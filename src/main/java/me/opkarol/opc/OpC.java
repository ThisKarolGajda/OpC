package me.opkarol.opc;

import me.opkarol.opc.api.gui.misc.OpInventoryRows;
import me.opkarol.opc.api.gui.type.OpInventoryBuilder;
import me.opkarol.opc.api.item.OpItemBuilder;
import me.opkarol.opc.api.plugin.OpPlugin;
import org.bukkit.Material;

import java.util.List;

public class OpC extends OpPlugin {

    @Override
    public void enable() {
        OpAPI.init(this);

        new OpInventoryBuilder("holder-name", "&bTEST", OpInventoryRows.ROWS_3)
                .setItem(Material.STONE_AXE, 10, 12, 15)
                .setItem(new OpItemBuilder(Material.CARROT).setAmount(15).setLore(List.of("OYOYOYO")), 1)
                .setNoCloseable()
                .save("test");
    }

    @Override
    public void disable() {
        
    }
}
