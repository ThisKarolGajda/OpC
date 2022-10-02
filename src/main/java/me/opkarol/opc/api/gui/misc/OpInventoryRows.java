package me.opkarol.opc.api.gui.misc;

import me.opkarol.opc.api.configuration.CustomConfiguration;
import me.opkarol.opc.api.configuration.IEmptyConfiguration;
import me.opkarol.opc.api.utils.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public enum OpInventoryRows implements IEmptyConfiguration {
    ROWS_1(9),
    ROWS_2(18),
    ROWS_3(27),
    ROWS_4(36),
    ROWS_5(45),
    ROWS_6(54);

    private int i;

    OpInventoryRows(int i) {
        this.i = i;
    }

    public int getSize() {
        return i;
    }

    @Override
    public @NotNull Consumer<CustomConfiguration> get() {
        return c -> i = StringUtil.getInt(c.getUnsafeObject());
    }

    @Override
    public @NotNull Consumer<CustomConfiguration> save() {
        return c -> c.setUnsafeObject(i);
    }

    @Override
    public boolean isEmpty() {
        return i == -1;
    }
}
