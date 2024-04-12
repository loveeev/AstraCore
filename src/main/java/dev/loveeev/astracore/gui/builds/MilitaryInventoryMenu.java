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
public class MilitaryInventoryMenu {

    private TownData data;

    @Getter
    private final Player player;
    private final int size;

    protected MilitaryInventoryMenu(Player player) {
        this.player = player;
        this.size = 9*6;
    }

    public Inventory setup(){
        // Создать инвентарь (меню)
        Inventory inv = Bukkit.createInventory(null, size, "Инвентарь завода");

        // Получить инвентарь военного завода в виде СЛОТ-ПРЕДМЕТ
        Map<Integer, ItemStack> factoryInv = data.getMilitaryInventory();
        // Установить предметы на их слоты
        for (Map.Entry<Integer, ItemStack> entry : factoryInv.entrySet()){
            ItemStack item = entry.getValue();
            if (item == null) continue;

            inv.setItem(entry.getKey(), item);
        }
        return inv;
    }

    public void display(){
        try{
            this.data = TownData.getOrCreate(player);

            // Обязательно СНАЧАЛА открываем инвентарь, чтобы сработало событие InventoryCloseEvent у прошлого меню
            player.openInventory(setup());

            // А затем присваиваем это меню городу
            data.setMilitaryInventoryMenu(this);
        } catch (NotRegisteredException e){
            Main.getInstance().getChatUtility().sendColoredMessage(player, Settings.NOT_IN_TOWN_MESSAGE);
        }
    }

    public void onMenuClose(Map<Integer, ItemStack> itemsMap) {
        if (TownData.isPlayerInTown(player)){
            data.setMilitaryInventory(itemsMap);
            data.setMilitaryInventoryMenu(null);
        }
    }

    public boolean isReallyOpened(){
        return player != null && player.isOnline();
    }

    public void closeMenu(){
        // При закрытии этого меню, автоматически сработает InventoryCloseMenu (в MilitaryFactoryListener)
        // и инвентарь из меню сохранится в данные города и в БД
        player.closeInventory();
    }

}
