package me.opkarol.opc.api.utils;

/*
 = Copyright (c) 2021-2022.
 = [OpPets] ThisKarolGajda
 = Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 = http://www.apache.org/licenses/LICENSE-2.0
 = Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class FormatUtils {
    private static final Pattern HEX_PATTERN = Pattern.compile("[A-f0-9]{6}");
    private static final char COLOR_CHAR = '\u00A7';

    public static @NotNull String formatMessage(String message) {
        try {
            return hexFormatMessage(format(message));
        } catch (Exception ignored) {
            return format(message);
        }
    }

    public static @Nullable String formatMessage(String message, OfflinePlayer player) {
        if (me.opkarol.opc.api.extensions.PlaceholderAPI.getInstance().isEnabled()) {
            return formatMessage(PlaceholderAPI.setPlaceholders(player, message));
        }
        return null;
    }

    private static String format(String s) {
        if (s == null) {
            return null;
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static @NotNull List<String> formatList(List<String> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        return list.stream().map(FormatUtils::formatMessage).collect(Collectors.toList());
    }

    public static String scrapMessage(String input) {
        return ChatColor.stripColor(input);
    }

    private static @NotNull String hexFormatMessage(String message) {
        final Pattern hexPattern = Pattern.compile("#<" + HEX_PATTERN + ">");
        Matcher matcher = hexPattern.matcher(message);
        StringBuilder buffer = new StringBuilder(message.length() + 32);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }
}
