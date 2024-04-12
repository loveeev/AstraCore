package dev.loveeev.astracore.command.impl;

import dev.loveeev.astracore.TeleportMenu.TeleportMenu;
import dev.loveeev.astracore.TeleportMenu.teleportmenuv;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class teleportmenu implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length < 2){
            return true;
        }
        String players = strings[1];
        Player player = Bukkit.getPlayer(players);
        if(strings[0].equalsIgnoreCase("1")) {
            new TeleportMenu(player).display();
        }else if(strings[0].equalsIgnoreCase("2")){
            new teleportmenuv(player).display();
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return new ArrayList<>();
    }
}
