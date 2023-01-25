package me.opkarol.opc.api.utils;

/*
 = Copyright (c) 2021-2022.
 = [OpPets] ThisKarolGajda
 = Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 = http://www.apache.org/licenses/LICENSE-2.0
 = Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

import me.clip.placeholderapi.PlaceholderAPI;
import me.opkarol.opc.api.list.OpList;
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
    private static final Pattern HEX_PATTERN = Pattern.compile("#<[A-f0-9]{6}>");
    private static final Pattern BOLD_HEX_PATTERN = Pattern.compile("&l#<[A-f0-9]{6}>");

    private static final char COLOR_CHAR = '§';

    public static @NotNull String formatMessage(String message) {
        try {
            return format(hexFormatMessage(gradient(message)));
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

    public static @NotNull OpList<String> format(List<String> list) {
        if (list == null) {
            return new OpList<>();
        }
        return list.stream()
                .map(FormatUtils::formatMessage)
                .collect(OpList.getCollector());
    }

    public static @NotNull List<String> formatList(List<String> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        return list.stream()
                .map(FormatUtils::formatMessage)
                .collect(Collectors.toList());
    }

    public static String scrapMessage(String input) {
        return ChatColor.stripColor(input);
    }

    private static @NotNull String hexFormatMessage(String message) {
        StringBuilder buffer1 = new StringBuilder(message.length() + 32);
        Matcher matcher2 = BOLD_HEX_PATTERN.matcher(message);
        while (matcher2.find()) {
            String group = matcher2.group(0);
            matcher2.appendReplacement(buffer1, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
                    + COLOR_CHAR + group.charAt(6) + COLOR_CHAR + group.charAt(7)
                    + COLOR_CHAR + group.charAt(8) + COLOR_CHAR + group.charAt(9) + "&l");
        }

        StringBuilder buffer2 = new StringBuilder(message.length() + 32);
        Matcher matcher1 = HEX_PATTERN.matcher(matcher2.appendTail(buffer1).toString());
        while (matcher1.find()) {
            String group = matcher1.group(0);
            matcher1.appendReplacement(buffer2, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
                    + COLOR_CHAR + group.charAt(6) + COLOR_CHAR + group.charAt(7));
        }

        return matcher1.appendTail(buffer2).toString();
    }

    public static String gradient(String start, String end, String text) {
        start = start.replace("<", "").replace(">", "");
        end = end.replace("<", "").replace(">", "");
        int r1 = Integer.valueOf(start.substring(1, 3), 16);
        int g1 = Integer.valueOf(start.substring(3, 5), 16);
        int b1 = Integer.valueOf(start.substring(5), 16);
        int r2 = Integer.valueOf(end.substring(1, 3), 16);
        int g2 = Integer.valueOf(end.substring(3, 5), 16);
        int b2 = Integer.valueOf(end.substring(5), 16);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            int r = (int) (r1 + (r2 - r1) * (i / (text.length() - 1.0)));
            int g = (int) (g1 + (g2 - g1) * (i / (text.length() - 1.0)));
            int b = (int) (b1 + (b2 - b1) * (i / (text.length() - 1.0)));
            String hex = String.format("#<%02x%02x%02x>", r, g, b);
            builder.append(hex).append(text.charAt(i));
        }
        return builder.toString();
    }

    public static String gradient(String message) {
        String pattern = "#!<(\\w+)>(.*?)#!<(\\w+)>";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(message);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (m.find()) {
            String start = m.group(1);
            String end = m.group(3);
            String text = m.group(2);
            sb.append(message, i, m.start());
            sb.append(gradient("#" + start, "#" + end, text));
            i = m.end();
        }
        sb.append(message.substring(i));
        return sb.toString();
    }
}
