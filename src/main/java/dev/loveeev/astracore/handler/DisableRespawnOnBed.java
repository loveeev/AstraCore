package dev.loveeev.astracore.handler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class DisableRespawnOnBed implements Listener {

    private boolean disableSpawn = false;

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (disableSpawn) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Спавн на кровати отключен.");
        }
    }

    // Метод для включения/отключения спавна на кровати
    public void toggleSpawn(boolean disable) {
        disableSpawn = disable;
    }
}
