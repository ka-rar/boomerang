package dev.karar.boomerang.bone;

import dev.karar.boomerang.Boomerang;
import dev.karar.boomerang.utils.Utils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public enum ItemType {

    BOOMERANG,
    SEDENTARY_BOOMERANG;

    public ItemStack getItem() {
        // Create item and properly attribute based on config.yml file preferences.
        ItemStack boomerang = new ItemStack(Material.getMaterial(Boomerang.getInstance().getConfig().getString(name() + ".material")), 1, (short) Boomerang.getInstance().getConfig().getInt(name() + ".type"));
        ItemMeta itemMeta = boomerang.getItemMeta().clone();
        itemMeta.setDisplayName(Utils.colorize(Boomerang.getInstance().getConfig().getString(name() + ".name")));
        List<String> itemLore = new ArrayList<>();
        Boomerang.getInstance().getConfig().getStringList(name() + ".lore").forEach((line) -> itemLore.add(Utils.colorize(line)));
        itemMeta.setLore(itemLore);
        Boomerang.getInstance().getConfig().getStringList(name() + ".enchantments").forEach((enchant) -> {
            String type = enchant.split(":")[0];
            int level = Integer.parseInt(enchant.split(":")[1]);
            itemMeta.addEnchant(Enchantment.getByName(type), level, true);
        });
        Boomerang.getInstance().getConfig().getStringList(name() + ".flags").forEach((flag) -> itemMeta.addItemFlags(ItemFlag.valueOf(flag)));
        boomerang.setItemMeta(itemMeta);

        // Fetch NMS and initialize custom metadata.
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(boomerang);
        NBTTagCompound tag = nmsItem.getTag();
        if (Boomerang.getInstance().getConfig().getBoolean(name() + ".unique")) {
            tag.setString("uuid", UUID.randomUUID().toString());
        }
        tag.setString("id", name());
        nmsItem.setTag(tag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }
}
