package me.opkarol.opc.api.teleport;

import me.opkarol.opc.OpC;
import me.opkarol.opc.api.configuration.CustomConfigurable;
import me.opkarol.opc.api.configuration.CustomConfiguration;
import me.opkarol.opc.api.misc.OpParticle;
import me.opkarol.opc.api.misc.OpSound;
import me.opkarol.opc.api.misc.OpText;
import me.opkarol.opc.api.misc.OpTitle;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.Consumer;

public final class TeleportSettingsVisual implements Serializable, CustomConfigurable {
    private OpText text;
    private OpParticle particle;
    private OpSound sound;
    private OpTitle title;

    public TeleportSettingsVisual(String text, OpParticle particle, OpSound sound, OpTitle title) {
        this.text = new OpText(text);
        this.particle = particle;
        this.sound = sound;
        this.title = title;
    }

    public TeleportSettingsVisual(String path) {
        get(path);
    }


    public TeleportSettingsVisual() { }

    public OpText text() {
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

    @Override
    public @NotNull Consumer<CustomConfiguration> get() {
        return c -> {
            OpC.getLog().info(c.getDefaultPath() + " --- 3 " + c.getParticle("particle"));
            particle = c.getParticle("particle");
            sound = c.getSound("sound");
            title = c.getTitle("title");
            text = c.getText("");
        };
    }

    @Override
    public @NotNull Consumer<CustomConfiguration> save() {
        return c -> c.setConfigurable("particle", particle)
                .setConfigurable("sound", sound)
                .setConfigurable("title", title)
                .setConfigurable("", text);
    }

    @Override
    public String toString() {
        return "TeleportSettingsVisual{" +
                "text=" + text +
                ", particle=" + particle +
                ", sound=" + sound +
                ", title=" + title +
                '}';
    }
}
