package dev.loveeev.astracore.command.impl;

import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.AstraWarsData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import dev.loveeev.astracore.command.BaseCommand;
import dev.loveeev.astracore.gui.impl.warInfo;

public class warinfo extends BaseCommand {


    public warinfo(Main plugin) {
        super(plugin);
        register("warinfo");
    }

    @Override
    public boolean handle(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) throws NotRegisteredException {
        if(!(sender instanceof Player)){
            Main.getInstance().getServer().getConsoleSender().sendMessage("Команда только для игроков.");
        }
        Player player = (Player) sender;
        if (!AstraWarsData.hasEnabledWar) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"На данный момент не ведётся ни одна война");
        }else {
            new warInfo(plugin).create(player).open(player);
        }
        return false;
    }
}