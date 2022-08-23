package me.opkarol.opc.api.teleport;

import me.opkarol.opc.api.files.Configuration;
import me.opkarol.opc.api.misc.OpParticle;
import me.opkarol.opc.api.misc.OpSound;
import me.opkarol.opc.api.misc.OpTitle;
import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportSettings {
    private TeleportSettingsVisual onStart, onEach, onEnd, onInvalid;

    public TeleportSettings(TeleportSettingsVisual onStart, TeleportSettingsVisual onEach, TeleportSettingsVisual onEnd, TeleportSettingsVisual onInvalid) {
        this.onStart = onStart;
        this.onEach = onEach;
        this.onEnd = onEnd;
        this.onInvalid = onInvalid;
    }

    public TeleportSettings(FileConfiguration configuration, String path) {
        path = path.endsWith(".") ? path : path.concat(".");
        this.onStart = new TeleportSettingsVisual(configuration, path + "onStart");
        this.onEach = new TeleportSettingsVisual(configuration, path + "onEach");
        this.onEnd = new TeleportSettingsVisual(configuration, path + "onEnd");
        this.onInvalid = new TeleportSettingsVisual(configuration, path + "onInvalid");
    }

    public TeleportSettingsVisual getOnStart() {
        return onStart;
    }

    public void setOnStart(TeleportSettingsVisual onStart) {
        this.onStart = onStart;
    }

    public TeleportSettingsVisual getOnEach() {
        return onEach;
    }

    public void setOnEach(TeleportSettingsVisual onEach) {
        this.onEach = onEach;
    }

    public TeleportSettingsVisual getOnEnd() {
        return onEnd;
    }

    public void setOnEnd(TeleportSettingsVisual onEnd) {
        this.onEnd = onEnd;
    }

    public void use(Player player, @NotNull TeleportSettingsVisual settingsVisual, String... specialData) {
        OpSound sound = settingsVisual.sound();
        OpParticle particle = settingsVisual.particle();
        OpTitle title = settingsVisual.title();
        String text = settingsVisual.text();
        if (sound != null) {
            sound.play(player);
        }

        if (particle != null) {
            particle.display(player);
        }

        if (title != null) {
            if (specialData.length > 1) {
                title.setSubTitle(specialData[0], specialData[1]).setTitle(specialData[0], specialData[1]).display(player);
            } else {
                title.display(player);
            }
        }

        if (text != null) {
            if (specialData.length > 1) {
                player.sendMessage(FormatUtils.formatMessage(text.replace(specialData[0], specialData[1])));
            } else {
                player.sendMessage(FormatUtils.formatMessage(text));
            }
        }
    }

    public TeleportSettingsVisual getOnInvalid() {
        return onInvalid;
    }

    public void setOnInvalid(TeleportSettingsVisual onInvalid) {
        this.onInvalid = onInvalid;
    }

    public void saveInConfiguration(Configuration configuration, String path) {
        path = path.endsWith(".") ? path : path.concat(".");
        onStart.saveInConfiguration(configuration, path + "onStart");
        onEach.saveInConfiguration(configuration, path + "onEach");
        onEnd.saveInConfiguration(configuration, path + "onEnd");
        onInvalid.saveInConfiguration(configuration, path + "onInvalid");
        configuration.save();
    }
}
