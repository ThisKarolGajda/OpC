package me.opkarol.opc.api.events;

import me.opkarol.opc.OpAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public class EventRegister {
    private static final Plugin opC = OpAPI.getInstance();

    public static <E extends Event> void registerEvent(Class<E> clazz, Consumer<E> consumer) {
        registerEvent(clazz, EventPriority.NORMAL, consumer);
    }

    public static <E extends Event> void registerEvent(Class<E> clazz, EventPriority priority, Consumer<E> consumer) {
        if (canRegister()) {
            opC.getServer().getPluginManager()
                    .registerEvent(clazz, new Listener() {
                            }, priority,
                            (l, e) -> consumer.accept((E) e), opC, true);
        }
    }

    public static boolean canRegister() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled(OpAPI.getInstance());
    }
}
