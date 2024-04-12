package dev.loveeev.astracore.gui.builds;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.TownData;
import dev.loveeev.astracore.settings.Settings;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Меню инвентаря военного завода
 */
public class invFuel {

    private TownData data;

    @Getter
    private final Player player;
    private final int size;

    protected invFuel(Player player) {
        this.player = player;
        this.size = 9*6;
    }

    public Inventory setup(){
        Inventory inv = Bukkit.createInventory(null, size, "Инвентарь станции");

        Map<Integer, ItemStack> factoryInv = data.getFuelinv();
        for (Map.Entry<Integer, ItemStack> entry : factoryInv.entrySet()) {
            int index = entry.getKey();
            ItemStack item = entry.getValue();
            if (index >= 0 && index < size) {
                ItemStack existingItem = inv.getItem(index);
                if (existingItem != null && existingItem.isSimilar(item)) {
                    existingItem.setAmount(existingItem.getAmount() + item.getAmount());
                } else {
                    inv.setItem(index, item);
                }
            }
        }

        return inv;
    }


    public void display(){
        try{
            this.data = TownData.getOrCreate(player);

            // Обязательно СНАЧАЛА открываем инвентарь, чтобы сработало событие InventoryCloseEvent у прошлого меню
            player.openInventory(setup());
            data.setInvfuel(this);
        } catch (NotRegisteredException e){
            Main.getInstance().getChatUtility().sendColoredMessage(player, Settings.NOT_IN_TOWN_MESSAGE);
        }
    }

    public void onMenuClose(Map<Integer, ItemStack> itemsMap) {
        if (TownData.isPlayerInTown(player)){
            data.setFuelInventory(itemsMap);
            data.setInvfuel(null);
        }
    }

    public boolean isReallyOpened(){
        return player != null && player.isOnline();
    }

    public void closeMenu(){
        player.closeInventory();
    }

}
