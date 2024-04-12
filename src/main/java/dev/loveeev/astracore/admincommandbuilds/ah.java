package dev.loveeev.astracore.admincommandbuilds;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Auction.Auction;
import dev.loveeev.astracore.Auction.ItemToSell;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.AuctionDatabaseManager;
import dev.loveeev.astracore.database.PostOfficeDatabaseManager;
import dev.loveeev.astracore.gui.builds.AuctionMenu;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author Zimoxy DEV: loveeev
 */

public class ah implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }
        Resident resident = TownyAPI.getInstance().getResident(player);
        if (args.length == 0) {
//                        Auction.getInstance().show(player, 0);
            new AuctionMenu(player).display();
        } else if (args[0].equals("sell")) {
            if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Возьмите в руку предмет, для его продажи.");
                return true;
            }
            if (args.length == 1) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Укажите цену предмета");
                return true;
            }

            if (player.getInventory().getItemInMainHand().getType().isAir()) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Возьмите в руку предмет,который хотите продать.");
                return true;
            }

            double price;
            try {
                price = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Неверная цена.");
                return true;
            }

            if (!player.hasPermission("auction.sell")) {
                Main.getInstance().getChatUtility().sendErrorNotification(player, "У вас нет прав для продажи предметов на мировом рынке.");
                return true;
            }

            int maxAllowed = 0;
            for (int i = 100; i > 0; i--) {
                if (player.hasPermission("auction.sell." + i)) {
                    maxAllowed = i;
                    break;
                }
            }

            if (Auction.getInstance().countItemsByPlayer(player) >= maxAllowed) {
                Main.getInstance().getChatUtility().sendErrorNotification(player, "Вы уже выставили максимальное количество предметов (" + maxAllowed + ").");
                return true;
            }
            Nation nation = TownyAPI.getInstance().getNation(player);
            Town town = TownyAPI.getInstance().getTown(player);
            PostOfficeDatabaseManager postOfficeDatabaseManager = new PostOfficeDatabaseManager();
            ItemToSell itemToSell = new ItemToSell(player, player.getInventory().getItemInMainHand(), (int) price, nation.getName(),town.getName(),postOfficeDatabaseManager.getdata());
            Auction.getInstance().getItems().add(itemToSell);
            AuctionDatabaseManager.addAuctionItem(itemToSell);
            AuctionMenu.refreshForAllViewers();

            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Ваш предмет выставлен на мировом рынке.");
        }
        return true;
    }
}
