package dev.karar.boomerang.utils;

import dev.karar.boomerang.Boomerang;
import dev.karar.boomerang.bone.ItemType;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static boolean isItem(ItemStack item, ItemType itemType) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        if (nmsItem == null) return false;
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) return false;
        return tag.getString("id").equals(itemType.name());
    }

    public static boolean isEntityIgnored(Entity entity) {
        if (entity == null || entity.isDead() || entity instanceof Item || entity instanceof ExperienceOrb || entity instanceof Arrow || entity instanceof ArmorStand) return true;
        if (!Boomerang.getInstance().getConfig().getBoolean("damage-mob") && entity instanceof Monster) return true;
        if (!Boomerang.getInstance().getConfig().getBoolean("damage-villager") && entity instanceof Villager) return true;
        if (!Boomerang.getInstance().getConfig().getBoolean("damage-player") && entity instanceof Player) return true;

        return false;
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        // Iterate through all blocks within radius of location, add to and return list of blocks.
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                if (location.getWorld().getBlockAt(x, (int) location.getY(), z).getType() != Material.AIR)
                    blocks.add(location.getWorld().getBlockAt(x, (int) location.getY(), z));
            }
        }
        return blocks;
    }

    public static int getEmptySlots(Inventory inventory) {
        int i = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType() == Material.AIR) i++;
        }
        return i;
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
