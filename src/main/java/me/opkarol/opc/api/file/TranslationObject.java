package me.opkarol.opc.api.file;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Deprecated
/*
  Deprecated, due to SimpleTranslation better use and multi translations per class storage.

  @see SimpleTranslation
 */
public record TranslationObject(String pattern, String replace) {

    @Contract(pure = true)
    public @NotNull String replace(@NotNull String s) {
        return s.replace(pattern, replace);
    }
}
