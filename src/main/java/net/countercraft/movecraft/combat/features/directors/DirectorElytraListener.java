package net.countercraft.movecraft.combat.features.directors;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import net.countercraft.movecraft.combat.localisation.I18nSupport;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * Determines when a player swaps an elytra on themselves
 */
public class DirectorElytraListener extends Directors implements Listener {
    private static boolean DisableDirectorElytra = false;

    public static void load(@NotNull FileConfiguration config) {
        DisableDirectorElytra = config.getBoolean("DisableDirectorElytra", false);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void ElytraEquipEvent(@NotNull PlayerArmorChangeEvent e) {
        Player p = e.getPlayer();
        p.sendMessage("Armor Event Proc Item: " + e.getNewItem().getType());

        if (DisableDirectorElytra) {
            if (e.getNewItem().getType().equals(Material.ELYTRA)) {
                p.sendMessage("Yes You're wearing an Elytra");
                if (!isDirector(p)) {
                    p.sendMessage("You're not a director, whore.");
                    return;
                }

                removeDirector(p);
                p.sendMessage(I18nSupport.getInternationalisedString("Director - No Longer Directing"));
                p.sendMessage(I18nSupport.getInternationalisedString("Director - Not Allowed To Direct While Wearing An Elytra"));

            }
        }
    }

}
