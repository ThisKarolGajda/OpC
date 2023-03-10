package me.opkarol.opc.api.tools;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.opkarol.opc.api.gui.OpInventory;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.tools.runnable.OpRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class HeadManager {
    private static final OpMap<UUID, String> HEAD_CACHE = new OpMap<>();

    public static @Nullable String getHeadValue(String name) {
        try {
            String result = getURLContent("https://api.mojang.com/users/profiles/minecraft/" + name);
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(result, JsonObject.class);
            String id = obj.get("id").toString().replace("\"", "");
            String signature = getURLContent("https://sessionserver.mojang.com/session/minecraft/profile/" + id);
            obj = gson.fromJson(signature, JsonObject.class);
            String value = obj.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
            String decoded = new String(Base64.getDecoder().decode(value));
            obj = gson.fromJson(decoded, JsonObject.class);
            String skinURL = obj.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
            byte[] skinByte = ("{\"textures\":{\"SKIN\":{\"url\":\"" + skinURL + "\"}}}").getBytes();
            return new String(Base64.getEncoder().encode(skinByte));
        } catch (Exception ignored) {
        }
        return null;
    }

    private static @NotNull String getURLContent(String urlStr) {
        URL url;
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try {
            url = new URL(urlStr);
            in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String str;
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
        } catch (Exception ignored) {
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ignored) {
            }
        }
        return sb.toString();
    }

    private static ItemStack getHead(@NotNull String value) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        UUID hashAsId = new UUID(value.hashCode(), value.hashCode());
        return Bukkit.getUnsafe().modifyItemStack(skull,
                "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + value + "\"}]}}}"
        );
    }

    public static void addHeadToPlayerInventory(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        if (HEAD_CACHE.containsKey(uuid)) {
            player.getInventory().addItem(getHead(HEAD_CACHE.unsafeGet(uuid)));
            return;
        }
        new OpRunnable(() -> {
            String value;
            value = getHeadValue(player.getName());
            if (value == null) {
                value = "";
            }
            ItemStack item = getHead(value);
            player.getInventory().addItem(item);
            HEAD_CACHE.set(uuid, value);
        }).runTaskAsynchronously();
    }

    public static ItemStack getHead(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        if (HEAD_CACHE.containsKey(uuid)) {
            return getHead(HEAD_CACHE.unsafeGet(uuid));
        }
        String value;
        value = getHeadValue(player.getName());
        if (value == null) {
            value = "";
        }
        ItemStack item = getHead(value);
        HEAD_CACHE.set(uuid, value);
        return item;
    }

    public static Optional<String> getCurrentHeadValue(UUID uuid) {
        return HEAD_CACHE.getByKey(uuid);
    }

    public static String getHeadValue(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        Optional<String> optional = getCurrentHeadValue(uuid);
        if (optional.isPresent()) {
            return optional.get();
        }

        String value = getHeadValue(player.getName());
        HEAD_CACHE.set(uuid, value);
        return value;
    }

    public static Optional<String> getCurrentHeadValue(@NotNull Player player) {
        return getCurrentHeadValue(player.getUniqueId());
    }

    public static void setHeadValue(Player player) {
        new OpRunnable(() -> {
            UUID uuid = player.getUniqueId();
            String value = getHeadValue(player.getName());
            HEAD_CACHE.set(uuid, value);
        }).runTaskAsynchronously();
    }

    public static void ifDoesntExistSetHeadValue(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        if (HEAD_CACHE.containsKey(uuid)) {
            return;
        }

        new OpRunnable(() -> {
            String value = getHeadValue(player.getName());
            HEAD_CACHE.set(uuid, value);
        }).runTaskAsynchronously();
    }

    public static void updatePlayerHeadInInventory(@NotNull Player player, OpInventory inventory, int slot, Consumer<ItemStack> itemStackConsumer) {
        new OpRunnable(() -> {
            Future<String> future = CompletableFuture.supplyAsync(() -> getHeadValue(player));

            try {
                String value = future.get(15L, TimeUnit.SECONDS);
                if (inventory.getInventoryHolder().getIHolder() != player.getOpenInventory().getTopInventory().getHolder()) {
                    return;
                }

                if (value == null) {
                    value = "";
                }
                UUID hashAsId = new UUID(value.hashCode(), value.hashCode());
                ItemStack itemStack1 = player.getOpenInventory().getItem(slot);
                if (itemStack1 == null || !itemStack1.getType().equals(Material.PLAYER_HEAD)) {
                    return;
                }
                Bukkit.getUnsafe().modifyItemStack(itemStack1,
                        "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + value + "\"}]}}}");
                itemStackConsumer.accept(itemStack1);
            } catch (InterruptedException | ExecutionException ignore) {} catch (TimeoutException e) {
                new OpRunnable(() -> {
                    player.closeInventory();
                    player.sendMessage("Nie można uzyskać profilu, skontaktuj się z administratorem!");
                }).runTask();
            }
        }).runTaskAsynchronously();
    }
}