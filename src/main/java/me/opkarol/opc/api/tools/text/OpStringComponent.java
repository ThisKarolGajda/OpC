package me.opkarol.opc.api.tools.text;

import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.utils.FormatUtils;
import me.opkarol.opc.api.utils.StringUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public record OpStringComponent(String string) {

    /**
     * Gets hovered message in format using the <HOVER: / start annotation
     * and > / as the end.
     * <p>
     * General format is `<HOVER:DISPLAY_MESSAGE:HOVER_MESSAGE>`
     * where DISPLAY_MESSAGE is a text under which is located component
     * and HOVER_MESSAGE is a text which is shown when a user hovers their mouse on component.
     * Example: "<HOVER:&bThis is what you see in chat:&cAnd this is what you see on hover>"
     *
     * @return the hover event component already built and formatted
     */
    public @NotNull TextComponent getHover() {
        String tempString = string;
        OpMap<String, TextComponent> tempMap = new OpMap<>();
        Pattern pattern = getPattern("<HOVER:>");
        Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            String[] group2 = matcher.group(2).split(":");
            if (group2.length == 2) {
                TextComponent component = new OpComponent(group2[0]).setHoverEvent(HoverEvent.Action.SHOW_TEXT, group2[1]).build();
                String uuid = UUID.randomUUID().toString();
                tempMap.set(uuid, component);
                tempString = tempString.replace(matcher.group(1), uuid);
            }
        }

        TextComponent temp = new TextComponent();
        for (String s : tempString.split(" ")) {
            tempMap.keySet().stream().filter(s::contains).findAny().ifPresentOrElse(group -> {
                int startIndex = s.indexOf(group);
                int endIndex = startIndex + group.length();

                String textBefore = s.substring(0, startIndex);
                if (textBefore.length() != 0) {
                    temp.addExtra(FormatUtils.formatMessage(textBefore));
                }

                temp.addExtra(tempMap.getOrDefault(group, null));

                String textAfter = s.substring(endIndex);
                if (textAfter.length() != 0) {
                    temp.addExtra(FormatUtils.formatMessage(textAfter) + " ");
                }
            }, () -> temp.addExtra(FormatUtils.formatMessage(s) + " "));
        }

        return temp;
    }

    /**
     * Gets clicked message in format using the <CLICK: / start annotation
     * and > / as the end.
     * <p>
     * General format is <CLICK:DISPLAY_MESSAGE:ACTION:CLICK_STRING>
     * where DISPLAY_MESSAGE is a text under which is located component
     * ACTION is a default Bukkit enum action,
     * and CLICK_STRING which is a string object used in provided action.
     * Example: "<CLICK:&bThis is what you see in chat:SUGGEST_COMMAND:/command suggested>"
     *
     * @return the click event component already built and formatted
     */
    public @NotNull TextComponent getClick() {
        final String[] tempString = {string};
        OpMap<String, TextComponent> tempMap = new OpMap<>();
        Pattern pattern = getPattern("<CLICK:>");
        Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            String[] group2 = matcher.group(2).split(":");
            if (group2.length == 3) {
                StringUtil.getEnumValue(group2[1], ClickEvent.Action.class).ifPresent(action -> {
                    TextComponent component = new OpComponent(group2[0]).setClickEvent(action, group2[2]).build();
                    String uuid = UUID.randomUUID().toString();
                    tempMap.set(uuid, component);
                    tempString[0] = tempString[0].replace(matcher.group(1), uuid);
                });
            }
        }

        return getTextComponent(tempString, tempMap);
    }

    public @NotNull TextComponent getTextComponent(String[] tempString, OpMap<String, TextComponent> tempMap) {
        TextComponent temp = new TextComponent();
        for (String s : tempString[0].split(" ")) {
            tempMap.keySet().stream().filter(s::contains).findAny().ifPresentOrElse(group -> {
                int startIndex = s.indexOf(group);
                int endIndex = startIndex + group.length();

                String textBefore = s.substring(0, startIndex);
                if (textBefore.length() != 0) {
                    temp.addExtra(textBefore);
                }

                temp.addExtra(tempMap.getOrDefault(group, null));

                String textAfter = s.substring(endIndex);
                if (textAfter.length() != 0) {
                    temp.addExtra(textAfter + " ");
                }
            }, () -> temp.addExtra(s + " "));
        }

        return temp;
    }

    // <CLICK-HOVER:DISPLAY:HOVER_MESSAGE:CLICK_ACTION:CLICK_STRING>

    /**
     * Gets clicked and hovered message in format using the <CLICK-HOVER: / start annotation
     * and > / as the end.
     * <p>
     * General format is <CLICK-HOVER:DISPLAY_MESSAGE:HOVER_MESSAGE:ACTION:CLICK_STRING>
     * where DISPLAY_MESSAGE is a text under which is located component
     * HOVER_MESSAGE is a text which is shown when a user hovers their mouse on component,
     * ACTION is a default Bukkit enum action,
     * and CLICK_STRING which is a string object used in provided action.
     * Example: "<CLICK:&bThis is what you see:SUGGEST_COMMAND:/command suggested>"
     *
     * @return the click and hover event component already built and formatted
     */
    public @NotNull TextComponent getClickAndHover() {
        final String[] tempString = {string};
        OpMap<String, TextComponent> tempMap = new OpMap<>();
        Pattern pattern = getPattern("<CLICK-HOVER:>");
        Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            String[] group2 = matcher.group(2).split(":");
            if (group2.length == 4) {
                StringUtil.getEnumValue(group2[2], ClickEvent.Action.class).ifPresent(action -> {
                    TextComponent component = new OpComponent(group2[0]).setClickEvent(action, group2[3])
                            .setHoverEvent(HoverEvent.Action.SHOW_TEXT, group2[1]).build();
                    String uuid = UUID.randomUUID().toString();
                    tempMap.set(uuid, component);
                    tempString[0] = tempString[0].replace(matcher.group(1), uuid);
                });
            }
        }

        return getTextComponent(tempString, tempMap);
    }

    @Contract("_ -> new")
    private @NotNull Pattern getPattern(@NotNull String toFind) {
        return Pattern.compile("(" + toFind.substring(0, toFind.length() - 1) + "(.*?)" + toFind.substring(toFind.length() - 1) + ")|('(.*?)')");
    }
}
