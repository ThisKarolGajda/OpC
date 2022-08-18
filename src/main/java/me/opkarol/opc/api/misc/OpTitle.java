package me.opkarol.opc.api.misc;

import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OpTitle implements Serializable {
    private String title;
    private String subTitle;
    private int fadeIn, fadeOut, stay;
    private List<Player> receivers;

    public OpTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
        this.stay = stay;
    }

    public OpTitle() { }

    public String getTitle() {
        return title;
    }

    public OpTitle setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public OpTitle setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public OpTitle setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
        return this;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public OpTitle setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
        return this;
    }

    public int getStay() {
        return stay;
    }

    public OpTitle setStay(int stay) {
        this.stay = stay;
        return this;
    }

    public List<Player> getReceivers() {
        return receivers;
    }

    public OpTitle setReceivers(List<Player> receivers) {
        this.receivers = receivers;
        return this;
    }

    public OpTitle addReceiver(Player player) {
        List<Player> list = getReceivers();
        if (getReceivers() == null) {
            list = new ArrayList<>();
        }
        list.add(player);
        setReceivers(list);
        return this;
    }

    public OpTitle display(@NotNull Player player) {
        player.sendTitle(FormatUtils.formatMessage(title), FormatUtils.formatMessage(subTitle), fadeIn, stay, fadeOut);
        return this;
    }

    public OpTitle display(List<Player> players) {
        if (players == null) {
            return this;
        }

        players.forEach(this::display);
        return this;
    }

    public OpTitle display() {
        return display(receivers);
    }
}
