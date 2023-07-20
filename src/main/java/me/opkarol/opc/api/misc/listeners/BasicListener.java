package me.opkarol.opc.api.misc.listeners;

import me.opkarol.opc.api.plugins.OpPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.io.Serializable;

public class BasicListener implements IListener, Listener, Serializable {
    @Override
    public void runListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, OpPlugin.getInstance());
    }

    @Override
    public void stopListener() {
        HandlerList.unregisterAll(this);
    }
}