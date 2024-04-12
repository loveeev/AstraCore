package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Constants;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.AstraWarsData;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.handler.DisableRespawnOnBed;
import dev.loveeev.astracore.handler.War;
import dev.loveeev.astracore.settings.Settings;
import dev.loveeev.astracore.util.DataUtil;
import dev.loveeev.astracore.util.TownyUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class AstraWarsCommandExecutor implements CommandExecutor {
    PreparedStatement ps;

    ResultSet rs;
    MySQL mySQL = MySQL.getInstance();

    public AstraWarsCommandExecutor() {
    }

    private DisableRespawnOnBed respawnListener;
    //ебал маму максима

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        try {
            if (args.length == 0) {
                Main.getInstance().send(player, "Команда должна иметь минимум 1 аргумент");
                return true;
            }

            if (args[0].equalsIgnoreCase("Проходящие-войны")) {
                this.warInfo(player);
                return true;
            }

            if (!player.hasPermission(Constants.adminPermission)) {
                Main.getInstance().send(player, "У вас нету доступа к данной команде");
                return true;
            }
            if (args[0].equalsIgnoreCase("Список-войн")) {
                this.list(player);
            } else if (args[0].equalsIgnoreCase("Настроить-войну")) {
                this.use(player, args);
            } else if (args[0].equalsIgnoreCase("Создать-войну")) {
                this.create(player, args);
            }else if(args[0].equalsIgnoreCase("warlist")){
                this.warlist(player,args);
            }else {
                Main.getInstance().send(player, "Команда не найдена");
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            Main.getInstance().send(player, "Команда завершена с ошибкой");
        }

        return true;
    }

    private void warlist(Player player, String[] args) {
        if(args.length < 3){
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"Используйте .. warlist on/off ,для ограничение входа игроков во время войны.");
            return;
        }
        if (args[1].equalsIgnoreCase("off")) {
            mySQL.updateint("TOWNY_RESIDENTS", "provin", "name", 0, "Detreyk");
        }
        if (args[1].equalsIgnoreCase("on")) {
            mySQL.updateint("TOWNY_RESIDENTS", "provin", "name", 1, "Detreyk");
        }
    }
    private void warInfo(Player player) {
        if (!AstraWarsData.hasEnabledWar) {
            Main.getInstance().send(player, "На данный момент не ведётся ни одна война");
        } else {
            War war = DataUtil.getFirstEnabledWar();
            StringBuilder message = new StringBuilder();
            message.append(Settings.prefix).append(war.getName());
            message.append("\n  ").append("Прогресс захвата городов (1 - ").append(Settings.TIMEFOROCCUOATION).append("):");
            Iterator var4 = war.getOccupationProgress().entrySet().iterator();


            while (var4.hasNext()) {
                Map.Entry<Town, Integer> entry = (Map.Entry) var4.next();
                message.append("\n    ").append(((Town) entry.getKey()).getName()).append(" - ").append(entry.getValue());
            }
            player.sendMessage(message.toString());
        }
    }

    private void list(Player player) {
        StringBuilder message = new StringBuilder(Settings.prefix + "Список всех наций:");
        if (AstraWarsData.warList.size() == 0) {
            message.append(" -");
        } else {
            Iterator var3 = AstraWarsData.warList.iterator();

            label48:
            while (true) {
                while (true) {
                    if (!var3.hasNext()) {
                        break label48;
                    }

                    War war = (War) var3.next();
                    message.append("\n  ").append(war.isEnable() ? "§a" : "§c").append(war.getName()).append("§f").append("\n  Нации:");
                    Iterator var5;
                    if (war.getNationList().size() == 0) {
                        message.append(" -");
                    } else {
                        var5 = war.getNationList().iterator();

                        while (var5.hasNext()) {
                            Nation nation = (Nation) var5.next();
                            message.append("\n    ").append(nation.getName());
                        }
                    }

                    message.append("\n  Игроки:");
                    if (war.getMemberList().size() == 0) {
                        message.append(" -");
                    } else {
                        var5 = war.getMemberList().iterator();

                        while (var5.hasNext()) {
                            UUID memberUuid = (UUID) var5.next();
                            OfflinePlayer member = Bukkit.getOfflinePlayer(memberUuid);
                            if (member.getName() != null) {
                                message.append("\n    ").append(member.getName());
                            }
                        }
                    }
                }
            }
        }

        player.sendMessage(message.toString());
    }

    private void use(Player player, String[] args) {
        if (args.length < 3) {
            Main.getInstance().send(player, "Команда такого типа должна иметь минимум 3 аргумента");
        } else {
            War war = DataUtil.getWarByName(args[1]);
            if (war == null) {
                Main.getInstance().send(player, "Война с таким названием не найдена");
            } else {
                if(args[2].equals("Начать")){
                    this.start(player,war);
                }
                if(args[2].equals("Завершить")){
                    this.stop(player,war);
                }
                if(args[2].equals("Удалить")){
                    this.delete(player,war);
                }
                if(args[2].equals("Добавить-нацию")){
                    this.addNation(player,war,args);
                }
                if(args[2].equals("Удалить-нацию")){
                    this.removeNation(player,war,args);
                }
                if(args[2] == null) {
                    Main.getInstance().send(player, args[2] + " - неизвестный аргумент");
                }
            }
        }
    }

    private void create(Player player, String[] args) {
        if (args.length != 2) {
            Main.getInstance().send(player, "Команда такого типа должна иметь 2 аргумента");
        } else {
            AstraWarsData.warList.add(new War(args[1]));
            try {
                PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("INSERT INTO wars (warname) VALUES (?)");
                st.setString(1, args[1]);
                st.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Main.getInstance().send(player, "Война " + args[1] + " создана");
        }
    }

    private void start(Player player, War war) {
        if (war.isEnable()) {
            Main.getInstance().send(player, "Война уже включена");
        } else if (war.getNationList().size() < 2) {
            Main.getInstance().send(player, "Должны быть минимум 2 воюющие нации");
        } else if (war.getMemberList().size() < 2) {
            Main.getInstance().send(player, "Должны быть минимум 2 участника войны");
        } else if (((List) AstraWarsData.warList.stream().map(War::isEnable).collect(Collectors.toList())).contains(true)) {
            Main.getInstance().send(player, "Нельзя содержать более 2 включенных войн. Это породит большие проблемы!");
        } else {
            war.setEnable(true);
            AstraWarsData.hasEnabledWar = true;
            mySQL.updateint("wars","startstop","warname",1, war.getName());
            Iterator var3 = Bukkit.getOnlinePlayers().iterator();

            while (var3.hasNext()) {
                Player onlinePlayer = (Player) var3.next();
                if (!onlinePlayer.hasPermission(Constants.adminPermission) && !war.getMemberList().contains(onlinePlayer.getUniqueId())) {
                    onlinePlayer.kickPlayer(Settings.prefix + "На сервере началась война. Вы не являетесь её участником");
                }
            }

            var3 = war.getNationList().iterator();

            while (var3.hasNext()) {
                Nation nation = (Nation) var3.next();
                Iterator var5 = nation.getTowns().iterator();

                while (var5.hasNext()) {
                    Town town = (Town) var5.next();
                    TownyUtil.setWarPermissions(town, true);
                }
            }

            Main.getInstance().send(player, "Война " + war.getName() + " включена");
            Main.getInstance().sendPublic("Началась война " + war.getName());
        }
    }

    private void stop(Player player, War war) {
        if (!war.isEnable()) {
            Main.getInstance().send(player, "Война уже отключена");
        } else {
            war.setEnable(false);
            AstraWarsData.hasEnabledWar = false;
            mySQL.updateint("wars","startstop","warname",0, war.getName());
            Iterator var3 = AstraWarsData.warList.iterator();

            while (var3.hasNext()) {
                War currentWar = (War) var3.next();
                if (currentWar.isEnable()) {
                    AstraWarsData.hasEnabledWar = true;
                    break;
                }
            }

            var3 = war.getNationList().iterator();

            while (var3.hasNext()) {
                Nation nation = (Nation) var3.next();
                Iterator var5 = nation.getTowns().iterator();

                while (var5.hasNext()) {
                    Town town = (Town) var5.next();
                    TownyUtil.setWarPermissions(town, false);
                }
            }

            Main.getInstance().send(player, "Война " + war.getName() + " отключена");
            Main.getInstance().sendPublic("Закончилась война " + war.getName());
        }
    }

    private void delete(Player player, War war) {
        if (war.isEnable()) {
            Main.getInstance().send(player, "Нельзя удалить включенную войну");
        } else {
            AstraWarsData.warList.remove(war);
            Main.getInstance().send(player, "Война " + war.getName() + " удалена");
        }
    }

    private void addNation(Player player, War war, String[] args) {
        if (args.length != 4) {
            Main.getInstance().send(player, "Команда такого типа должна иметь 4 аргумента");
        } else {
            Nation nation = TownyAPI.getInstance().getNation(args[3]);
            if (nation == null) {
                Main.getInstance().send(player, "Нация с данным названием не найдена");
            } else if (war.getNationList().contains(nation)) {
                Main.getInstance().send(player, "Нация уже находится в списке");
            } else {
                List<Resident> soldiers = nation.getResidents().stream().filter(resident -> resident.getNationRanks().contains("soldier")).collect(Collectors.toList());
                war.getNationList().add(nation);
                war.getMemberList().addAll(soldiers.stream().map(soldier -> UUID.fromString(String.valueOf(soldier.getUUID()))).collect(Collectors.toList()));
                nation.getResidents();
                Main.getInstance().send(player, "Нация " + args[3] + " добавлена в войну " + war.getName());
            }
        }
    }

    private void removeNation(Player player, War war, String[] args) {
        if (args.length != 4) {
            Main.getInstance().send(player, "Команда такого типа должна иметь 4 аргумента");
        } else {
            Nation nation = TownyAPI.getInstance().getNation(args[3]);
            if (nation == null) {
                Main.getInstance().send(player, "Нация с данным названием не найдена");
            } else {
                war.getNationList().remove(nation);
                List<Resident> soldiers = nation.getResidents().stream().filter(resident -> resident.getNationRanks().contains("soldier")).collect(Collectors.toList());
                war.getMemberList().removeAll(soldiers.stream().map(soldier -> UUID.fromString(String.valueOf(soldier.getUUID()))).collect(Collectors.toList()));
                Main.getInstance().send(player, "Нация " + args[3] + " удалена из войны " + war.getName());
            }
        }
    }
}