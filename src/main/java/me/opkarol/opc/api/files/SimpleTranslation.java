package me.opkarol.opc.api.files;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SimpleTranslation {
    private final String[][] strings;

    public SimpleTranslation(String[]... strings) {
        this.strings = strings;
    }

    public SimpleTranslation(String r, String s) {
        this.strings = new String[][]{{r, s}};
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull SimpleTranslation as(String r, String s) {
        return new SimpleTranslation(r, s);
    }

    public String[][] getStrings() {
        return strings;
    }
}
