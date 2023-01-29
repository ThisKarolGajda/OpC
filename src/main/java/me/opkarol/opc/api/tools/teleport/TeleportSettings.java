package me.opkarol.opc.api.tools.teleport;

import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.wrappers.OpParticle;
import me.opkarol.opc.api.wrappers.OpSound;
import me.opkarol.opc.api.wrappers.OpText;
import me.opkarol.opc.api.wrappers.OpTitle;
import me.opkarol.opc.api.serialization.Serialize;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportSettings extends Serialize {
    private TeleportSettingsVisual onStart, onEach, onEnd, onInvalid;

    public TeleportSettings(TeleportSettingsVisual onStart, TeleportSettingsVisual onEach, TeleportSettingsVisual onEnd, TeleportSettingsVisual onInvalid) {
        super(null);
        this.onStart = onStart;
        this.onEach = onEach;
        this.onEnd = onEnd;
        this.onInvalid = onInvalid;
    }

    public TeleportSettings(@NotNull OpMap<String, Object> objects) {
        super(null);
        objects.getByKey("onStart").ifPresent(onStart -> setOnStart((TeleportSettingsVisual) onStart));
        objects.getByKey("onEach").ifPresent(onEach -> setOnEach((TeleportSettingsVisual) onEach));
        objects.getByKey("onEnd").ifPresent(onEnd -> setOnEnd((TeleportSettingsVisual) onEnd));
        objects.getByKey("onInvalid").ifPresent(onInvalid -> setOnInvalid((TeleportSettingsVisual) onInvalid));
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
        OpSound sound = settingsVisual.getSound();
        OpParticle particle = settingsVisual.getParticle();
        OpTitle title = settingsVisual.getTitle();
        OpText text = settingsVisual.getText();

        if (sound != null) {
            sound.play(player);
        }

        if (particle != null) {
            particle.display(player);
        }

        if (title != null) {
            if (specialData.length > 1) {
                title.setSubtitle(specialData[0], specialData[1]).setTitle(specialData[0], specialData[1]).display(player);
            } else {
                title.display(player);
            }
        }

        if (text != null) {
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
    public String toString() {
        return "TeleportSettings{" +
                "onStart=" + onStart +
                ", onEach=" + onEach +
                ", onEnd=" + onEnd +
                ", onInvalid=" + onInvalid +
                '}';
    }


    @Override
    public @NotNull OpMap<String, Object> serialize() {
        return getMapBuilder()
                .setValue("onStart", onStart)
                .setValue("onEach", onEach)
                .setValue("onEnd", onEnd)
                .setValue("onInvalid", onInvalid);
    }
}
