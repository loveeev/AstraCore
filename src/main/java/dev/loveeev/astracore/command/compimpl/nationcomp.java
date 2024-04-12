package dev.loveeev.astracore.command.compimpl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class nationcomp implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        Nation nation = TownyAPI.getInstance().getNation(player);
        if(nation != null){
            return getPartialMatches(strings[0], nation.getResidents().stream().filter(resident -> resident.getNationRanks().contains("soldier")).map(resident -> resident.getName()).collect(Collectors.toList()));
        }
     return new ArrayList<>();
    }
    private List<String> getPartialMatches(String arg, List<String> options) {
        return options.stream().filter(option -> option.startsWith(arg)).collect(Collectors.toList());
    }
}
