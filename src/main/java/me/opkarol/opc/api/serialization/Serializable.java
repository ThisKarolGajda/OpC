package me.opkarol.opc.api.serialization;

import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.map.OpMapBuilder;
import org.jetbrains.annotations.NotNull;

public interface Serializable {

    @NotNull
    OpMap<String, Object> serialize();

    default OpMapBuilder<String, Object> getMapBuilder() {
        OpMapBuilder<String, Object> builder = new OpMapBuilder<>();
        builder.set(Serialization.SERIALIZED_TYPE_KEY, getClass().getName());
        return builder;
    }
}
