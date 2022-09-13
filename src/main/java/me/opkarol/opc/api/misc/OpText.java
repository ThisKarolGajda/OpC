package me.opkarol.opc.api.misc;

import me.opkarol.opc.api.configuration.CustomConfiguration;
import me.opkarol.opc.api.configuration.IEmptyConfiguration;
import me.opkarol.opc.api.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

public final class OpText implements Serializable, IEmptyConfiguration {
    private String text;

    public OpText(String text) {
        this.text = text;
    }

    public OpText() {
        this.text = null;
    }

    @Override
    public @NotNull Consumer<CustomConfiguration> get() {
        return c -> text = c.getString("text");
    }

    @Override
    public @NotNull Consumer<CustomConfiguration> save() {
        return c -> c.setString("text", text).save();
    }

    public @NotNull String getFormattedText() {
        if (text == null) {
            return "";
        }
        return FormatUtils.formatMessage(text);
    }

    public String text() {
        return text;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        OpText that = (OpText) obj;
        return Objects.equals(this.text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

    @Override
    public String toString() {
        return "OpText{" +
                "text='" + text + '\'' +
                '}';
    }

    public boolean isEmpty() {
        return text == null || text.isBlank();
    }
}
