package me.opkarol.opc.api.misc;

import me.opkarol.opc.api.configuration.CustomConfigurable;
import me.opkarol.opc.api.configuration.CustomConfiguration;
import me.opkarol.opc.api.location.OpSerializableLocation;
import me.opkarol.opc.api.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

public class OpSound implements Serializable, CustomConfigurable {
    private float volume = 1, pitch = 1;
    private Sound sound;
    private SoundCategory category;
    private List<Player> receivers;
    private OpSerializableLocation location;

    public OpSound(Sound sound) {
        this.sound = sound;
    }

    public OpSound() {
        this.sound = null;
    }

    public OpSound(String sound) {
        StringUtil.getEnumValue(sound, Sound.class).ifPresent(sound1 -> this.sound = sound1);
    }

    public Sound getSound() {
        return sound;
    }

    public OpSound setSound(Sound sound) {
        this.sound = sound;
        return this;
    }

    public OpSound playToAll() {
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
            player.playSound(location, sound, volume, pitch);
        } else {
            player.playSound(location, sound, category, volume, pitch);
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


    public OpSound setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public OpSound setVolume(float volume) {
        this.volume = volume;
        return this;
    }
    @Override
    public Consumer<CustomConfiguration> get() {
        return c -> {
            this.volume = c.getFloat("volume");
            this.pitch = c.getFloat("pitch");
            this.sound = c.getUnsafeEnum("sound", Sound.class);
            this.category = c.getUnsafeEnum("category", SoundCategory.class);
            this.location = c.getLocation("location");
        };
    }

    @Override
    public Consumer<CustomConfiguration> save() {
        return c -> c.setFloat("volume", volume)
                .setFloat("pitch", pitch)
                .setEnum("sound", sound)
                .setEnum("category", category)
                .setLocation("location", location).save();
    }
}
