package me.opkarol.opc.api.command.types;

import org.jetbrains.annotations.NotNull;

public interface IType {

    int getI();

    default int getAsType(@NotNull IType type) {
        return type.getI();
    }

    default boolean equals(@NotNull IType type) {
        return getAsType(type) == getI();
    }
}
