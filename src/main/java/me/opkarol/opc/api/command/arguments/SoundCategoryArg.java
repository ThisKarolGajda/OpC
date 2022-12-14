package me.opkarol.opc.api.command.arguments;

import me.opkarol.opc.api.command.types.IType;
import me.opkarol.opc.api.utils.StringUtil;
import org.bukkit.SoundCategory;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SoundCategoryArg <I extends IType> extends OpTypeArg<I> {
    public SoundCategoryArg(I name) {
        super(name);
    }

    @Override
    public @Nullable Object getObject(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof SoundCategory) {
            return object;
        }

        if (object instanceof String) {
            Optional<SoundCategory> optional = StringUtil.getEnumValue((String) object, SoundCategory.class);
            if (optional.isPresent()) {
                return optional.get();
            }
        }

        return null;
    }
}
