package dev.loveeev.astracore.util;


import com.palmergames.adventure.text.Component;
import com.palmergames.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatFormatter {
    static MiniMessage mm = MiniMessage.miniMessage();

    public static void sendMessage(String text, Player player) {
        Component parsed = mm.deserialize(text);
        player.sendMessage(String.valueOf(parsed));
    }

    public void sendMessage(String text) {
        Component parsed = mm.deserialize(text);
        Bukkit.getServer().getConsoleSender().sendMessage(String.valueOf(parsed));
    }

}
