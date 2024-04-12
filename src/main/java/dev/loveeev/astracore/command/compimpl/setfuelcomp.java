package dev.loveeev.astracore.command.compimpl;

import com.palmergames.bukkit.towny.TownyAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class setfuelcomp implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 1){
            return getPartialMatches(strings[0],List.of("set","info"));
        }
        if(strings.length == 2) {
            if (strings[0].equalsIgnoreCase("set")) {
                return getPartialMatches(strings[1], TownyAPI.getInstance().getTowns().stream().map(town -> town.getName()).collect(Collectors.toList()));
            }
        }
        return new ArrayList<>();
    }

    private List<String> getPartialMatches(String arg, List<String> options) {
        return options.stream().filter(option -> option.startsWith(arg)).collect(Collectors.toList());
    }
}
