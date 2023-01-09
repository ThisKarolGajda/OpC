package me.opkarol.opc.api.misc.opobjects;

import me.opkarol.opc.api.serialization.SerializableName;
import me.opkarol.opc.api.serialization.Serialize;
import me.opkarol.opc.api.list.OpList;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.map.OpMapBuilder;
import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.opkarol.opc.api.utils.VariableUtil.getOrDefault;

@SerializableName("OpTitle")
public class OpTitle extends Serialize {
    private String title;
    private String subtitle;
    private String tempTitle;
    private String tempSubTitle;
    private int fadeIn, fadeOut, stay;
    private OpList<Player> receivers;

    public OpTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        super(null);
        this.title = title;
        this.subtitle = subTitle;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
        this.stay = stay;
    }

    public OpTitle(@NotNull OpMapBuilder<String, Object> objects) {
        super(null);
        objects.getByKey("title").ifPresent(title -> setTitle((String) title));
        objects.getByKey("subtitle").ifPresent(subtitle -> setSubtitle((String) subtitle));
        objects.getByKey("fadeIn").ifPresent(fadeIn -> setFadeIn((Integer) fadeIn));
        objects.getByKey("stay").ifPresent(stay -> setStay((Integer) stay));
        objects.getByKey("fadeOut").ifPresent(fadeOut -> setFadeOut((Integer) fadeOut));
    }

    public OpTitle() {
        super(null);
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
        return getOrDefault(tempSubTitle, subtitle);
    }

    public OpTitle setSubtitle(String change, String changeInto) {
        return setSubtitle(subtitle.replace(change, changeInto));
    }

    public OpTitle setSubtitle(String subTitle) {
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
    public @NotNull OpMap<String, Object> serialize() {
        return getMapBuilder()
                .setValue("title", title)
                .setValue("subtitle", subtitle)
                .setValue("fadeIn", fadeIn)
                .setValue("stay", stay)
                .setValue("fadeOut", fadeOut);
    }
}
