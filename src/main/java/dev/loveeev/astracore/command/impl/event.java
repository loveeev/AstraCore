package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.AlreadyRegisteredException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Constants;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.settings.Settings;
import dev.loveeev.astracore.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class event implements CommandExecutor{

    PreparedStatement ps;

    ResultSet rs;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        MySQL mySQL = MySQL.getInstance();
        Player player = (Player) sender;
        if (args.length < 1) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Используйте /event аргумент");
            return true;
        }
        if (args[0].equalsIgnoreCase("join")) {
            if (isInventoryEmpty(player)) {
                try {
                    mySQL.selectstring("authme","username", player.getName(),"eventjoin","integer");
                    Object result = Main.getInstance().getseceltinteger();
                    mySQL.selectstring("eventjoin","dud", "start","st","integer");
                    Object resulttf = Main.getInstance().getseceltinteger();
                    if((Integer) resulttf == 1) {
                        if ((Integer)result == 0) {
                            Resident resident = TownyAPI.getInstance().getResident(player);
                            if (resident.hasTown()) {
                                String updateQuery = "INSERT INTO `event` (`username`, `town`) VALUES ('" + player.getName() + "', '" + resident.getTown().getName() + "');";
                                PreparedStatement statement1 = MySQL.getInstance().getCon().prepareStatement(updateQuery);
                                statement1.executeUpdate();
                                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы успешно присоединились к Ивенту. НЕ ЗАБУДЬТЕ ОЧИСТИТЬ ИНВЕНТАРЬ");
                                mySQL.update("authme","eventjoin","username","1",player.getName());
                            } else {
                                String updateQuery = "INSERT INTO `event` (`username`, `town`) VALUES ('" + player.getName() + "', '" + "NULL" + "');";
                                PreparedStatement statement1 = MySQL.getInstance().getCon().prepareStatement(updateQuery);
                                statement1.executeUpdate();
                                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы успешно присоединились к Ивенту. НЕ ЗАБУДЬТЕ ОЧИСТИТЬ ИНВЕНТАРЬ.");
                                mySQL.update("authme","eventjoin","username","1",player.getName());
                            }
                        } else {
                            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы уже участник ивента, НЕ ЗАБУДЬТЕ ОЧИСТИТЬ ИНВЕНТАРЬ");
                        }
                    }else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player,"Набор на ивент закрыт.");
                    }
                } catch (SQLException | NotRegisteredException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Освободите инвентарем.");
            }
        }
        if (player.hasPermission(Constants.adminPermission)) {
            if (args[0].equalsIgnoreCase("pvp")) {
                if (args[1].equalsIgnoreCase("delete")) {
                    try {
                        String updateQuery = "DELETE FROM `event`";
                        PreparedStatement statement1 = MySQL.getInstance().getCon().prepareStatement(updateQuery);
                        statement1.executeUpdate();
                        String updateQueryy = "ALTER TABLE `authme` DROP `eventjoin`";
                        PreparedStatement statement2 = MySQL.getInstance().getCon().prepareStatement(updateQueryy);
                        statement2.executeUpdate();
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Ивент успешно удален");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(args[1].equalsIgnoreCase("create")){
                    try {
                        String updateQuery = "ALTER TABLE `authme` ADD `eventjoin` TINYINT(1) NULL DEFAULT '0' AFTER `nationrank`;";
                        PreparedStatement statement1 = MySQL.getInstance().getCon().prepareStatement(updateQuery);
                        statement1.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (args[1].equalsIgnoreCase("teleportall")) {
                    try {
                        PreparedStatement statement = MySQL.getInstance().getCon().prepareStatement("SELECT username FROM event");
                        ResultSet resultSet = statement.executeQuery();
                        List<String> usernames = new ArrayList<>();
                        while (resultSet.next()) {
                            usernames.add(resultSet.getString("username"));
                        }
                        for (String username : usernames) {
                            OfflinePlayer player1 = Bukkit.getOfflinePlayer(username);
                            if (player1.isOnline()) {
                                Player onlineplayer = (Player) player1;
                                    Location homeLocation = new Location(player.getWorld(), -1962, 165, -6554);
                                    onlineplayer.getInventory().clear();
                                    onlineplayer.teleport(homeLocation);
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (args[1].equalsIgnoreCase("start")) {
                    try {
                    Bukkit.broadcastMessage("§x§f§f§d§a§a§4§lEVENT §8▹ §fИвент начался!!!");
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"rg flag -w \"world\" pvp pvp allow");
                    PreparedStatement statement = MySQL.getInstance().getCon().prepareStatement("SELECT username FROM event");
                    ResultSet resultSet = statement.executeQuery();
                    List<String> usernames = new ArrayList<>();
                    while (resultSet.next()) {
                        usernames.add(resultSet.getString("username"));
                    }
                    for (String username : usernames) {
                        OfflinePlayer player1 = Bukkit.getOfflinePlayer(username);
                        if (player1.isOnline()) {
                            Player onlineplayer = (Player) player1;
                            Resident resident = TownyAPI.getInstance().getResident(onlineplayer);
                            if (!resident.isMayor()) {
                                resident.removeTown();
                            }
                        }
                    }
                } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(args[1].equalsIgnoreCase("stopsearch")){
                    mySQL.update("eventjoin","st","dud","0","start");
                    Bukkit.broadcastMessage("§x§f§f§d§a§a§4§lEVENT §8▹ §fНабор на ивент окончен");
                }
                if (args[1].equalsIgnoreCase("stop")) {
                    if(args.length < 3){
                        Main.getInstance().getChatUtility().sendSuccessNotification(player,"Должно быть более 2 аргументов");
                        return true;
                    }
                    Bukkit.broadcastMessage("§x§f§f§d§a§a§4§lEVENT §8▹ §fИвент окончен!!! Победитель: " + args[2]);

                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"rg flag -w \"world\" pvp pvp deny");
                    try {
                        PreparedStatement statement = MySQL.getInstance().getCon().prepareStatement("SELECT username FROM event");
                        ResultSet resultSet = statement.executeQuery();
                        List<String> usernames = new ArrayList<>();
                        while (resultSet.next()) {
                            usernames.add(resultSet.getString("username"));
                        }
                        for (String username : usernames) {
                            OfflinePlayer player1 = Bukkit.getOfflinePlayer(username);
                            if (player1.isOnline()) {
                                Player onlineplayer = (Player) player1;
                                Resident resident = TownyAPI.getInstance().getResident(onlineplayer);
                                mySQL.selectstring("event","username",resident.getName(),"town","string");
                                Object srav = Main.getInstance().getseceltinteger();
                                if(srav != null) {
                                    Town town = TownyAPI.getInstance().getTown((String) srav);
                                    resident.setTown(town);
                                }
                            }
                        }
                    } catch (SQLException | AlreadyRegisteredException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (args[1].equalsIgnoreCase("startsearch")) {
                    if(args.length < 3){
                        Main.getInstance().getChatUtility().sendSuccessNotification(player,"Укажите приз");
                        return true;
                    }
                    mySQL.update("eventjoin","st","dud","1","start");
                    String item = args[2];
                    String text = "§x§f§f§d§a§a§4§lEVENT §8▹ §fПроводиться пвп Ивент. Награда: " + item + ". На вступление дается 5 минут. Для вступление пропишите команду /event join";
                    Bukkit.broadcastMessage(text);
                }
                if (args[1].equalsIgnoreCase("givekit")) {
                    if (args.length < 4) {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Должно быть более 4 аргументов");
                        return true;
                    }
                    if (args[2].equalsIgnoreCase("pvp")) {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы выдали игрокам кит.");
                        try {
                            PreparedStatement statement = MySQL.getInstance().getCon().prepareStatement("SELECT username FROM event");
                            ResultSet resultSet = statement.executeQuery();
                            List<String> usernames = new ArrayList<>();
                            while (resultSet.next()) {
                                usernames.add(resultSet.getString("username"));
                            }
                            for (String username : usernames) {
                                OfflinePlayer player1 = Bukkit.getOfflinePlayer(username);
                                if (player1.isOnline()) {
                                    Player onlineplayer = (Player) player1;
                                    for (String items : Settings.KITSITEMPVP) {
                                        giveitem(onlineplayer, Material.valueOf(items), 1, 1);
                                    }
                                    pon(args, player, onlineplayer);
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    if (args[2].equalsIgnoreCase("pvp1")) {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы выдали игрокам кит.");
                        try {
                            PreparedStatement statement = MySQL.getInstance().getCon().prepareStatement("SELECT username FROM event");
                            ResultSet resultSet = statement.executeQuery();
                            List<String> usernames = new ArrayList<>();
                            while (resultSet.next()) {
                                usernames.add(resultSet.getString("username"));
                            }
                            for (String username : usernames) {
                                OfflinePlayer player1 = Bukkit.getOfflinePlayer(username);
                                if (player1.isOnline()) {
                                    Player onlineplayer = (Player) player1;
                                    for (String items : Settings.KITSITEMPVPTWO) {
                                        giveitem(onlineplayer, Material.valueOf(items), 1, 1);
                                    }
                                    pon(args, player, onlineplayer);
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if(args[1].equalsIgnoreCase("list")) {
                    try {
                        String query = "SELECT COUNT(*) FROM `event`";
                        PreparedStatement statement = MySQL.getInstance().getCon().prepareStatement(query);
                        ResultSet resultSet = statement.executeQuery(query);
                        if (resultSet.next()) {
                            int rowCount = resultSet.getInt(1);
                            Main.getInstance().getChatUtility().sendSuccessNotification(player,"Учавствует: §x§f§f§d§a§a§4" + rowCount + " §fигроков");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас недостаточно прав.");
        }
        return true;
    }

    private void pon(@NotNull String[] args, Player player, Player onlineplayer) {
        if (args[3].equalsIgnoreCase("GEWEHR_98")) {
            giveitemgun(onlineplayer, Material.CROSSBOW, 1, 5030, "Gewehr 98", "Урон: 10");
            giveitemgun(onlineplayer, Material.PHANTOM_MEMBRANE, 40, 6020, "§#FFDAA47,62x39", ".");
        } else if (args[3].equalsIgnoreCase("MOSINA")) {
            giveitemgun(onlineplayer, Material.CROSSBOW, 1, 5040, "Мосина", "Урон: 10");
            giveitemgun(onlineplayer, Material.PHANTOM_MEMBRANE, 40, 6020, "§#FFDAA47,62x39", ".");
        } else if (args[3].equalsIgnoreCase("M1911")) {
            giveitemgun(onlineplayer, Material.CROSSBOW, 1, 5010, "M1911", "Урон: 3");
            giveitemgun(onlineplayer, Material.PHANTOM_MEMBRANE, 40, 6010, "§#FFDAA49mm", ".");
        } else if (args[3].equalsIgnoreCase("P08")) {
            giveitemgun(onlineplayer, Material.CROSSBOW, 1, 5020, "P08 (Люгер)", "Урон: 3");
            giveitemgun(onlineplayer, Material.PHANTOM_MEMBRANE, 40, 6010, "§#FFDAA49mm", ".");
        } else if (args[3].equalsIgnoreCase("NAGAN")) {
            giveitemgun(onlineplayer, Material.CROSSBOW, 1, 5050, "Наган", "Урон: 6");
            giveitemgun(onlineplayer, Material.PHANTOM_MEMBRANE, 40, 6010, "§#FFDAA49mm", ".");
        } else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Киты выдан без оружия.");
        }
    }

    public boolean isInventoryEmpty(Player player) {
        ItemStack[] contents = player.getInventory().getContents();

        for (ItemStack item : contents) {
            if (item != null) {
                return false;
            }
        }
        return true;
    }
    public void giveitem(Player player, Material material, int amount,int modeldata){
            ItemStack item = new ItemStack(ItemBuilder
                    .fromMaterial(Material.valueOf(String.valueOf(material)))
                    .setAmount(amount)
                    .setCustomModelData(modeldata)
                    .build());
            Inventory playerinv = player.getInventory();
            playerinv.addItem(item);
            player.updateInventory();
        }
    public void giveitemgun(Player player, Material material, int amount,int modeldata,String name,String lore){
        ItemStack item = new ItemStack(ItemBuilder
                .fromMaterial(Material.valueOf(String.valueOf(material)))
                .setName(name)
                .setLore(lore)
                .setAmount(amount)
                .setCustomModelData(modeldata)
                .build());
        Inventory playerinv = player.getInventory();
        playerinv.addItem(item);
        player.updateInventory();
    }
    }

