package me.opkarol.opc.api.head;

import me.opkarol.opc.api.map.OpMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static me.opkarol.opc.api.utils.Util.getOrDefault;

public class HeadDatabase{
    private static HeadDatabase headDatabase;
    private final OpMap<String, ItemStack> map = new OpMap<>();

    public HeadDatabase() {
        headDatabase = this;
    }

    public static HeadDatabase getInstance() {
        return getOrDefault(headDatabase, new HeadDatabase());
    }

    public ItemStack getHead(String owner) {
        Optional<ItemStack> itemStack = map.getByKey(owner);
        return itemStack.orElseGet(() -> getSkull(owner));
    }

    public void setHead(String owner, ItemStack itemStack) {
        map.set(owner, itemStack);
    }

    private @Nullable ItemStack getSkull(String owner) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta == null) {
            return null;
        }
        meta.setOwner(owner);
        head.setItemMeta(meta);
        setHead(owner, head);
        return head;
    }
}