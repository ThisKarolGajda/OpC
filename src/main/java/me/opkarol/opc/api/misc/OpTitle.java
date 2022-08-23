package me.opkarol.opc.api.misc;

import me.opkarol.opc.api.files.Configuration;
import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static me.opkarol.opc.api.utils.Util.getOrDefault;

public class OpTitle implements Serializable {
    private String title;
    private String subTitle;
    private String tempTitle;
    private String tempSubTitle;
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

    public OpTitle(@NotNull FileConfiguration config, String path) {
        path = path.endsWith(".") ? path : path.concat(".");
        this.title = config.getString(path + "title");
        this.subTitle = config.getString(path + "subtitle");
        this.fadeIn = config.getInt(path + "fadeIn");
        this.stay = config.getInt(path + "stay");
        this.fadeOut = config.getInt(path + "fadeOut");
    }

    public void saveInConfiguration(@NotNull Configuration configuration, String path) {
        FileConfiguration config = configuration.getConfig();
        path = path.endsWith(".") ? path : path.concat(".");
        config.set(path + "title", getOrDefault(title, ""));
        config.set(path + "subtitle", getOrDefault(subTitle, ""));
        config.set(path + "fadeIn", getOrDefault(fadeIn, 0));
        config.set(path + "stay", getOrDefault(stay, 0));
        config.set(path + "fadeOut", getOrDefault(fadeOut, 0));
        configuration.save();
    }

    public String getTitle() {
        return getOrDefault(tempTitle, title);
    }

    public OpTitle setTitle(String title) {
        this.title = title;
        return this;
    }

    public OpTitle setTitle(String change, String changeInto) {
        this.tempTitle = title.replace(change, changeInto);
        return this;
    }

    public String getSubTitle() {
        return getOrDefault(tempSubTitle, subTitle);
    }

    public OpTitle setSubTitle(String change, String changeInto) {
        this.tempSubTitle = subTitle.replace(change, changeInto);
        return this;
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

    public void clearTempString() {
        this.tempTitle = null;
        this.tempSubTitle = null;
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
        player.sendTitle(FormatUtils.formatMessage(getTitle()), FormatUtils.formatMessage(getSubTitle()), fadeIn, stay, fadeOut);
        clearTempString();
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
