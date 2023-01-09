package me.opkarol.opc.api.tools.location;

import me.opkarol.opc.api.serialization.Serialize;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.utils.StringUtil;
import me.opkarol.opc.api.utils.VariableUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class OpSerializableLocation extends Serialize {
    private double x, y, z;
    private float pitch, yaw;
    private String world;
    private UUID worldUUID;
    private OpSerializableLocation lastLocation;

    public OpSerializableLocation(double x, double y, double z, float pitch, float yaw, World world) {
        super(null);
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        if (world != null) {
            this.world = world.getName();
            this.worldUUID = world.getUID();
        }
    }

    public OpSerializableLocation(@NotNull Location location) {
        super(null);
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
        super(null);
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

    public OpSerializableLocation() {
        super(null);
    }

    public OpSerializableLocation(OpMap<String, Object> objects) {
        super(null);
        objects.getByKey("x").ifPresent(x -> setX((Double) x));
        objects.getByKey("y").ifPresent(y -> setY((Double) y));
        objects.getByKey("z").ifPresent(z -> setZ((Double) z));
        objects.getByKey("yaw").ifPresent(yaw -> setYaw((Float) yaw));
        objects.getByKey("pitch").ifPresent(pitch -> setPitch((Float) pitch));
        objects.getByKey("world").ifPresent(world -> setWorld((String) world));
    }

    public double getX() {
        return x;
    }

    public String getStringX() {
        return String.valueOf(getX());
    }

    public String getShortX() {
        return String.valueOf((int) getX());
    }

    public double getY() {
        return y;
    }

    public String getStringY() {
        return String.valueOf(getY());
    }

    public String getShortY() {
        return String.valueOf((int) getY());
    }

    public double getZ() {
        return z;
    }

    public String getStringZ() {
        return String.valueOf(getZ());
    }

    public String getShortZ() {
        return String.valueOf((int) getZ());
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
        return VariableUtil.getOrDefault(lastLocation, setLastLocation());
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
        return String.format("X: %s Y: %s Z: %s World: %s", getShortX(), getShortY(), getShortZ(), getStringWorld());
    }

    public boolean isNotValid() {
        return toString().equals("0.0;0.0;0.0;0.0;0.0;null");
    }

    @Override
    public @NotNull OpMap<String, Object> serialize() {
        return getMapBuilder()
                .setValue("x", x)
                .setValue("y", y)
                .setValue("z", z)
                .setValue("yaw", yaw)
                .setValue("pitch", pitch)
                .setValue("world", world);
    }

    public void setLastLocation(OpSerializableLocation lastLocation) {
        this.lastLocation = lastLocation;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public void setWorldUUID(UUID worldUUID) {
        this.worldUUID = worldUUID;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
