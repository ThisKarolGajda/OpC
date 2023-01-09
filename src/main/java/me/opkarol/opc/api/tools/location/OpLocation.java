package me.opkarol.opc.api.tools.location;

/*
 * Copyright (c) 2021-2022.
 * [OpPets] ThisKarolGajda
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

import me.opkarol.opc.api.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class OpLocation {
    private final double x, y, z;
    private final float pitch, yaw;
    private final World world;
    private OpLocation lastLocation;

    public OpLocation(double x, double y, double z, float pitch, float yaw, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.world = world;
    }

    public OpLocation(@NotNull Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
        this.world = location.getWorld();
    }

    public OpLocation(String string) {
        if (string != null && string.length() != 0) {
            String[] params = string.split(";");
            if (params.length == 6) {
                x = StringUtil.getDouble(params[0]);
                y = StringUtil.getDouble(params[1]);
                z = StringUtil.getDouble(params[2]);
                pitch = StringUtil.getFloat(params[3]);
                yaw = StringUtil.getFloat(params[4]);
                world = Bukkit.getWorld(params[5]);
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

    public OpLocation(@NotNull OpSerializableLocation location) {
        this(location.toString());
    }

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
        return world;
    }

    public String getStringWorld() {
        if (getWorld() == null) {
            return "null";
        }
        return getWorld().getName();
    }

    public Location getLocation() {
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s;%s;%s", getStringX(), getStringY(), getStringZ(), getStringPitch(), getStringYaw(), getStringWorld());
    }

    public String toFamilyString() {
        return String.format("X: %s Y: %s Z: %s World: %s", getStringX(), getStringY(), getStringZ(), getStringWorld());
    }

    public OpLocation getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation() {
        this.lastLocation = new OpLocation(toString());
    }

    public OpSerializableLocation toLocation() {
        return new OpSerializableLocation(toString());
    }

    public OpSerializableLocation getHighestYLocation() {
        Location location = getLocation();
        location.setY(getWorld().getHighestBlockYAt((int) getX(), (int) getZ()));
        return new OpSerializableLocation(location);
    }
}
