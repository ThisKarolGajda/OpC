package me.opkarol.opc.api.teleport;

import me.opkarol.opc.api.configuration.CustomConfigurable;
import me.opkarol.opc.api.configuration.CustomConfiguration;
import me.opkarol.opc.api.misc.OpParticle;
import me.opkarol.opc.api.misc.OpSound;
import me.opkarol.opc.api.misc.OpText;
import me.opkarol.opc.api.misc.OpTitle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TeleportSettings implements CustomConfigurable {
    private TeleportSettingsVisual onStart, onEach, onEnd, onInvalid;

    public TeleportSettings(TeleportSettingsVisual onStart, TeleportSettingsVisual onEach, TeleportSettingsVisual onEnd, TeleportSettingsVisual onInvalid) {
        this.onStart = onStart;
        this.onEach = onEach;
        this.onEnd = onEnd;
        this.onInvalid = onInvalid;
    }

    public TeleportSettings(String path) {
        get(path);
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
        OpText text = settingsVisual.text();
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

        if (text != null && !text.isEmpty()) {
            if (specialData.length > 1) {
                player.sendMessage(text.getFormattedText().replace(specialData[0], specialData[1]));
            } else {
                player.sendMessage(text.getFormattedText());
            }
        }
    }

    public TeleportSettingsVisual getOnInvalid() {
        return onInvalid;
    }

    public void setOnInvalid(TeleportSettingsVisual onInvalid) {
        this.onInvalid = onInvalid;
    }

    @Override
    public Consumer<CustomConfiguration> get() {
        return c -> {
            this.onStart = new TeleportSettingsVisual(c.getPath("onStart"));
            this.onEach = new TeleportSettingsVisual(c.getPath("onEach"));
            this.onEnd = new TeleportSettingsVisual(c.getPath("onEnd"));
            this.onInvalid = new TeleportSettingsVisual(c.getPath("onInvalid"));
        };
    }

    @Override
    public String toString() {
        return "TeleportSettings{" +
                "onStart=" + onStart +
                ", onEach=" + onEach +
                ", onEnd=" + onEnd +
                ", onInvalid=" + onInvalid +
                '}';
    }

    @Override
    public Consumer<CustomConfiguration> save() {
        return c -> c.setConfigurable("onStart", onStart)
                .setConfigurable("onEach", onEach)
                .setConfigurable("onEnd", onEnd)
                .setConfigurable("onInvalid", onInvalid);
    }


}
