package dev.loveeev.astracore.util;

import dev.loveeev.astracore.Auction.ItemToSell;
import dev.loveeev.astracore.settings.Settings;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/**
 * @author Zimoxy DEV: loveeev
 */
public class ItemUtil  {


    public static ItemStack createItemToSell(ItemToSell itemToSell) {
        ItemStack itemStack = itemToSell.getItem().clone();
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<String> lore = Settings.SELL_ITEM_LORE;

        for(int i = 0; i < lore.size(); i++){

        lore.set(
                i,
                ChatColor.translateAlternateColorCodes('&',
                        lore.get(i)
                                .replace( "%price%", Double.toString(itemToSell.getPrice()))
                                .replace( "%player%", itemToSell.getSellerName()))
        );

        }
        itemMeta.setLore(lore);


        itemMeta.getPersistentDataContainer().set(
                NamespacedKey.fromString("player_id"),
                PersistentDataType.STRING,
                itemToSell.getSellerUuid().toString()
        );
        itemMeta.getPersistentDataContainer().set(
                NamespacedKey.fromString("price"),
                PersistentDataType.INTEGER,
                itemToSell.getPrice()
        );
            itemStack.setItemMeta(itemMeta);

            return itemStack;
    }


    public static boolean equals(ItemStack itemStack1,ItemStack itemStack2) {
        return itemStack1.getType() == itemStack2.getType() &&
                itemStack1.getItemMeta().getDisplayName().equals(itemStack2.getItemMeta().getDisplayName()) &&
                itemStack1.getEnchantments().equals(itemStack2.getEnchantments());
    }

}
