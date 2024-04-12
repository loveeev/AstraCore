package dev.loveeev.astracore.command.compimpl;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class eventcomp implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if(strings.length == 1){
            return getPartialMatches(strings[0],List.of("pvp","join"));
        }
        if(strings.length == 2){
            //
            return getPartialMatches(strings[1],List.of("start","stop","startsearch","teleportall","givekit","delete","list","create","stopsearch"));
        }
        if(strings[1].equalsIgnoreCase("givekit")){
            //Киты
            return getPartialMatches(strings[2],List.of("pvp","pvp1"));
        }
        if(strings.length == 4) {
                return getPartialMatches(strings[3], List.of("GEWEHR_98", "MOSINA", "M1911", "P08", "NAGAN"));
        }
        if(strings[1].equalsIgnoreCase("startsearch")){
            return new ArrayList<>();
        }
        if(strings[1].equalsIgnoreCase("stopsearch")){
            return new ArrayList<>();
        }
        if(strings[1].equalsIgnoreCase("create")){
            return new ArrayList<>();
        }
        if(strings[1].equalsIgnoreCase("teleportall")){
            return new ArrayList<>();
        }
        if(strings[1].equalsIgnoreCase("start")){
            return new ArrayList<>();
        }
        if(strings[1].equalsIgnoreCase("delete")){
            return new ArrayList<>();
        }
        if(strings[1].equalsIgnoreCase("list")){
            return new ArrayList<>();
        }
        if(strings[1].equalsIgnoreCase("stop")){
            return getPartialMatches(strings[2], Bukkit.getOnlinePlayers().stream().map(player1 -> player1.getName()).collect(Collectors.toList()));
        }
        return new ArrayList<>();
    }
    private List<String> getPartialMatches(String arg, List<String> options) {
        return options.stream().filter(option -> option.startsWith(arg)).collect(Collectors.toList());
    }
}
