package dev.karar.boomerang.commands;

import dev.karar.boomerang.Boomerang;
import dev.karar.boomerang.bone.ItemType;
import dev.karar.boomerang.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("boomerang.give")) {
            sender.sendMessage(Boomerang.getInstance().getMessageData().get("no-permission"));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(Boomerang.getInstance().getMessageData().get("invalid-syntax"));
            return true;
        }

        // Validate amount.
        int amount = 1;
        if (args.length > 1) {
            try {
                amount = Integer.parseInt(args[1]);
                if (amount < 1) {
                    sender.sendMessage(Boomerang.getInstance().getMessageData().get("failed-negative-amount").replace("{amount}", args[1]));
                    return true;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(Boomerang.getInstance().getMessageData().get("failed-invalid-amount").replace("{amount}", args[1]));
                return true;
            }
        }

        // Validate target is online.
        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Boomerang.getInstance().getMessageData().get("failed-offline").replace("{player}", args[0]));
            return true;
        }

        // Validate target inventory is not full.
        if (target.getInventory().firstEmpty() == -1) {
            sender.sendMessage(Boomerang.getInstance().getMessageData().get("failed-inventory-full").replace("{player}", args[0]));
            return true;
        }

        // Limit amount to number of empty slots.
        amount = Math.min(amount, Utils.getEmptySlots(target.getInventory()));

        for (int i = 0; i < amount; i++) {
            target.getInventory().addItem(ItemType.BOOMERANG.getItem());
        }

        sender.sendMessage(Boomerang.getInstance().getMessageData().get("successful").replace("{player}", target.getName()).replace("{amount}", String.valueOf(amount)).replace("{s}", amount != 1 ? "s" : ""));
        return true;
    }
}
