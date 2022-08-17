package me.opkarol.opc.api.misc;

import me.opkarol.opc.api.files.Configuration;
import me.opkarol.opc.api.utils.StringUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OpParticleBuilder {
    private float offsetX, offsetY, offsetZ;
    private int amount;
    private Particle particle;
    private OpLocation location;
    private List<Player> receivers;
    private OpRunnable animatedTask;

    public OpParticleBuilder(Particle particle) {
        this.particle = particle;
    }

    public OpParticleBuilder(@NotNull FileConfiguration config, String path) {
        path = path.endsWith(".") ? path : path.concat(".");
        StringUtil.getEnumValue(config.getString(path + "particle"), Particle.class).ifPresent(particle1 -> this.particle = particle1);
        this.amount = StringUtil.getInt(config.getString(path + "amount"));
        this.location = new OpLocation(config.getString(path + "location"));
        setOffset(config.getString(path + "offset"));
    }

    public void editInConfiguration(@NotNull FileConfiguration config, String path) {
        path = path.endsWith(".") ? path : path.concat(".");
        if (particle != null) {
            config.set(path + "particle", particle.name());
        }
        config.set(path + "amount", amount);
        if (location != null) {
            config.set(path + "location", location.toString());
        }
        config.set(path + "offset", getOffset());
    }

    public void saveInConfiguration(@NotNull Configuration configuration, String path) {
        path = path.endsWith(".") ? path : path.concat(".");
        FileConfiguration config = configuration.getConfig();
        if (config == null) {
            return;
        }
        if (particle != null) {
            config.set(path + "particle", particle.name());
        }
        config.set(path + "amount", amount);
        if (location != null) {
            config.set(path + "location", location.toString());
        }
        config.set(path + "offset", getOffset());
        configuration.save();
    }

    public OpParticleBuilder setOffset(float offsetX, float offsetY, float offsetZ) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        return this;
    }

    public String getOffset() {
        return String.format("%s;%s;%s", String.valueOf(offsetX), String.valueOf(offsetY), String.valueOf(offsetZ));
    }

    public OpParticleBuilder setOffset(String offset) {
        if (offset != null) {
            String[] offsets = offset.split(";");
            if (offsets.length == 3) {
                this.offsetX = StringUtil.getFloat(offsets[0]);
                this.offsetY = StringUtil.getFloat(offsets[1]);
                this.offsetZ = StringUtil.getFloat(offsets[2]);
            }
        }
        return this;
    }

    public OpParticleBuilder setOffset(@NotNull Vector offset) {
        this.offsetX = (float) offset.getX();
        this.offsetY = (float) offset.getY();
        this.offsetZ = (float) offset.getZ();
        return this;
    }

    public Particle getParticle() {
        return particle;
    }

    public OpParticleBuilder setParticle(Particle particle) {
        this.particle = particle;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public OpParticleBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public OpParticleBuilder setOffsetX(float offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public OpParticleBuilder setOffsetY(float offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public float getOffsetZ() {
        return offsetZ;
    }

    public OpParticleBuilder setOffsetZ(float offsetZ) {
        this.offsetZ = offsetZ;
        return this;
    }

    public OpLocation getLocation() {
        return location;
    }

    public OpParticleBuilder setLocation(OpLocation opLocation) {
        this.location = opLocation;
        return this;
    }

    public OpParticleBuilder setLocation(Location location) {
        this.location = new OpLocation(location);
        return this;
    }

    public List<Player> getReceivers() {
        return receivers;
    }

    public OpParticleBuilder setReceivers(List<Player> receivers) {
        this.receivers = receivers;
        return this;
    }

    public OpParticleBuilder addReceiver(Player player) {
        List<Player> list = receivers == null ? new ArrayList<>() : receivers;
        list.add(player);
        return setReceivers(list);
    }

    public OpParticleBuilder display() {
        return display(getReceivers());
    }

    public <T> OpParticleBuilder display(T specialData) {
        return display(getReceivers(), specialData);
    }

    public <T> OpParticleBuilder display(List<Player> players, T specialData) {
        if (players == null) {
            return this;
        }

        players.forEach(player -> player.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), amount, offsetX, offsetY, offsetZ, specialData));
        return this;
    }

    public OpParticleBuilder display(Player player) {
        if (player == null) {
            return this;
        }

        player.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), amount, offsetX, offsetY, offsetZ);
        return this;
    }

    public OpParticleBuilder display(List<Player> players) {
        if (players == null) {
            return this;
        }

        players.forEach(player -> player.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), amount, offsetX, offsetY, offsetZ));
        return this;
    }

    public OpParticleBuilder animateDisplay(int amount, int delay) {
        final int[] i = {0};
        animatedTask = new OpRunnable(r -> {
            if (i[0] >= amount) {
                r.cancel();
            }

            display();
            i[0]++;
        }).runTaskTimerAsynchronously(0, delay);
        return this;
    }

    public OpParticleBuilder displayByNearPlayers(Player byPlayer, int reach) {
        if (byPlayer == null) {
            return this;
        }
        List<Player> players = new ArrayList<>(List.of(byPlayer));
        for (Entity entity : byPlayer.getNearbyEntities(reach, reach, reach)) {
            if (entity instanceof Player) {
                players.add((Player) entity);
            }
        }
        return display(players);
    }

    public void cancelAnimatedTask() {
        animatedTask.cancel();
    }
}