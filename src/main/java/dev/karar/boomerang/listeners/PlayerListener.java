package dev.karar.boomerang.listeners;

import dev.karar.boomerang.Boomerang;
import dev.karar.boomerang.bone.Bone;
import dev.karar.boomerang.bone.ItemType;
import dev.karar.boomerang.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (Utils.isItem(player.getItemInHand(), ItemType.BOOMERANG)) {
            Bone bone = new Bone(player, player.getItemInHand(), player.getInventory().getHeldItemSlot());
            Boomerang.getInstance().getBoomerangs().add(bone);
            player.setItemInHand(ItemType.SEDENTARY_BOOMERANG.getItem());
        }
    }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent event) {
        event.setCancelled(Utils.isItem(event.getItemDrop().getItemStack(), ItemType.SEDENTARY_BOOMERANG));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Create an identical list to bypass ConcurrentModificationException.
        for (Bone bone : new ArrayList<>(Boomerang.getInstance().getBoomerangs())) {
            if (bone.player == player) bone.endReturn();
        }
    }
}
