package dev.karar.boomerang.bone;

import dev.karar.boomerang.Boomerang;
import dev.karar.boomerang.bone.animation.ReturnAnimation;
import dev.karar.boomerang.bone.animation.ThrowAnimation;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class Bone {

    public final Player player;
    public final ArmorStand bone;
    public ItemStack heldItem;
    public int heldSlot;

    private int sendID;
    private int returnID;

    public Bone(Player player, ItemStack item, int slot) {
        this.player = player;
        this.bone = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(0, 0.8f, 0), EntityType.ARMOR_STAND);
        this.bone.setVisible(false);
        this.bone.setMarker(true);
        this.bone.setGravity(false);
        this.bone.setRightArmPose(new EulerAngle(270, 0, 0));
        this.bone.getEquipment().setItemInHand(ItemType.BOOMERANG.getItem());

        this.heldItem = item;
        this.heldSlot = slot;
        startSend();
    }

    public void startSend() {
        sendID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Boomerang.getInstance(), new ThrowAnimation(this), 0, 1);
        // Start return animation when boomerang has exhausted lifespan for ticks-throw set in config.yml file.
        Bukkit.getScheduler().runTaskLater(Boomerang.getInstance(), () -> {
            Bukkit.getScheduler().cancelTask(sendID);
            if (Bukkit.getPlayer(player.getUniqueId()) == null) return;

            if (returnID == 0) startReturn();
        }, Boomerang.getInstance().getConfig().getLong("animation.throw.duration"));
    }

    public void startReturn() {
        // Cancel any send task in case return task was called by collision earlier than desired.
        Bukkit.getScheduler().cancelTask(sendID);

        returnID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Boomerang.getInstance(), new ReturnAnimation(this), 0, 1);
        Bukkit.getScheduler().runTaskLater(Boomerang.getInstance(), () -> {
            if (!bone.isDead()) endReturn();
        }, Boomerang.getInstance().getConfig().getLong("animation.return.duration"));
    }

    public void endReturn() {
        Bukkit.getScheduler().cancelTask(returnID);
        die();
        if (Bukkit.getPlayer(player.getUniqueId()) == null) return;
        player.getInventory().setItem(heldSlot, heldItem);
    }

    public void die() {
        bone.remove();
        Boomerang.getInstance().getBoomerangs().remove(this);
    }
}
