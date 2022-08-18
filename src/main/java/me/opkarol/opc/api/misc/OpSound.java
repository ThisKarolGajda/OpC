package me.opkarol.opc.api.misc;

import me.opkarol.opc.api.files.Configuration;
import me.opkarol.opc.api.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public class OpSound implements Serializable {
    private float volume = 1, pitch = 1;
    private Sound sound;
    private SoundCategory category;
    private List<Player> receivers;
    private OpLocation location;

    public OpSound(Sound sound) {
        this.sound = sound;
    }

    public OpSound(String sound) {
        StringUtil.getEnumValue(sound, Sound.class).ifPresent(sound1 -> this.sound = sound1);
    }

    public OpSound(FileConfiguration config, String path) {
        path = path.endsWith(".") ? path : path.concat(".");
        this.volume = StringUtil.getFloat(config.getString(path + "volume"));
        this.pitch = StringUtil.getFloat(config.getString(path + "pitch"));
        StringUtil.getEnumValue(config.getString(path + "category"), SoundCategory.class).ifPresent(soundCategory -> this.category = soundCategory);
        StringUtil.getEnumValue(config.getString(path + "sound"), Sound.class).ifPresent(sound1 -> this.sound = sound1);
        this.location = new OpLocation(config.getString(path + "location"));
    }

    public void editInConfiguration(@NotNull FileConfiguration config, String path) {
        path = path.endsWith(".") ? path : path.concat(".");
        config.set(path + "volume", volume);
        config.set(path + "pitch", pitch);
        if (category != null) {
            config.set(path + "category", category.name());
        }
        if (location != null) {
            config.set(path + "location", location.toString());
        }
        if (sound != null) {
            config.set(path + "sound", sound.name());
        }
    }

    public void saveInConfiguration(@NotNull Configuration configuration, String path) {
        path = path.endsWith(".") ? path : path.concat(".");
        FileConfiguration config = configuration.getConfig();
        if (config == null) {
            return;
        }
        config.set(path + "volume", volume);
        config.set(path + "pitch", pitch);
        if (category != null) {
            config.set(path + "category", category.name());
        }
        if (location != null) {
            config.set(path + "location", location.toString());
        }
        if (sound != null) {
            config.set(path + "sound", sound.name());
        }
        configuration.save();
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
        return play(new OpLocation(location));
    }

    public OpSound play(OpLocation location) {
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

    public OpLocation getLocation() {
        return location;
    }

    public OpSound setLocation(OpLocation location) {
        this.location = location;
        return this;
    }

    public OpSound setLocation(Location location) {
        this.location = new OpLocation(location);
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
}
