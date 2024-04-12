package dev.loveeev.astracore.command;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.util.Logging;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
/**
 * @author Zimoxy DEV: loveeev
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Getter

public abstract class BaseCommand implements CommandExecutor {

    Main plugin;

    public BaseCommand(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            handle(sender, command, label, args);
        } catch (Exception exception) {
            UUID errorUUID = UUID.randomUUID();
            if (sender instanceof Player player) {
                plugin.getChatUtility().sendErrorNotification(player, "Во время выполнения комманды произошла непредвиденная ошибка. UUID ошибки: &#f2edaa" + errorUUID);
            }
            Logging.error("Во время выполнения комманды произошла непредвиденная ошибка. UUID ошибки: " + errorUUID);
            Logging.error(exception.toString());
        }
        return true;
    }

    public void sendHelpMessage(Player player) {

    }

    protected void register(String command) {
        Objects.requireNonNull(plugin.getCommand(command)).setExecutor(this);
    }

    public abstract boolean handle(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) throws NotRegisteredException;
}