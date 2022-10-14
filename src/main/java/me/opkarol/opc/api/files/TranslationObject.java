package me.opkarol.opc.api.files;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final record TranslationObject(String pattern, String replace) {

    @Contract(pure = true)
    public @NotNull String replace(@NotNull String s) {
        return s.replace(pattern, replace);
    }
}
