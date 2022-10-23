package dev.karar.boomerang.bone.animation;

import dev.karar.boomerang.Boomerang;
import dev.karar.boomerang.bone.Bone;
import dev.karar.boomerang.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ThrowAnimation implements Runnable {

    private final Bone animation;

    private final Player player;
    private final ArmorStand bone;

    private final Location startLoc;

    public ThrowAnimation(Bone animation) {
        this.animation = animation;
        this.player = animation.player;
        this.bone = animation.bone;

        this.startLoc = animation.player.getLocation();
    }

    @Override
    public void run() {
        if (bone.isDead()) return;

        if (Bukkit.getPlayer(player.getUniqueId()) == null) {
            animation.die();
            return;
        }

        double radius = Boomerang.getInstance().getConfig().getDouble("animation.throw.radius");
        for (Entity nearby : bone.getNearbyEntities(radius, radius, radius)) {
            if (Utils.isEntityIgnored(nearby)) continue;

            if (Boomerang.getInstance().getConfig().getDouble("animation.throw.damage") > 0) {
                LivingEntity mob = (LivingEntity) nearby;
                mob.damage(Boomerang.getInstance().getConfig().getDouble("animation.throw.damage"));
            }
        }

        Location location = bone.getLocation();
        Vector direction = startLoc.getDirection();

        // On instance boomerang collides with a block, cancel and return to sender.
        if (!Utils.getNearbyBlocks(new Location(location.getWorld(), location.getX(), location.getY() + 1, location.getZ()), 0).isEmpty()) {
            animation.startReturn();
            return;
        }

        // Rotate and teleport boomerang towards sender-cast direction.
        location.setYaw(bone.getLocation().getYaw() + Boomerang.getInstance().getConfig().getInt("animation.throw.rotate-speed"));
        location.add(direction);
        bone.teleport(location);
    }
}
