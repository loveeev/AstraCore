package dev.loveeev.astracore.layer;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.samjakob.spigui.SpiGUI;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.admincommandbuilds.ah;
import dev.loveeev.astracore.command.impl.setfuel;
import dev.loveeev.astracore.command.impl.townycommand;
import dev.loveeev.astracore.command.impl.*;
import dev.loveeev.astracore.util.ChatUtility;
import dev.loveeev.astracore.util.semantics.Layer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * @author Zimoxy DEV: loveeev
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UtilityLayer extends Layer {

    ChatUtility chatUtility;
    SpiGUI spiGUI;
    public UtilityLayer(Main plugin) {
        super(plugin);
    }

    @Override
    public void enable() {
        super.enable();
        chatUtility = new ChatUtility("i huilan");
        prepareGUI();
        registerCommands();
    }

    private void prepareGUI() {
        spiGUI = new SpiGUI(plugin);
        spiGUI.setEnableAutomaticPagination(false);
    }

    private void registerCommands() {
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWN, "build", new townycommand(plugin));
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN, "builds", new ABuildCommand());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN, "spec",new specset());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN,"warlimit", new adminsoldier());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.NATION,"Назначить-военного", new nsoldieradd());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.NATION,"Снять-военного", new nsoldierremove());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN, "war", new waraddadmin());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN, "ah", new ah());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWN, "payadmin", new payamind());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN, "fuel", new setfuel());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN, "infotown", new infotownpay());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN, "zavodwar", new warlimit());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWN, "sectorbuy", new sectorbuy(plugin));
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWN, "menu", new t(plugin));
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWN,"market",new market(plugin));
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWN,"port",new RaftOpen());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWN,"limitsinfo",new limitsinfo());
        new warinfo(plugin);
        new Ah1Command(plugin);
        new sell(plugin);
        new customcraft(plugin);
    }
}
