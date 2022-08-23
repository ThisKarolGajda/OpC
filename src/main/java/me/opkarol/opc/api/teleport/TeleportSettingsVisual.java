package me.opkarol.opc.api.teleport;

import me.opkarol.opc.api.files.Configuration;
import me.opkarol.opc.api.misc.OpParticle;
import me.opkarol.opc.api.misc.OpSound;
import me.opkarol.opc.api.misc.OpTitle;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public final class TeleportSettingsVisual implements Serializable {
    private String text;
    private OpParticle particle;
    private OpSound sound;
    private OpTitle title;

    public TeleportSettingsVisual(String text, OpParticle particle, OpSound sound, OpTitle title) {
        this.text = text;
        this.particle = particle;
        this.sound = sound;
        this.title = title;
    }

    public void saveInConfiguration(@NotNull Configuration configuration, String path) {
        FileConfiguration config = configuration.getConfig();
        path = path.endsWith(".") ? path : path.concat(".");
        if (text != null) {
            config.set(path + "text", text);
        }
        if (particle != null) {
            particle.saveInConfiguration(configuration, path + "particle");
        }
        if (sound != null) {
            sound.saveInConfiguration(configuration, path + "sound");
        }
        if (title != null) {
            title.saveInConfiguration(configuration, path + "title");
        }
        configuration.save();
    }

    public TeleportSettingsVisual(@NotNull FileConfiguration config, String path) {
        path = path.endsWith(".") ? path : path.concat(".");
        this.text = config.getString(path + "text");
        if (config.get(path + "particle") != null) {
            this.particle = new OpParticle(config, path + "particle");
        }
        if (config.get(path + "sound") != null) {
            this.sound = new OpSound(config, path + "sound");
        }
        if (config.get(path + "title") != null) {
            this.title = new OpTitle(config, path + "title");
        }
    }

    public String text() {
        return text;
    }

    public OpParticle particle() {
        return particle;
    }

    public OpSound sound() {
        return sound;
    }

    public OpTitle title() {
        return title;
    }
}
