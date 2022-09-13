package me.opkarol.opc.api.misc;

import me.opkarol.opc.api.configuration.CustomConfiguration;
import me.opkarol.opc.api.configuration.IEmptyConfiguration;
import me.opkarol.opc.api.list.OpList;
import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.Consumer;

import static me.opkarol.opc.api.utils.VariableUtil.getOrDefault;

public class OpTitle implements Serializable, IEmptyConfiguration {
    private String title;
    private String subtitle;
    private String tempTitle;
    private String tempSubTitle;
    private int fadeIn, fadeOut, stay;
    private OpList<Player> receivers;

    public OpTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subtitle = subTitle;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
        this.stay = stay;
    }

    public OpTitle() { }

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
        return getOrDefault(tempSubTitle, subtitle);
    }

    public OpTitle setSubTitle(String change, String changeInto) {
        this.tempSubTitle = subtitle.replace(change, changeInto);
        return this;
    }

    public OpTitle setSubTitle(String subTitle) {
        this.subtitle = subTitle;
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

    public OpList<Player> getReceivers() {
        return receivers;
    }

    public OpTitle setReceivers(OpList<Player> receivers) {
        this.receivers = receivers;
        return this;
    }

    public OpTitle addReceiver(Player player) {
        OpList<Player> list = getReceivers();
        if (getReceivers() == null) {
            list = new OpList<>();
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

    public OpTitle display(OpList<Player> players) {
        if (players == null) {
            return this;
        }

        players.forEach(this::display);
        return this;
    }

    public OpTitle display() {
        return display(receivers);
    }

    @Override
    public Consumer<CustomConfiguration> get() {
        return c -> {
            this.title = c.getString("title");
            this.subtitle = c.getString("subtitle");
            this.fadeIn = c.getInt("fadeIn");
            this.stay = c.getInt("stay");
            this.fadeOut = c.getInt("fadeOut");
        };
    }

    @Override
    public Consumer<CustomConfiguration> save() {
        return c -> c.setString("title", title)
                .setString("subtitle", subtitle)
                .setInt("fadeIn", fadeIn)
                .setInt("stay", stay)
                .setInt("fadeOut", fadeOut)
                .save();
    }

    @Override
    public String toString() {
        return "OpTitle{" +
                "title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", tempTitle='" + tempTitle + '\'' +
                ", tempSubTitle='" + tempSubTitle + '\'' +
                ", fadeIn=" + fadeIn +
                ", fadeOut=" + fadeOut +
                ", stay=" + stay +
                ", receivers=" + receivers +
                '}';
    }

    @Override
    public boolean isEmpty() {
        return fadeIn == -1 || stay == -1 || fadeOut == -1 || title == null;
    }
}
