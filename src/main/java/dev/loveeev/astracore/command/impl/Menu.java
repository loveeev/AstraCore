package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.command.BaseCommand;
import dev.loveeev.astracore.gui.impl.menu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Menu extends BaseCommand {


    public Menu(Main plugin) {
        super(plugin);
        register("menu");
    }

    @Override
    public boolean handle(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) throws NotRegisteredException {
        if(!(sender instanceof Player)){
            Main.getInstance().getServer().getConsoleSender().sendMessage("Команда только для игроков.");
        }
        Player player = (Player) sender;
        new menu(plugin).create(player).open(player);
        return false;
    }
}
