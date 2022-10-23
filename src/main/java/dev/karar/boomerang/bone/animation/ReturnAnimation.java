package dev.karar.boomerang.bone.animation;

import dev.karar.boomerang.Boomerang;
import dev.karar.boomerang.bone.Bone;
import dev.karar.boomerang.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

public class ReturnAnimation implements Runnable {

    private final Bone animation;

    private final Player player;
    private final ArmorStand bone;

    public ReturnAnimation(Bone animation) {
        this.animation = animation;
        this.player = animation.player;
        this.bone = animation.bone;
    }

    @Override
    public void run() {
        if (bone.isDead()) return;

        if (Bukkit.getPlayer(player.getUniqueId()) == null) {
            animation.die();
            return;
        }

        double radius = Boomerang.getInstance().getConfig().getDouble("animation.return.radius");
        for (Entity nearby : bone.getNearbyEntities(radius, radius, radius)) {
            if (Utils.isEntityIgnored(nearby)) continue;

            if (Boomerang.getInstance().getConfig().getDouble("animation.return.damage") > 0) {
                LivingEntity mob = (LivingEntity) nearby;
                mob.damage(Boomerang.getInstance().getConfig().getDouble("animation.return.damage"));
            }
        }

        Location location = bone.getLocation();
        Vector direction = bone.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();

        // Rotate and teleport boomerang towards sender.
        location.setYaw(bone.getLocation().getYaw() + Boomerang.getInstance().getConfig().getInt("animation.return.rotate-speed"));
        location.subtract(direction);
        bone.teleport(location);

        if (Boomerang.getInstance().getConfig().getBoolean("animation.catch.proximity")) {
            double catchRadius = Boomerang.getInstance().getConfig().getDouble("animation.catch.radius");
            for (Entity nearby : bone.getNearbyEntities(catchRadius, catchRadius, catchRadius)) {
                if (nearby == player) animation.endReturn();
            }
        }
    }
}
