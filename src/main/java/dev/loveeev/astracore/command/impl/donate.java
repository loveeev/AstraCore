package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.command.BaseCommand;
import dev.loveeev.astracore.gui.impl.Donate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class donate extends BaseCommand {
    public donate(Main plugin) {
        super(plugin);
        register("donate");
    }

    @Override
    public boolean handle(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) throws NotRegisteredException {
        Player player = (Player) sender;
        new Donate(plugin).create(player).open(player);
        Main.getInstance().getChatUtility().sendSuccessNotification(player,"Вы открыли меню донатов.");
        return false;
    }
}
