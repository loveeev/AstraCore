package dev.loveeev.astracore.event;

import dev.loveeev.astracore.data.AstraWarsData;
import dev.loveeev.astracore.handler.War;
import dev.loveeev.astracore.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

public class playerdeath implements Listener {

    private static War war;

    public static void setWar(War war) {
        playerdeath.war = war;
    }

    /**
     * Вызывается когда игрок умирает на войне
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        final Player player = event.getEntity().getPlayer();
        if(Settings.DEATHONWAR) {

            if (AstraWarsData.hasEnabledWar) {
                Location location = new Location(Bukkit.getWorld("lobbys"), 235, 77, 360);
                player.teleport(location);
                player.setOp(false);
                player.setInvulnerable(true);
                Bukkit.getScheduler().runTaskLater((Plugin) this, () -> {
                    Location location1 = new Location(Bukkit.getWorld("lobbys"),237,66,221);
                    player.teleport(location1);
                    player.setOp(true);
                    player.setInvulnerable(false);
                }, 20L * Settings.TIMEWAR); // Пример: телепортировать через 10 секунд после смерти
            }
        }else {
            if(AstraWarsData.hasEnabledWar){
                assert player != null;
                war.getMemberList().remove(player.getUniqueId());
                player.kickPlayer("Вы погибли на войне. Сможете зайти по ее завершению");
            }
        }
    }
}
