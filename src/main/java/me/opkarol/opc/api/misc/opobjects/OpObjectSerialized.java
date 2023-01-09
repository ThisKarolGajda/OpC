package me.opkarol.opc.api.misc.opobjects;

import me.opkarol.opc.api.serialization.Serialize;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.map.OpMapBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

public class OpObjectSerialized extends Serialize {
    private final Object object;

    public OpObjectSerialized(OpMapBuilder<String, Object> objects) {
        super(objects);
        this.object = objects.unsafeGet("");
    }

    public OpObjectSerialized(Object object) {
        super(null);
        this.object = object;
    }

    @Override
    public @NotNull OpMap<String, Object> serialize() {
        return getMapBuilder()
                .setValue("", object.toString());
    }

    public <W> Optional<W> getOptional() {
        try {
            return Optional.of((W) object);
        } catch (Throwable ex) {
            return Optional.empty();
        }
    }

    public <W> void ifPresentThen(Consumer<? super W> consumer) {
        getOptional().ifPresent((Consumer<? super Object>) consumer);
    }

    public Object get() {
        return object;
    }
}
