package me.opkarol.opc.api.wrappers;

import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.map.OpMapBuilder;
import me.opkarol.opc.api.serialization.SerializableName;
import me.opkarol.opc.api.serialization.Serialize;
import me.opkarol.opc.api.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;

@SerializableName("OpText")
public final class OpText extends Serialize {
    private String text;

    public OpText(String text) {
        super(null);
        this.text = text;
    }

    public OpText() {
        super(null);
        this.text = null;
    }

    public OpText(@NotNull OpMapBuilder<String, Object> objects) {
        super(null);
        objects.getByKey("text").ifPresent(text -> setText((String) text));
    }

    public @NotNull String getFormattedText() {
        if (text == null) {
            return "";
        }
        return FormatUtils.formatMessage(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public @NotNull OpMap<String, Object> serialize() {
        return getMapBuilder()
                .setValue("text", text);
    }

    @Override
    public String toString() {
        return "OpText{" +
                "text='" + text + '\'' +
                '}';
    }
}
