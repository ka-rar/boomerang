package dev.karar.boomerang.listeners;

import dev.karar.boomerang.bone.ItemType;
import dev.karar.boomerang.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        // If hotbar swap = sedentary, cancel.
        event.setCancelled(event.getHotbarButton() != -1 && Utils.isItem(player.getInventory().getItem(event.getHotbarButton()), ItemType.SEDENTARY_BOOMERANG));
        // If click = sedentary, cancel.
        if (!event.isCancelled()) event.setCancelled(Utils.isItem(event.getCurrentItem(), ItemType.SEDENTARY_BOOMERANG));
    }
}
