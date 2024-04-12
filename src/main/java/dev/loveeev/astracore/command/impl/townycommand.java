package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.gui.impl.BuildingMenu;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
/**
 * @author Zimoxy DEV: loveeev
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)

public class townycommand implements CommandExecutor {
    Main plugin;

    public townycommand(Main plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {
            Resident resident = TownyAPI.getInstance().getResident(player);
            if (resident.hasTown()) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы открыли меню государственных зданий.");
                    new BuildingMenu(player).display();
            } else {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы не состоите в городе.");
            }
        }
        return true;
    }
}