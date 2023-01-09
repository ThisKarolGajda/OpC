package me.opkarol.opc.api.misc.opobjects;

import me.opkarol.opc.api.list.OpList;
import me.opkarol.opc.api.tools.runnable.OpRunnable;
import me.opkarol.opc.api.tools.text.OpComponent;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class OpActionBar implements Serializable {
    private String text;
    private OpComponent actionBar;
    private OpList<Player> receivers;
    private OpRunnable runnable;

    public OpActionBar(OpComponent actionBar) {
        this.actionBar = actionBar;
    }

    public OpActionBar(String text) {
        this.text = text;
    }

    public OpActionBar send() {
        receivers.forEach(this::send);
        return this;
    }

    public OpActionBar send(@NotNull Player player) {
        return send(player, actionBar);
    }

    public OpActionBar send(Player player, OpComponent component) {
        if (actionBar == null) {
            return this;
        }
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component.build());
        return this;
    }

    public OpActionBar send(Player player, String text) {
        return send(player, new OpComponent(text));
    }

    public OpComponent getActionBar() {
        return actionBar;
    }

    public OpActionBar setActionBar(OpComponent actionBar) {
        this.actionBar = actionBar;
        return this;
    }

    public OpList<Player> getReceivers() {
        return receivers;
    }

    public OpActionBar setReceivers(OpList<Player> receivers) {
        this.receivers = receivers;
        return this;
    }

    public OpActionBar addReceiver(Player player) {
        OpList<Player> list = getReceivers();
        if (getReceivers() == null) {
            list = new OpList<>();
        }
        list.add(player);
        setReceivers(list);
        return this;
    }

    public OpActionBar sendLooped(int times) {
        final int[] i = {times};
        runnable = new OpRunnable(r -> {
            if (i[0] < 1) {
                sendEmpty();
                r.cancel();
            } else {
                build(text.replace("%time%", String.valueOf(i[0]))).send();
            }
            i[0]--;
        }).runTaskTimerAsynchronously(0, 20);
        return this;
    }

    public OpActionBar sendEmpty() {
        receivers.forEach(player -> send(player, new OpComponent("&l")));
        return this;
    }

    public void cancel() {
        runnable.cancel();
    }

    public String getText() {
        return text;
    }

    public OpActionBar setText(String text) {
        this.text = text;
        return this;
    }

    public OpActionBar build(String text) {
        actionBar = new OpComponent(text);
        return this;
    }

    public OpActionBar build() {
        return build(text);
    }

}
