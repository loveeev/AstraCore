package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import dev.loveeev.astracore.Auction.Auction;
import dev.loveeev.astracore.Auction.ItemToSell;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.command.BaseCommand;
import dev.loveeev.astracore.database.AuctionDatabaseManager;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.database.PostOfficeDatabaseManager;
import dev.loveeev.astracore.gui.builds.AuctionMenu;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Zimoxy DEV: loveeev
 */

public class Ah1Command extends BaseCommand {

    PreparedStatement ps;

    ResultSet rs;

    public Ah1Command(Main plugin) {
        super(plugin);
        register("ah");
    }

    @Override
    public boolean handle(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        Resident resident = TownyAPI.getInstance().getResident(player);
        if(resident.hasTown()) {

            if(!resident.hasNation()){
                Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас нет нации.");
                return true;
            }
            try {
                ps = MySQL.getInstance().getCon().prepareStatement("SELECT * FROM TOWNY_TOWNS WHERE name = ?"); // Обязательно нужно делать с ?, на строчке ниже ты передаешь аргументы которые над вставить
                ps.setString(1, resident.getTown().getName()); // ЛуЧШЕ каждый арг. делать в отдельной строке - для каждого типа, INT, Double и тд
                rs = ps.executeQuery();
                if (!rs.next()) { // rs.next() - тут проверяется есть ли результат такой в БД
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Ошибка базы данных, обратитесь за помощью к администратору.");
                } else {
                    int resulttf;
                    resulttf = rs.getInt("carg");
                    if (resulttf == 0) {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас не построен порт. Он нужен для использования мирового рынка!");
                        return true;
                    }
                    if (args.length == 0) {
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
                            PostOfficeDatabaseManager postOfficeDatabaseManager = new PostOfficeDatabaseManager();
                            ItemToSell itemToSell = new ItemToSell(player, player.getInventory().getItemInMainHand(), (int) price, TownyAPI.getInstance().getNation(player).getName(),TownyAPI.getInstance().getTown(player).getName(),postOfficeDatabaseManager.getdata());
                            Auction.getInstance().getItems().add(itemToSell);
                            AuctionDatabaseManager.addAuctionItem(itemToSell);
                            AuctionMenu.refreshForAllViewers();

                            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Ваш предмет выставлен на мировом рынке.");
                        }
                        return true;
                    }
            } catch (NotRegisteredException | SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас нет города.");
        }
        return true;
    }
}
