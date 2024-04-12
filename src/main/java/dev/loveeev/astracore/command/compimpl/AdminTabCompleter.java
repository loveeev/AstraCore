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


/**
 * @author Zimoxy DEV: loveeev
 */

public class AdminTabCompleter implements TabCompleter {


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {


        if (args.length == 1) {
            return getPartialMatches(args[0], TownyAPI.getInstance().getTowns().stream().map(town -> town.getName()).collect(Collectors.toList()));
        }
        if (args.length == 2) {
            return getPartialMatches(args[1], List.of("порт", "военный-завод", "военный-завод+", "банк", "предприятие","сталелитейный-завод","аграрный-сектор", "телеграф","нефтяная-станция","нефтеперерабатывающий-завод","локомотивный-завод","верфь","танкостроительный-завод","авиастроительный-завод","завод-легкой-техники","завод-тяжелой-техники","текстильная-фабрика"
            ,"монетный-Двор"));
        }
        if(args.length == 3) {
            return getPartialMatches(args[2], List.of("true", "false"));
        }
        return new ArrayList<>();
    }

    private List<String> getPartialMatches(String arg, List<String> options) {
        return options.stream().filter(option -> option.startsWith(arg)).collect(Collectors.toList());
    }
}
