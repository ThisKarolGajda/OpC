package me.opkarol.opc.api.tools.teleport;

import me.opkarol.opc.api.misc.opobjects.OpSound;
import me.opkarol.opc.api.misc.opobjects.OpTitle;
import org.bukkit.Sound;

public class DefaultTeleportSettings extends TeleportSettings {
    private static final TeleportSettingsVisual onStart = new TeleportSettingsVisual("You are going to be teleported soon! Don't move!", null, null, null);
    private static final TeleportSettingsVisual onEach = new TeleportSettingsVisual(null, null, new OpSound(Sound.BLOCK_COMPARATOR_CLICK), new OpTitle("Teleporting in %time%!", "Don't move!", 5, 13, 2));
    private static final TeleportSettingsVisual onEnd = new TeleportSettingsVisual("You were teleported to %location%!", null, new OpSound(Sound.BLOCK_ANVIL_LAND).setVolume(0.3f), new OpTitle("Teleported!", "%location%", 10, 100, 10));
    private static final TeleportSettingsVisual onInvalid = new TeleportSettingsVisual("Place where you wanted to teleport is unsafe!", null, new OpSound(Sound.ENTITY_GOAT_HORN_BREAK).setVolume(1.5f), new OpTitle("Not safe!", "You cannot teleport there!", 10, 80, 10));

    public DefaultTeleportSettings() {
        super(onStart, onEach, onEnd, onInvalid);
    }
}
