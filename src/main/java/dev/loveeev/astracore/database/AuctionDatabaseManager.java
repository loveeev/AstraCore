package dev.loveeev.astracore.database;

import dev.loveeev.astracore.Auction.Auction;
import dev.loveeev.astracore.Auction.ItemToSell;
import dev.loveeev.astracore.data.BukkitSerialization;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;

public class AuctionDatabaseManager {

    public static final String TABLE_NAME = "TOWNY_AUCTION";

    public static void createTable() {
        try (PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                "sellerName VARCHAR(255) NOT NULL, sellerUuid VARCHAR(255) NOT NULL, item TEXT NOT NULL, price DECIMAL NOT NULL," +
                "bidTime INT, nation VARCHAR(255) NOT NULL, town VARCHAR(255) NOT NULL)")) {
            st.executeUpdate();
        } catch (SQLException e) {
            Common.error(e, "Failed to create database table towny_auction");
        }
    }

    public static void load() {
        try (PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("SELECT * FROM " + TABLE_NAME);
             ResultSet set = st.executeQuery()) {

            while (set.next()) {
                String sellerName = set.getString("sellerName");
                String sellerUuid = set.getString("sellerUuid");
                String itemString = set.getString("item");
                double price = set.getDouble("price");
                long bidTime = set.getLong("bidTime");
                String nation = set.getString("nation");
                String town = set.getString("town");

                UUID uuid;
                try{
                    uuid = UUID.fromString(sellerUuid);
                } catch (IllegalArgumentException e){
                    continue;
                }

                ItemStack itemStack;
                try{
                    itemStack = BukkitSerialization.itemFromBase64(itemString);
                } catch (IOException e){
                    continue;
                }
                PostOfficeDatabaseManager postOfficeDatabaseManager = new PostOfficeDatabaseManager();
                ItemToSell auctionItem = new ItemToSell(sellerName, uuid, itemStack, (int)price, Instant.ofEpochSecond(bidTime),nation,town,postOfficeDatabaseManager.getdata());
                Auction.getInstance().getItems().add(auctionItem);
            }
        } catch (SQLException e) {
            Common.error(e, "Failed to load database table towny_auction.");
        }
    }

    public static void removeAuctionItem(ItemToSell itemToSell) {
        Common.runAsync(() -> {
            try (PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE item = ?")) {
                st.setString(1, BukkitSerialization.itemToBase64(itemToSell.getItem()));
                st.executeUpdate();
            } catch (SQLException e) {
                Common.error(e, "Failed to remove item from database table towny_auction.");
            }
        });
    }

    public static void addAuctionItem(ItemToSell item) {
        Common.runAsync(() -> {
            try (PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("INSERT INTO " + TABLE_NAME + " (sellerName, sellerUuid, item, price, bidTime, nation, town) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                st.setString(1, item.getSellerName());
                st.setString(2, item.getSellerUuid().toString());
                st.setString(3, BukkitSerialization.itemToBase64(item.getItem()));
                st.setDouble(4, item.getPrice());
                st.setLong(5, item.getBidTime().getEpochSecond());
                st.setString(6,item.getNation());
                st.setString(7,item.getTown());
                st.executeUpdate();
            } catch (SQLException e) {
                Common.error(e, "Failed to add auction item to database table towny_auction.");
            }
        });
    }

    public static void setNewPrice(ItemToSell item) {
        Common.runAsync(() -> {
            try (PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("UPDATE " + TABLE_NAME + " SET price = ? WHERE item = ?")) {
                st.setDouble(1, item.getPrice());
                st.setString(2, BukkitSerialization.itemToBase64(item.getItem()));
                st.executeUpdate();
            } catch (SQLException e) {
                Common.error(e, "Failed to update auction item price in the database table towny_auction.");
            }
        });
    }

}
