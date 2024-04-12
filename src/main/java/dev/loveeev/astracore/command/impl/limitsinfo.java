package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class limitsinfo implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        Nation nation = TownyAPI.getInstance().getNation(strings[0]);
        if (nation != null) {
            try {
                ResultSet resultset = MySQL.getInstance().executeQuery("SELECT * `TOWNY_NATIONS` WHERE name = ?", nation.getName());
                if (resultset.next()) {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, String.valueOf(resultset.getInt("limits")));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
