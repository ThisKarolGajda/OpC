package me.opkarol.opc.api.command.arguments;

import me.opkarol.opc.api.command.types.IType;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.Nullable;

public class EnchantArg<I extends IType> extends OpTypeArg<I> {

    public EnchantArg(I name) {
        super(name);
    }

    @Override
    public @Nullable Object getObject(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof Enchantment) {
            return object;
        }

        if (object instanceof String) {
            return Enchantment.getByKey(NamespacedKey.minecraft(object.toString()));
        }

        return null;
    }
}
