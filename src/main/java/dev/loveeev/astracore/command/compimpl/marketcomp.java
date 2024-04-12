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

public class marketcomp implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 1){
            return getPartialMatches(args[0],List.of("add","remove"));
        }
        if(args.length == 2){
            return getPartialMatches(args[1], TownyAPI.getInstance().getNations().stream().map(nation -> nation.getName()).collect(Collectors.toList()));
        }
        return new ArrayList<>();
    }
    private List<String> getPartialMatches(String arg, List<String> options) {
        return options.stream().filter(option -> option.startsWith(arg)).collect(Collectors.toList());
    }
}
