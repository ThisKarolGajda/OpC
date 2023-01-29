package me.opkarol.opc.api.wrappers;

import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.map.OpMapBuilder;
import me.opkarol.opc.api.serialization.SerializableName;
import me.opkarol.opc.api.serialization.Serialize;
import me.opkarol.opc.api.tools.location.OpSerializableLocation;
import me.opkarol.opc.api.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SerializableName("OpSound")
public class OpSound extends Serialize {
    private double volume = 1, pitch = 1;
    private Sound sound;
    private SoundCategory category;
    private List<Player> receivers;
    private OpSerializableLocation location;

    public OpSound(Sound sound) {
        super(null);
        this.sound = sound;
    }

    public OpSound() {
        super(null);
        this.sound = null;
    }

    public OpSound(String sound) {
        super(null);
        StringUtil.getEnumValue(sound, Sound.class).ifPresent(sound1 -> this.sound = sound1);
    }

    public OpSound(@NotNull OpMapBuilder<String, Object> objects) {
        super(null);
        objects.ifPresentThen("volume", this::setVolume, StringUtil::getDouble);
        objects.ifPresentThen("pitch", this::setPitch, StringUtil::getDouble);
        objects.ifPresentEnumThen("sound", Sound.class, this::setSound);
        objects.ifPresentEnumThen("category", SoundCategory.class, this::setCategory);
        objects.<OpSerializableLocation>ifPresentSpecificThen("location", this::setLocation);
    }

    public Sound getSound() {
        return sound;
    }

    public OpSound setSound(Sound sound) {
        this.sound = sound;
        return this;
    }

    public OpSound playToAllOnline() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            play(player);
        }
        return this;
    }

    public OpSound play() {
        return play(getLocation());
    }

    public OpSound play(Location location) {
        return play(new OpSerializableLocation(location));
    }

    public OpSound play(OpSerializableLocation location) {
        if (receivers == null) {
            return this;
        }

        receivers.forEach(player -> play(player, location.getLocation()));
        return this;
    }

    public OpSound play(@NotNull Player player, @NotNull Location location) {
        if (sound == null) {
            return this;
        }
        if (category == null) {
            player.playSound(location, sound, (float) volume, (float) pitch);
        } else {
            player.playSound(location, sound, category, (float) volume, (float) pitch);
        }
        return this;
    }

    public OpSound play(@NotNull Player player) {
        return play(player, player.getLocation());
    }

    public SoundCategory getCategory() {
        return category;
    }

    public OpSound setCategory(SoundCategory category) {
        this.category = category;
        return this;
    }

    public OpSound setReceivers(List<Player> receivers) {
        this.receivers = receivers;
        return this;
    }

    public OpSerializableLocation getLocation() {
        return location;
    }

    public OpSound setLocation(OpSerializableLocation location) {
        this.location = location;
        return this;
    }

    public OpSound setLocation(Location location) {
        this.location = new OpSerializableLocation(location);
        return this;
    }


    public OpSound setPitch(double pitch) {
        this.pitch = pitch;
        return this;
    }

    public OpSound setVolume(double volume) {
        this.volume = volume;
        return this;
    }

    @Override
    public @NotNull OpMap<String, Object> serialize() {
        return getMapBuilder()
                .setValue("volume", volume)
                .setValue("pitch", pitch)
                .setValue("sound", sound.name())
                .setValue("category", category.name())
                .setValue("location", location);
    }

    @Override
    public String toString() {
        return "OpSound{" +
                "volume=" + volume +
                ", pitch=" + pitch +
                ", sound=" + sound +
                ", category=" + category +
                ", receivers=" + receivers +
                ", location=" + location +
                '}';
    }
}
