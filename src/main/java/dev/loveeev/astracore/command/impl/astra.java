//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.AlreadyRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Constants;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.settings.Settings;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class astra implements CommandExecutor {
    public astra() {
    }

    PreparedStatement ps;
    ResultSet rs;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        MySQL mySQL = MySQL.getInstance();
        Player player = (Player) sender;
        mySQL.selectstring("TOWNY_RESIDENTS", "name", "loveeev", "provin", "integer");
        int resulttf = (Integer)Main.getInstance().getseceltinteger();
        if (resulttf == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Команда доступна только для игроков");
                return true;
            } else {
                if (args.length != 2) {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Используйте /tbuy buy город");
                    return true;
                } else {
                    Town town = TownyUniverse.getInstance().getTown(args[1]);
                    if (town == null) {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Город не найден");
                        return true;
                    } else {
                        Resident townMayor = town.getMayor();
                        if (args[0].equalsIgnoreCase("npcMayor")) {
                            if (!player.hasPermission(Constants.adminPermission)) {
                                Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас нету прав на данную команду");
                                return true;
                            }

                            if (townMayor.getName().startsWith("NPC")) {
                                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Город уже без мэра");
                                return true;
                            }

                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ta set mayor " + town.getName() + " npc");
                            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Мэр города " + town.getName() + " сменён на NPC");
                        } else if (args[0].equalsIgnoreCase("buy")) {
                            Resident resident = TownyUniverse.getInstance().getResident(player.getUniqueId());
                            if (resident == null) {
                                Main.getInstance().getChatUtility().sendSuccessNotification((Player) null, "Resident == null - true исключение");
                                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Resident == null - true исключение");
                                return true;
                            }

                            if (resident.hasTown()) {
                                Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас уже имеется город");
                                return true;
                            }

                            if (!townMayor.getName().startsWith("NPC")) {
                                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Данный город не свободен");
                                return true;
                            }
                            Double bal = Main.getInstance().getEconomy().getBalance(player);
                            int townPrice = Settings.TOWNPRICE;
                            if(!Settings.MARKETMONEY) {
                                if (bal >= townPrice) {
                                    EconomyResponse economyResponse = Main.getInstance().getEconomy().withdrawPlayer(player, (double) townPrice);
                                    if (economyResponse.type == ResponseType.FAILURE) {
                                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Транзакция не успешна. Стоимость города - " + townPrice);
                                        return true;
                                    }
                                } else {
                                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас недостаточно средств.");
                                }
                            }else {
                                int amount = Main.getInstance().getItemAmount(player, Material.GOLD_INGOT);
                                if(amount >= townPrice){
                                    player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT,townPrice));
                                }else {
                                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Транзакция не успешна. Стоимость города - &#ffdaaa" + townPrice + " &fзолотых слитков");
                                    return true;
                                }
                            }

                                try {
                                    resident.setTown(town);
                                } catch (AlreadyRegisteredException var12) {
                                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "AlreadyRegistered ошибка");
                                }

                                town.setMayor(resident);
                                townMayor.removeTown();
                                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы купили город " + town.getName());
                                usecommand("ta town " + town.getName() + " set perm ally off");
                                usecommand("ta town " + town.getName() + " set perm nation off");
                                usecommand("ta town " + town.getName() + " set perm outsider off");
                                commandwr("pvp", town);
                                commandwr("forcepvp", town);
                                commandwr("explosion", town);
                                commandwr("fire", town);
                                commandwr("mobs", town);
                        } else {
                            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Команда не найдена");
                        }
                        return true;
                    }
                }
            }
        } else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Плагин провинций отключен.");
        }
        return false;
    }

    public void usecommand(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public void commandwr(String p, Town town) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ta town " + town.getName() + " toggle " + p + " off");
    }
}

