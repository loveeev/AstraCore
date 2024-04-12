package dev.loveeev.astracore.command.compimpl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class waraddadmincomp implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return getPartialMatches(strings[0], List.of("Снять-Военного", "Назначить-военного"));
        }
        if (strings.length == 2) {
            return getPartialMatches(strings[1], TownyAPI.getInstance().getNations().stream().map(nation2 -> nation2.getName()).collect(Collectors.toList()));
        }
        if (strings[0].equalsIgnoreCase("Снять-Военного")) {
            if (strings.length == 3) {
                String nation = strings[1];
                Nation nation1 = TownyAPI.getInstance().getNation(nation);
                return getPartialMatches(strings[2], nation1.getResidents().stream().filter(resident -> resident.getNationRanks().contains("soldier")).map(resident -> resident.getName()).collect(Collectors.toList()));
            }
        } else if (strings[0].equalsIgnoreCase("Назначить-военного")) {
            String nation = strings[1];
            Nation nation1 = TownyAPI.getInstance().getNation(nation);
            return getPartialMatches(strings[2], nation1.getResidents().stream().map(resident -> resident.getName()).collect(Collectors.toList()));
        } else {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    private List<String> getPartialMatches(String arg, List<String> options) {
        return options.stream().filter(option -> option.startsWith(arg)).collect(Collectors.toList());
    }
}

