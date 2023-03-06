package me.opkarol.opc.api.file;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SimpleTranslation {
    private final String[][] strings;

    public SimpleTranslation(String[]... strings) {
        this.strings = strings;
    }

    public SimpleTranslation(String replace, String with) {
        this.strings = new String[][]{{replace, with}};
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull SimpleTranslation of(String replace, String with) {
        return new SimpleTranslation(replace, with);
    }

    public String[][] getStrings() {
        return strings;
    }
}
