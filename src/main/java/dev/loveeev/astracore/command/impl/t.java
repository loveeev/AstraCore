package dev.loveeev.astracore.command.impl;

import dev.loveeev.astracore.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class t implements CommandExecutor {

    Main plugin;
    public t(Main plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        Main.getInstance().getChatUtility().sendSuccessNotification(player,"В разработке...");

        return false;
    }
}
