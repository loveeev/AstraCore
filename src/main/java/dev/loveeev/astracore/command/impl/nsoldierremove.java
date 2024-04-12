package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class nsoldierremove implements CommandExecutor {

    PreparedStatement ps;

    ResultSet rs;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        MySQL mySQL = MySQL.getInstance();
        Player player = (Player) commandSender;
        String gamer = args[0];
        Resident resident = TownyAPI.getInstance().getResident(player);
        if(gamer == null){
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Укажите ник игрока которого хотите снять.");
        }else {
            if (resident.isKing() || resident.hasNationRank("assistant")) {
                mySQL.selectstring("TOWNY_NATIONS","name",TownyAPI.getInstance().getNation(player).getName(),"soldiers","integer");
                Object soldiers;
                soldiers = Main.getInstance().getseceltinteger();
                Nation nation = TownyAPI.getInstance().getNation(player.getName());
                Nation nation1 = TownyAPI.getInstance().getNation(gamer);
                Resident res = TownyAPI.getInstance().getResident(gamer);
                String soldier = "soldier";
                if (nation == nation1) {
                    if (res.hasNationRank(soldier)) {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы сняли " + gamer + " с роли военного.");
                        res.removeNationRank(soldier);
                            int sol =(Integer) soldiers - 1;
                            mySQL.updateint("TOWNY_NATIONS","soldiers","name",sol, String.valueOf(TownyAPI.getInstance().getNation(gamer).getName()));
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "У этого игрока нет роли военного.");
                    }
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Этот игрок не из вашей нации.");
                }
            } else {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Эту команду может использовать только создатель нации.");
            }
        }
        return false;
    }
}
