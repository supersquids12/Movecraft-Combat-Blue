package net.countercraft.movecraft.combat.features;

import net.countercraft.movecraft.util.Tags;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class TNTPhysics implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onTNTExplode(@NotNull EntityExplodeEvent e) {

        if (!(e.getEntity() instanceof TNTPrimed))
            return;

        // Cancelling the usual TNT behavior
        e.setCancelled(true);


        List<Entity> Entities = e.getEntity().getNearbyEntities(5,5,5);

        // Running through the nearby entities and applying a velocity vector
        for (int i = 0; i < Entities.size(); i++) {
            Entities
        }

    }
}
