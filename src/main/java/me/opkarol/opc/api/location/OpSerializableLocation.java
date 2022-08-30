package me.opkarol.opc.api.location;

import me.opkarol.opc.api.configuration.CustomConfigurable;
import me.opkarol.opc.api.configuration.CustomConfiguration;
import me.opkarol.opc.api.utils.StringUtil;
import me.opkarol.opc.api.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.UUID;
import java.util.function.Consumer;

public class OpSerializableLocation implements Serializable, CustomConfigurable {
    private double x, y, z;
    private float pitch, yaw;
    private String world;
    private UUID worldUUID;
    private OpSerializableLocation lastLocation;

    public OpSerializableLocation(double x, double y, double z, float pitch, float yaw, @NotNull World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.world = world.getName();
        this.worldUUID = world.getUID();
    }

    public OpSerializableLocation(@NotNull Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
        World world1 = location.getWorld();
        if (world1 == null) {
            return;
        }
        this.world = world1.getName();
        this.worldUUID = world1.getUID();
    }

    public OpSerializableLocation(String string) {
        if (string != null && string.length() != 0) {
            String[] params = string.split(";");
            if (params.length == 6) {
                x = StringUtil.getDouble(params[0]);
                y = StringUtil.getDouble(params[1]);
                z = StringUtil.getDouble(params[2]);
                pitch = StringUtil.getFloat(params[3]);
                yaw = StringUtil.getFloat(params[4]);
                world = params[5];
                return;
            }
        }

        x = 0;
        y = 0;
        z = 0;
        pitch = 0;
        yaw = 0;
        world = null;
    }

    public OpSerializableLocation(@NotNull OpLocation location) {
         this(location.toString());
    }

    public OpSerializableLocation() { }

    public double getX() {
        return x;
    }

    public String getStringX() {
        return String.valueOf(getX());
    }

    public double getY() {
        return y;
    }

    public String getStringY() {
        return String.valueOf(getY());
    }

    public double getZ() {
        return z;
    }

    public String getStringZ() {
        return String.valueOf(getZ());
    }

    public float getPitch() {
        return pitch;
    }

    public String getStringPitch() {
        return String.valueOf(getPitch());
    }

    public float getYaw() {
        return yaw;
    }

    public String getStringYaw() {
        return String.valueOf(getYaw());
    }

    public World getWorld() {
        if (worldUUID != null) {
            return Bukkit.getWorld(worldUUID);
        }
        return Bukkit.getWorld(world);
    }

    public String getStringWorld() {
        if (world == null) {
            return "null";
        }

        return world;
    }

    public Location getLocation() {
        return new Location(getWorld(), x, y, z, yaw, pitch);
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s;%s;%s", getStringX(), getStringY(), getStringZ(), getStringPitch(), getStringYaw(), getStringWorld());
    }

    public OpSerializableLocation getLastLocation() {
        return Util.getOrDefault(lastLocation, setLastLocation());
    }

    public OpSerializableLocation setLastLocation() {
        this.lastLocation = new OpSerializableLocation(toString());
        return this.lastLocation;
    }

    public OpLocation toLocation() {
        return new OpLocation(toString());
    }

    public OpSerializableLocation getHighestYLocation() {
        Location location = getLocation();
        location.setY(getWorld().getHighestBlockYAt((int) getX(), (int) getZ()) + 1);
        return new OpSerializableLocation(location);
    }

    public String toFamilyString() {
        return String.format("X: %s Y: %s Z: %s World: %s", getStringX(), getStringY(), getStringZ(), getStringWorld());
    }

    @Override
    public Consumer<CustomConfiguration> get() {
        return c -> {
            this.x = c.getDouble("x");
            this.y = c.getDouble("y");
            this.z = c.getDouble("z");
            this.yaw = c.getFloat("yaw");
            this.pitch = c.getFloat("pitch");
            this.world = c.getString("world");
        };
    }

    @Override
    public Consumer<CustomConfiguration> save() {
        return c -> c.setDouble("x", x)
                .setDouble("y", y)
                .setDouble("z", z)
                .setFloat("yaw", yaw)
                .setFloat("pitch", pitch)
                .setString("world", world);
    }
}
