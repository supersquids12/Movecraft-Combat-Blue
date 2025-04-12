package net.countercraft.movecraft.combat.features;

import net.countercraft.movecraft.util.Tags;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import io.papermc.paper.event.entity.EntityKnockbackEvent;

import static io.papermc.paper.event.entity.EntityKnockbackEvent.Cause.EXPLOSION;

public class TNTPhysics implements Listener {
    public static double TNTKnockbackFactor = 2;

    public static void load(@NotNull FileConfiguration config) {
        TNTKnockbackFactor = config.getDouble("TNTKnockbackFactor", 1);
    }


    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void TNTKnockbackEvent (@NotNull EntityKnockbackEvent e) {

        if (!(e.getCause() == EXPLOSION))
            return;
        System.out.println("We have an explosion: " + e.getKnockback().toString());
        // Increase knockback by factor provide from config
        e.setKnockback(e.getKnockback().multiply(TNTKnockbackFactor));
    }
}