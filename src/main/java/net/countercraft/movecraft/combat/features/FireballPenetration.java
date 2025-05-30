package net.countercraft.movecraft.combat.features;

import net.countercraft.movecraft.util.Tags;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

public class FireballPenetration implements Listener {
    public static boolean EnableFireballPenetration = true;
    public static boolean UnderwaterFireballPenetration = true;
    public static double resistancePercent = 90;

    public static void load(@NotNull FileConfiguration config) {
        EnableFireballPenetration = config.getBoolean("EnableFireballPenetration", false);
        UnderwaterFireballPenetration = config.getBoolean("UnderwaterFireballPenetration", false);
    }


    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockIgnite(@NotNull BlockIgniteEvent e) {
        if (!EnableFireballPenetration)
            return;
        if (e.getCause() != BlockIgniteEvent.IgniteCause.FIREBALL)
            return;
        if (e.getIgnitingEntity() == null)
            return;


        Block sourceBlock = e.getBlock();
        Block testBlock = sourceBlock.getRelative(BlockFace.EAST);
        if (!testBlock.getType().isBurnable())
            testBlock = sourceBlock.getRelative(BlockFace.WEST);
        if (!testBlock.getType().isBurnable())
            testBlock = sourceBlock.getRelative(BlockFace.NORTH);
        if (!testBlock.getType().isBurnable())
            testBlock = sourceBlock.getRelative(BlockFace.SOUTH);
        if (!testBlock.getType().isBurnable())
            return;


        // check if the source block is among the list of higher resistance blocks
        if (isResistant(testBlock)) {
            if (Math.random() * 100 >= resistancePercent) {
                // To prevent infinite recursion we call the event with SPREAD as the cause
                BlockIgniteEvent igniteEvent = new BlockIgniteEvent(testBlock, BlockIgniteEvent.IgniteCause.SPREAD, e.getIgnitingEntity());
                Bukkit.getPluginManager().callEvent(igniteEvent);
                if (igniteEvent.isCancelled())
                    return;
                // chance to break sourceBlock based on resistancePercent
                testBlock.setType(Material.AIR);
            }
            return;
        }


        // To prevent infinite recursion we call the event with SPREAD as the cause
        BlockIgniteEvent igniteEvent = new BlockIgniteEvent(testBlock, BlockIgniteEvent.IgniteCause.SPREAD, e.getIgnitingEntity());
        Bukkit.getPluginManager().callEvent(igniteEvent);
        if (igniteEvent.isCancelled())
            return;

        testBlock.setType(Material.AIR);

    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onProjectileHit(@NotNull ProjectileHitEvent e) {
        if (!UnderwaterFireballPenetration)
            return;
        if (!(e.getEntity() instanceof Fireball))
            return;

        Block sourceBlock = e.getHitBlock();
        BlockFace hitFace = e.getHitBlockFace();
        if (sourceBlock == null || hitFace == null)
            return; // Hit an entity instead of a block
        if (!sourceBlock.getType().isBurnable())
            return;
        if (!Tags.FLUID.contains(sourceBlock.getRelative(hitFace).getType()))
            return;

        BlockIgniteEvent igniteEvent = new BlockIgniteEvent(sourceBlock, BlockIgniteEvent.IgniteCause.SPREAD, e.getEntity());
        Bukkit.getPluginManager().callEvent(igniteEvent);
        if (igniteEvent.isCancelled())
            return;

        sourceBlock.setType(Material.AIR);
    }

    public static boolean isResistant(Block sourceBlock) {
        return sourceBlock.getType().toString().endsWith("WOOL");
    }


}
