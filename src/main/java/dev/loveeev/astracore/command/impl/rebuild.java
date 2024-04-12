package dev.loveeev.astracore.command.impl;

import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class rebuild implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("rebuild")) {
            if (!sender.hasPermission("yourplugin.rebuild")) {
                sender.sendMessage("You don't have permission to use this command.");
                return true;
            }

            // Connect to your database
            Connection connection = MySQL.getInstance().getCon();
            if (connection == null) {
                sender.sendMessage("Unable to connect to the database.");
                return true;
            }

            // Remove specified columns from table TOWNY_NATIONS
            try {
                PreparedStatement alterStatement = connection.prepareStatement("ALTER TABLE TOWNY_NATIONS " +
                        "DROP COLUMN warlimit, " +
                        "DROP COLUMN bcamount, " +
                        "DROP COLUMN farmamount, " +
                        "DROP COLUMN bmamount, " +
                        "DROP COLUMN bankamount, " +
                        "DROP COLUMN stamount, " +
                        "DROP COLUMN waramount, " +
                        "DROP COLUMN portamount, " +
                        "DROP COLUMN limits, " +
                        "DROP COLUMN soldiers, " +
                        "DROP COLUMN fuelamount, " +
                        "DROP COLUMN fuelcraftamount, " +
                        "DROP COLUMN trainamount, " +
                        "DROP COLUMN raftamount, " +
                        "DROP COLUMN tankamount, " +
                        "DROP COLUMN planeamount, " +
                        "DROP COLUMN drillamount, " +
                        "DROP COLUMN caramount, " +
                        "DROP COLUMN stazavodamount, " +
                        "DROP COLUMN staproduction, " +
                        "DROP COLUMN setnation, " +
                        "DROP COLUMN setnationc, " +
                        "DROP COLUMN setnationl, " +
                        "DROP COLUMN fabricamount, " +
                        "DROP COLUMN setnationbkomand, " +
                        "DROP COLUMN setnationlkomand, " +
                        "DROP COLUMN setnationckomand, " +
                        "DROP COLUMN setnationkomand, " +
                        "DROP COLUMN MoneyYardAmount, " +
                        "DROP COLUMN CapProcent;");
                alterStatement.execute();
                alterStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage("An error occurred while removing columns from the table TOWNY_NATIONS.");
                return true;
            }

            // Remove specified columns from table TOWNY_TOWNS
            try {
                PreparedStatement alterStatement = connection.prepareStatement("ALTER TABLE TOWNY_TOWNS " +
                        "DROP COLUMN resourcesector, " +
                        "DROP COLUMN militaryInv, " +
                        "DROP COLUMN producingGun, " +
                        "DROP COLUMN oaksector, " +
                        "DROP COLUMN payer, " +
                        "DROP COLUMN farmer, " +
                        "DROP COLUMN mayorbank, " +
                        "DROP COLUMN bc, " +
                        "DROP COLUMN spec, " +
                        "DROP COLUMN cargw, " +
                        "DROP COLUMN premium, " +
                        "DROP COLUMN carg, " +
                        "DROP COLUMN cargwar, " +
                        "DROP COLUMN cargst, " +
                        "DROP COLUMN bank, " +
                        "DROP COLUMN statfuelglow, " +
                        "DROP COLUMN statfuel, " +
                        "DROP COLUMN fuelinv, " +
                        "DROP COLUMN cargfuel, " +
                        "DROP COLUMN fuel, " +
                        "DROP COLUMN cargfuelcraft, " +
                        "DROP COLUMN cargtrain, " +
                        "DROP COLUMN cargraft, " +
                        "DROP COLUMN cargtank, " +
                        "DROP COLUMN cargplane, " +
                        "DROP COLUMN cargdrill, " +
                        "DROP COLUMN cargcar, " +
                        "DROP COLUMN stazavod, " +
                        "DROP COLUMN staproduction, " +
                        "DROP COLUMN cargfabric, " +
                        "DROP COLUMN procwar, " +
                        "DROP COLUMN WarProc, " +
                        "DROP COLUMN MoneyYard, " +
                        "DROP COLUMN yardinv, " +
                        "DROP COLUMN MoneyYardPlus, " +
                        "DROP COLUMN MoneyPredPlus;");
                alterStatement.execute();
                alterStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage("An error occurred while removing columns from the table TOWNY_TOWNS.");
                return true;
            }
            Main.getInstance().rebuild();
            sender.sendMessage("Пересоздания всех данных завершено успешно.");
            return true;
        }
        return false;
    }
}
