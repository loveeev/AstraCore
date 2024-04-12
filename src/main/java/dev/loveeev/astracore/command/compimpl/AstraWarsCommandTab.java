package dev.loveeev.astracore.command.compimpl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.TownyObject;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.AstraWarsData;
import dev.loveeev.astracore.handler.War;
import dev.loveeev.astracore.util.DataUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AstraWarsCommandTab implements TabCompleter {
    public AstraWarsCommandTab() {
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        if(args.length == 1){
            return getPartialMatches(args[0], List.of("Создать-войну", "Список-войн", "Проходящие-войны", "Настроить-войну","warlist"));
        }
        if(args.length == 2) {
                if (args[0].equalsIgnoreCase("Настроить-войну")) {
                return getPartialMatches(args[1], AstraWarsData.warList.stream().map(War::getName).collect(Collectors.toList()));
            }
            if(args[0].equalsIgnoreCase("warlist")){
                return getPartialMatches(args[1],List.of("off","on"));
            }
        }
        if(args.length == 3) {
            return getPartialMatches(args[2], List.of("Добавить-нацию", "Удалить-нацию", "Удалить", "Начать", "Завершить"));
        }
        if(args.length == 4){
            if (args[2].equalsIgnoreCase("Добавить-нацию")) {
                return getPartialMatches(args[3], TownyAPI.getInstance().getNations().stream().map(nation -> nation.getName()).collect(Collectors.toList()));
            }
            if (args[2].equalsIgnoreCase("Удалить-нацию")) {
                War war;
                war = DataUtil.getWarByName(args[1]);
                return getPartialMatches(args[3], war.getNationList().stream().map(TownyObject::getName).collect(Collectors.toList()));
            }
        }
        if(args.length == 5){
            Main.getInstance().send(player, "Не может быть больше 4 аргументов");
        }
        return new ArrayList<>();
    }

    private List<String> getPartialMatches(String arg, List<String> options) {
        return options.stream().filter(option -> option.startsWith(arg)).collect(Collectors.toList());
    }
}