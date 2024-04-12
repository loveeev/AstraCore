package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class nsoldieradd implements CommandExecutor {

    PreparedStatement ps;
    ResultSet rs;

    MySQL mySQL = MySQL.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        try {
            this.setsoldier(player, strings);
        } catch (NotRegisteredException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private void setsoldier(Player player, String[] args) throws NotRegisteredException {
        String gamer = args[0];
        Resident resident = TownyAPI.getInstance().getResident(player);
        Town town = TownyAPI.getInstance().getTown(player);
        if(args[0] == null) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Введите ник игрока которого хотите назначить.");
        }else {
            if (resident.isKing() || resident.hasNationRank("assistant")) {
                mySQL.selectstring("TOWNY_NATIONS","name",town.getNation().getName(),"soldiers","integer");
                Object soldiers;
                Object limits;
                soldiers = Main.getInstance().getseceltinteger();
                mySQL.selectstring("TOWNY_NATIONS","name",town.getNation().getName(),"limits","integer");
                limits = Main.getInstance().getseceltinteger();
                if ((Integer) limits > (Integer) soldiers) {
                    Nation nation = TownyAPI.getInstance().getNation(player.getName());
                    Nation nation1 = TownyAPI.getInstance().getNation(gamer);
                    Resident res = TownyAPI.getInstance().getResident(gamer);
                    String soldier = "soldier";
                    if (nation == nation1) {
                        if (res.hasNationRank(soldier)) {
                            Main.getInstance().getChatUtility().sendSuccessNotification(player, "У этого игрока уже есть роль военного.");
                        } else {
                            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы назначили " + gamer + " на роль военного.");
                            res.addNationRank(soldier);
                            try {
                                int sol = (Integer)soldiers + 1;

                                String updateQuery = "UPDATE TOWNY_NATIONS SET soldiers = " + sol + " WHERE name = ?";
                                PreparedStatement statement = MySQL.getInstance().getCon().prepareStatement(updateQuery);
                                statement.setString(1, TownyAPI.getInstance().getNation(player).getName());
                                statement.executeUpdate();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Этот игрок не из вашей нации.");
                    }
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас лимит военных.");
                }
            } else {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Эту команду может использовать только создатель нации.");
            }
        }
    }
}
