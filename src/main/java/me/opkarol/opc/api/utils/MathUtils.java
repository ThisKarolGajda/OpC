package me.opkarol.opc.api.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MathUtils {

    public static int getRandomInt(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public static void removeItems(Inventory inventory, Material type, int amount) {
        if (amount <= 0) {
            return;
        }
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack itemStack = inventory.getItem(slot);
            if (itemStack == null || type != itemStack.getType()) {
                continue;
            }
            int newAmount = itemStack.getAmount() - amount;
            if (newAmount > 0) {
                itemStack.setAmount(newAmount);
                break;
            } else {
                inventory.clear(slot);
                amount = -newAmount;
                if (amount == 0) {
                    break;
                }
            }
        }
    }
}
