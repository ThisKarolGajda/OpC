package me.opkarol.opc.api.commands.arguments;

import me.opkarol.opc.api.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SoundArg extends OpCommandArg {
    public SoundArg(String name) {
        super(name);
    }

    @Override
    public @Nullable Object getObject(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof Sound) {
            return object;
        }

        if (object instanceof String s) {
            s = s.toUpperCase().replace(".", "_");
            Optional<Sound> optional = StringUtil.getEnumValue(s, Sound.class);
            if (optional.isPresent()) {
                return optional.get();
            }
        }

        return null;
    }
}
