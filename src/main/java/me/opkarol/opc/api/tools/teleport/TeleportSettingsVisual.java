package me.opkarol.opc.api.tools.teleport;

import me.opkarol.opc.api.serialization.Serialize;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.misc.opobjects.OpParticle;
import me.opkarol.opc.api.misc.opobjects.OpSound;
import me.opkarol.opc.api.misc.opobjects.OpText;
import me.opkarol.opc.api.misc.opobjects.OpTitle;
import org.jetbrains.annotations.NotNull;

public final class TeleportSettingsVisual extends Serialize {
    private OpText text;
    private OpParticle particle;
    private OpSound sound;
    private OpTitle title;

    public TeleportSettingsVisual(String text, OpParticle particle, OpSound sound, OpTitle title) {
        super(null);
        this.text = new OpText(text);
        this.particle = particle;
        this.sound = sound;
        this.title = title;
    }

    public TeleportSettingsVisual(OpMap<String, Object> objects) {
        super(null);
        objects.getByKey("particle").ifPresent(particle -> setParticle((OpParticle) particle));
        objects.getByKey("sound").ifPresent(sound -> setSound((OpSound) sound));
        objects.getByKey("title").ifPresent(title -> setTitle((OpTitle) title));
        objects.getByKey("text").ifPresent(text -> setText((OpText) text));
    }

    public TeleportSettingsVisual() {
        super(null);
    }

    public OpText getText() {
        return text;
    }

    public OpParticle getParticle() {
        return particle;
    }

    public OpSound getSound() {
        return sound;
    }

    public OpTitle getTitle() {
        return title;
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

    @Override
    public @NotNull OpMap<String, Object> serialize() {
        return getMapBuilder()
                .setValue("particle", particle)
                .setValue("sound", sound)
                .setValue("title", title)
                .setValue("text", text);
    }

    public void setText(OpText text) {
        this.text = text;
    }

    public void setParticle(OpParticle particle) {
        this.particle = particle;
    }

    public void setTitle(OpTitle title) {
        this.title = title;
    }

    public void setSound(OpSound sound) {
        this.sound = sound;
    }
}
