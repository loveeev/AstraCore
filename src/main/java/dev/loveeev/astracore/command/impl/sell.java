package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.command.BaseCommand;
import dev.loveeev.astracore.data.TownData;
import dev.loveeev.astracore.gui.sellmenus.goldsell;
import dev.loveeev.astracore.settings.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class sell extends BaseCommand {
    public sell(Main plugin) {
        super(plugin);
        register("sell");
    }
    TownData data;
    @Override
    public boolean handle(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) throws NotRegisteredException {
        Player player = (Player) sender;
        Resident resident = TownyAPI.getInstance().getResident(player);
        if(Settings.MARKETMONEY) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "В связи с новой системой экономики /sell отключен.");
            return true;
        }
        if(resident.hasTown()) {
            new dev.loveeev.astracore.gui.impl.sell(player, plugin, data).display();
        }else {
            new goldsell(plugin).create(player).open(player);
        }
        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы открыли меню продаж.");
        return false;
    }
}
