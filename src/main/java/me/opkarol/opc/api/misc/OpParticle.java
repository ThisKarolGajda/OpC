package me.opkarol.opc.api.misc;

import me.opkarol.opc.api.configuration.CustomConfigurable;
import me.opkarol.opc.api.configuration.CustomConfiguration;
import me.opkarol.opc.api.location.OpSerializableLocation;
import me.opkarol.opc.api.runnable.OpRunnable;
import me.opkarol.opc.api.utils.StringUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static me.opkarol.opc.api.utils.Util.getOrDefault;

public class OpParticle implements CustomConfigurable, Serializable {
    private float offsetX, offsetY, offsetZ;
    private int amount;
    private Particle particle;
    private OpSerializableLocation location;
    private List<Player> receivers;
    private OpRunnable animatedTask;

    public OpParticle(Particle particle) {
        this.particle = particle;
    }

    public OpParticle() { }

    public OpParticle setOffset(float offsetX, float offsetY, float offsetZ) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        return this;
    }

    public String getOffset() {
        return String.format("%s;%s;%s", offsetX, offsetY, offsetZ);
    }

    public OpParticle setOffset(String offset) {
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

    public OpParticle setOffset(@NotNull Vector offset) {
        this.offsetX = (float) offset.getX();
        this.offsetY = (float) offset.getY();
        this.offsetZ = (float) offset.getZ();
        return this;
    }

    public Particle getParticle() {
        return particle;
    }

    public OpParticle setParticle(Particle particle) {
        this.particle = particle;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public OpParticle setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public OpParticle setOffsetX(float offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public OpParticle setOffsetY(float offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public float getOffsetZ() {
        return offsetZ;
    }

    public OpParticle setOffsetZ(float offsetZ) {
        this.offsetZ = offsetZ;
        return this;
    }

    public OpSerializableLocation getLocation() {
        return location;
    }

    public OpParticle setLocation(OpSerializableLocation opLocation) {
        this.location = opLocation;
        return this;
    }

    public OpParticle setLocation(Location location) {
        this.location = new OpSerializableLocation(location);
        return this;
    }

    public List<Player> getReceivers() {
        return receivers;
    }

    public OpParticle setReceivers(List<Player> receivers) {
        this.receivers = receivers;
        return this;
    }

    public OpParticle addReceiver(Player player) {
        List<Player> list = getOrDefault(receivers, new ArrayList<>());
        list.add(player);
        return setReceivers(list);
    }

    public OpParticle display() {
        return display(getReceivers());
    }

    public <T> OpParticle display(T specialData) {
        return display(getReceivers(), specialData);
    }

    public <T> OpParticle display(List<Player> players, T specialData) {
        if (players == null) {
            return this;
        }

        if (location != null) {
            players.forEach(player -> player.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), amount, offsetX, offsetY, offsetZ, specialData));
        }
        return this;
    }

    public OpParticle display(Player player) {
        if (player == null) {
            return this;
        }
        if (location != null) {
            player.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), amount, offsetX, offsetY, offsetZ);
        }
        return this;
    }

    public OpParticle display(List<Player> players) {
        if (players == null) {
            return this;
        }

        players.forEach(this::display);
        return this;
    }

    public OpParticle animateDisplay(int amount, int delay) {
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

    public OpParticle displayByNearPlayers(Player byPlayer, int reach) {
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

    @Override
    public Consumer<CustomConfiguration> get() {
        return c -> {
            this.setOffset(c.getString("offset"));
            this.location = c.getLocation("location");
            this.particle = c.getUnsafeEnum("particle", Particle.class);
            this.amount = c.getInt("amount");
        };
    }

    @Override
    public Consumer<CustomConfiguration> save() {
        return c -> c.setString("offset", getOffset())
                .setLocation("location", location)
                .setEnum("particle", particle)
                .setInt("amount", amount).save();
    }
}