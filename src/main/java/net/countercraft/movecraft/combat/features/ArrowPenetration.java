package net.countercraft.movecraft.combat.features;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

public class ArrowPenetration implements Listener {
    public static boolean EnableArrowPenetration = false;
    public static double resistancePercent = 90;
    public static boolean EnablePlayerArrowPenetration = false;
    public static boolean EnableMobArrowPenetration = false;

    public static void load(@NotNull FileConfiguration config) {
        EnableArrowPenetration = config.getBoolean("EnableArrowPenetration", false);
        EnablePlayerArrowPenetration = config.getBoolean("EnablePlayerArrowPenetration", false);
        EnableMobArrowPenetration = config.getBoolean("EnablePlayerArrowPenetration", false);
    }


    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onArrowHit(@NotNull ProjectileHitEvent e) {
        if (!EnableArrowPenetration)
            return;
        if (!(e.getEntity() instanceof AbstractArrow))
            return;

        if (e.getHitBlock() == null )
            return;

        AbstractArrow arrow = (AbstractArrow) e.getEntity();


        if (arrow.getShooter() != null) {
            // Enables/Disables whether arrows fired from a player act the same as if fired from a dispenser
            if (arrow.getShooter() instanceof Player) {
                if (!EnablePlayerArrowPenetration)
                    return;
            }

            else {
                if (!EnableMobArrowPenetration)
                    return;
            }
        }

        Block sourceBlock = e.getHitBlock();

        // To prevent infinite recursion we delete the arrow on hit
        e.getEntity().remove();

        // check if the source block is among the list of higher resistance blocks
        if (isResistant(sourceBlock)) {
            if (Math.random() * 100 >= resistancePercent) {
                // chance to break sourceBlock based on resistancePercent
                sourceBlock.setType(Material.AIR);
            }
        }
    }


    public static boolean isResistant(Block sourceBlock) {
        return sourceBlock.getType().toString().endsWith("WOOL");
    }


}