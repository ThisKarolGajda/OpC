package me.opkarol.opc.api.tools.text;

import me.opkarol.opc.api.utils.FormatUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;

public class OpComponent {
    private String message;
    private HoverEvent hoverEvent;
    private ClickEvent clickEvent;

    public OpComponent(String message) {
        this.message = message;
    }

    public TextComponent build() {
        TextComponent temp = new TextComponent(FormatUtils.formatMessage(message));
        if (hoverEvent != null) {
            temp.setHoverEvent(hoverEvent);
        }

        if (clickEvent != null) {
            temp.setClickEvent(clickEvent);
        }
        return temp;
    }

    public String getMessage() {
        return message;
    }

    public OpComponent setMessage(String message) {
        this.message = message;
        return this;
    }

    public OpComponent setHoverEvent(HoverEvent.Action action, Content content) {
        this.hoverEvent = new HoverEvent(action, content);
        return this;
    }

    public OpComponent setHoverEvent(HoverEvent.Action action, String text) {
        return setHoverEvent(action, getTextContent(FormatUtils.formatMessage(text)));
    }

    public OpComponent setClickEvent(ClickEvent.Action action, String string) {
        this.clickEvent = new ClickEvent(action, string);
        return this;
    }

    public Content getTextContent(String s) {
        return new Text(s);
    }

    public Content getItemContent(String id, int count, ItemTag tag) {
        return new Item(id, count, tag);
    }
}
