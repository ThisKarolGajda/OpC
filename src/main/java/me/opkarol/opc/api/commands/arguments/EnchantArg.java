package me.opkarol.opc.api.commands.arguments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.Nullable;

public class EnchantArg extends OpCommandArg {

    public EnchantArg(String name) {
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
