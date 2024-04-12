package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RaftOpen implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) commandSender;
        Resident resident = TownyAPI.getInstance().getResident(player);
        if (args.length < 2){
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Используйте /t port open/close 'город'");
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            if (resident.isMayor()) {
                if (args[0].equalsIgnoreCase("open")) {
                    MySQL.getInstance().executeUpdate("INSERT INTO `RaftMenu` (`nation`, `nationallow`) VALUES (?, ?)", Objects.requireNonNull(TownyAPI.getInstance().getTown(player)).getName(),args[1]);
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы успешно добавили город &#FFDAA4" + args[1]);
                }
                if (args[0].equalsIgnoreCase("close")) {
                    MySQL.getInstance().executeUpdate("DELETE FROM `RaftMenu` WHERE `nation` = ? AND `nationallow` = ?",Objects.requireNonNull(TownyAPI.getInstance().getTown(player)).getName(),args[1]);
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы успешно удалили город &#FFDAA4" + args[1]);
                }
            } else {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы должны быть мэром для использования этой команды.");
            }
        });
        return false;
    }
}
