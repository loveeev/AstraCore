package dev.loveeev.astracore.handler;

import dev.loveeev.astracore.Constants;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.AstraWarsData;
import dev.loveeev.astracore.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocessHandler implements Listener {
    public PlayerCommandPreprocessHandler() {
    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (AstraWarsData.hasEnabledWar && Settings.forbiddenCommands.contains(event.getMessage().substring(1)) && !player.hasPermission(Constants.adminPermission)) {
            Main.getInstance().send(event.getPlayer(), "Данная команда запрещена во время войны");
            event.setCancelled(true);
        }

    }
}