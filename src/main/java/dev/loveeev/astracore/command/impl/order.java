package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import dev.loveeev.astracore.data.BaseBuilds;
import dev.loveeev.astracore.data.TownData;
import dev.loveeev.astracore.database.PostOfficeDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class order implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String players = args[0];
        Town town = TownyAPI.getInstance().getTown("");
        TownBlock
        Player player = Bukkit.getPlayer(players);
        assert player != null;
        int i = Integer.parseInt(args[1]);
        PostOfficeDatabaseManager postOfficeDatabaseManager = new PostOfficeDatabaseManager();
        BaseBuilds baseBuilds = BaseBuilds.getOrCreate(TownyAPI.getInstance().getTownName(player));
        TownData townData = TownData.getOrCreate(TownyAPI.getInstance().getTownName(player));
        switch (i) {
            case 1, 4 -> townData.setPremium(true);
            case 2, 5 -> baseBuilds.setYardplus(true);
            case 3, 6 -> baseBuilds.setPradplus(true);
            case 7 -> {
                if (!player.isOnline()) {
                    postOfficeDatabaseManager.addMessage(player.getName(), 100, postOfficeDatabaseManager.getdata());
                } else {
                    player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 100));
                }
            }
            case 8 -> {
                if (!player.isOnline()) {
                    postOfficeDatabaseManager.addMessage(player.getName(), 240, postOfficeDatabaseManager.getdata());
                } else {
                    player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 240));
                }
            }
            case 9 -> {
                if (!player.isOnline()) {
                    postOfficeDatabaseManager.addMessage(player.getName(), 576, postOfficeDatabaseManager.getdata());
                } else {
                    player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 576));
                }
            }
            case 10 -> {
                if (!player.isOnline()) {
                    postOfficeDatabaseManager.addMessage(player.getName(), 1440, postOfficeDatabaseManager.getdata());

                } else {
                    player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 1440));
                }
            }
            case 11 -> {
                if (!player.isOnline()) {
                    postOfficeDatabaseManager.addMessage(player.getName(), 3200, postOfficeDatabaseManager.getdata());
                } else {
                    player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 3200));
                }
            }
            case 12 -> command("lp user " + player.getName() + " parent addtemp plus 30d");
            case 13 -> command("lp user " + player.getName() + " parent addtemp plus 183d");
            case 14 -> command("lp user " + player.getName() + " parent addtemp plus 1y");
            case 15 -> command("lp user " + player.getName() + " parent addtemp pluss 30d");
            case 16 -> command("lp user " + player.getName() + " parent addtemp pluss 183d");
            case 17 -> command("lp user " + player.getName() + " parent addtemp pluss 1y");
            case 18 -> command("lp user " + player.getName() + " parent addtemp plusx 30d");
            case 19 -> command("lp user " + player.getName() + " parent addtemp plusx 183d");
            case 20 -> command("lp user " + player.getName() + " parent addtemp plusx 1y");
            case 21 ->
                    command("cmi give " + player.getName() + " netherite_pickaxe{Unbreakable:1,Enchantments:[{id:\"efficiency\",lvl:7},{id:\"fortune\",lvl:4}]}");
            case 22 -> command("lp user " + player.getName() + " permission settemp townyflight.command.tfly true 30d");
            case 23 -> command("lp user " + player.getName() + " permission settemp cmi.command.repair true 30d");
        }

        return false;
    }
    public void command(String command){
        CommandSender commandSender = Bukkit.getConsoleSender();
        Bukkit.dispatchCommand(commandSender,command);
    }
}
