package net.countercraft.movecraft.combat.features;

import net.countercraft.movecraft.combat.MovecraftCombat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.LinkedList;

public class ArrowLifespan extends BukkitRunnable implements Listener {
    private static final String METADATA_KEY = "MCC-Expiry";
    public static int ArrowLifespan = 0;
    public static int ArrowSpeedFactor = 1;
    public static boolean DisableArrowGravity = true;
    public static boolean EnablePlayerArrowLifespan = false;
    public static boolean EnableMobArrowLifespan = false;
    private final Deque<AbstractArrow> queue = new LinkedList<>();

    public static void load(@NotNull FileConfiguration config) {
        ArrowLifespan = config.getInt("ArrowLifespan", 6);
        ArrowLifespan *= 20 * 50; // Convert from seconds to milliseconds
        ArrowSpeedFactor = config.getInt("ArrowSpeedFactor", 2);
        DisableArrowGravity = config.getBoolean("DisableArrowGravity", true);
        EnablePlayerArrowLifespan = config.getBoolean("EnablePlayerArrowBehavior", false);
        EnableMobArrowLifespan = config.getBoolean("EnableMobArrowLifespan", false);
    }

    @Override
    public void run() {
        // Clear out the old arrows from the queue
        while (queue.size() > 0) {
            if (System.currentTimeMillis() - queue.peek().getMetadata(METADATA_KEY).get(0).asLong() <= ArrowLifespan)
                break; // We've hit an older arrow, stop processing

            AbstractArrow f = queue.pop();
            f.remove();
        }
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onProjectileLaunch(@NotNull ProjectileLaunchEvent e) {
        if (!(e.getEntity() instanceof AbstractArrow))
            return;

        AbstractArrow arrow = (AbstractArrow) e.getEntity();
        arrow.setMetadata(METADATA_KEY, new FixedMetadataValue(MovecraftCombat.getInstance(), System.currentTimeMillis()));


        if (arrow.getShooter() != null) {
            // Enables/Disables whether arrows fired from a player act the same as if fired from a dispenser
            if (arrow.getShooter() instanceof Player) {
                if (!EnablePlayerArrowLifespan)
                    return;
            }

            else {
                if (!EnableMobArrowLifespan)
                    return;
            }
        }

        // Gravity setting based on config
        arrow.setGravity(!DisableArrowGravity);

        // Multiply velocity by the amount provided by config
        arrow.setVelocity(arrow.getVelocity().multiply(ArrowSpeedFactor));
        queue.add(arrow);
    }

}