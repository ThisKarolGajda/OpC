package me.opkarol.opc.api.misc;

import me.opkarol.opc.api.configuration.CustomConfiguration;
import me.opkarol.opc.api.configuration.IEmptyConfiguration;
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

/**
 * The type Op sound.
 */
public class OpSound implements Serializable, IEmptyConfiguration {
    private float volume = 1, pitch = 1;
    private Sound sound;
    private SoundCategory category;
    private List<Player> receivers;
    private OpSerializableLocation location;

    /**
     * Instantiates a new Op sound.
     *
     * @param sound the sound
     */
    public OpSound(Sound sound) {
        this.sound = sound;
    }

    /**
     * Instantiates a new Op sound.
     */
    public OpSound() {
        this.sound = null;
    }

    /**
     * Instantiates a new Op sound.
     *
     * @param sound the sound
     */
    public OpSound(String sound) {
        StringUtil.getEnumValue(sound, Sound.class).ifPresent(sound1 -> this.sound = sound1);
    }

    /**
     * Gets sound.
     *
     * @return the sound
     */
    public Sound getSound() {
        return sound;
    }

    /**
     * Sets sound.
     *
     * @param sound the sound
     * @return the sound
     */
    public OpSound setSound(Sound sound) {
        this.sound = sound;
        return this;
    }

    /**
     * Play to all op sound.
     *
     * @return the op sound
     */
    public OpSound playToAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            play(player);
        }
        return this;
    }

    /**
     * Play op sound.
     *
     * @return the op sound
     */
    public OpSound play() {
        return play(getLocation());
    }

    /**
     * Play op sound.
     *
     * @param location the location
     * @return the op sound
     */
    public OpSound play(Location location) {
        return play(new OpSerializableLocation(location));
    }

    /**
     * Play op sound.
     *
     * @param location the location
     * @return the op sound
     */
    public OpSound play(OpSerializableLocation location) {
        if (receivers == null) {
            return this;
        }

        receivers.forEach(player -> play(player, location.getLocation()));
        return this;
    }

    /**
     * Play op sound.
     *
     * @param player   the player
     * @param location the location
     * @return the op sound
     */
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

    /**
     * Play op sound.
     *
     * @param player the player
     * @return the op sound
     */
    public OpSound play(@NotNull Player player) {
        return play(player, player.getLocation());
    }

    /**
     * Gets category.
     *
     * @return the category
     */
    public SoundCategory getCategory() {
        return category;
    }

    /**
     * Sets category.
     *
     * @param category the category
     * @return the category
     */
    public OpSound setCategory(SoundCategory category) {
        this.category = category;
        return this;
    }

    /**
     * Sets receivers.
     *
     * @param receivers the receivers
     * @return the receivers
     */
    public OpSound setReceivers(List<Player> receivers) {
        this.receivers = receivers;
        return this;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public OpSerializableLocation getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     * @return the location
     */
    public OpSound setLocation(OpSerializableLocation location) {
        this.location = location;
        return this;
    }

    /**
     * Sets location.
     *
     * @param location the location
     * @return the location
     */
    public OpSound setLocation(Location location) {
        this.location = new OpSerializableLocation(location);
        return this;
    }


    /**
     * Sets pitch.
     *
     * @param pitch the pitch
     * @return the pitch
     */
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

    @Override
    public boolean isEmpty() {
        return volume == -1 || pitch == -1 || sound == null;
    }
}
