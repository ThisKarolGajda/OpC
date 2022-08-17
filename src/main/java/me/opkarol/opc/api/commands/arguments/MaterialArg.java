package me.opkarol.opc.api.commands.arguments;

import me.opkarol.opc.api.utils.StringUtil;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public class MaterialArg extends OpCommandArg {

    public MaterialArg(String name) {
        super(name);
    }

    @Override
    public @Nullable Object getObject(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof Material) {
            return object;
        }

        if (object instanceof String) {
            return StringUtil.getEnumValue((String) object, Material.class);
        }

        return null;
    }
}
