package dev.loveeev.astracore.handler;

import dev.loveeev.astracore.Constants;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.AstraWarsData;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.settings.Settings;
import dev.loveeev.astracore.util.DataUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinHandler implements Listener {
    public PlayerJoinHandler() {

    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        MySQL mySQL = MySQL.getInstance();
        mySQL.selectstring("TOWNY_RESIDENTS","name","Detreyk","provin","integer");
        Integer pon = (Integer)Main.getInstance().getseceltinteger();
        if(pon == 1) {
            if (AstraWarsData.hasEnabledWar) {
                War war = DataUtil.getFirstEnabledWar();
                if (war == null) {
                    Main.getInstance().getChatUtility().sendSuccessNotification((Player) null, "PlayerJoinHandler.onPlayerJoinEvent war is null");
                    return;
                }

                Player player = event.getPlayer();
                if (player.hasPermission(Constants.adminPermission)) {
                    return;
                }

                if (!war.getMemberList().contains(player.getUniqueId())) {
                    player.kickPlayer(Settings.prefix + "На сервере ведётся война. Вы не являетесь её участником");
                }

            }
        }
        }
    }