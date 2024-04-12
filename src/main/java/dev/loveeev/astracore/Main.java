package dev.loveeev.astracore;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.object.AddonCommand;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.samjakob.spigui.SpiGUI;
import dev.loveeev.astracore.TeleportMenu.teleportmenuv;
import dev.loveeev.astracore.command.compimpl.infotownpay;
import dev.loveeev.astracore.command.compimpl.sectorbuy;
import dev.loveeev.astracore.command.compimpl.*;
import dev.loveeev.astracore.command.impl.*;
import dev.loveeev.astracore.data.AstraWarsData;
import dev.loveeev.astracore.data.DependencyManager;
import dev.loveeev.astracore.database.*;
import dev.loveeev.astracore.event.playerdeath;
import dev.loveeev.astracore.handler.*;
import dev.loveeev.astracore.layer.SpigotServiceLayer;
import dev.loveeev.astracore.layer.UtilityLayer;
import dev.loveeev.astracore.settings.Settings;
import dev.loveeev.astracore.util.ChatUtility;
import dev.loveeev.astracore.util.DataUtil;
import dev.loveeev.astracore.util.TownyUtil;
import dev.loveeev.astradonate.order;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.rubix327.itemslangapi.ItemsLangAPI;
import me.rubix327.itemslangapi.Lang;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.SimpleSettings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 * @author Zimoxy DEV: loveeev
 */

@Getter
public final class Main extends SimplePlugin {

    @Getter
    private static Main instance;
    private Economy economy;
    private ItemsLangAPI langAPI;
    PreparedStatement ps;
    ResultSet rs;
    UtilityLayer utilityLayer;
    SpigotServiceLayer serviceLayer;
    private final PluginManager pluginManager = Bukkit.getPluginManager();
    private DisableRespawnOnBed respawnListener;
    private MySQL mySQL;
    public Main() {
    }
    @Override
    protected void onPluginStart() {
        instance = this; // Initializing instance
        mySQL = new MySQL();
        createBossBar();
        startUpdateTask();
        DependencyManager.checkSoftDependencies();
        utilityLayer = new UtilityLayer(this);
        utilityLayer.enable();
        serviceLayer = new SpigotServiceLayer(this);
        serviceLayer.enable();
        respawnListener = new DisableRespawnOnBed();
        Player player = getServer().getOnlinePlayers().stream().findFirst().orElse(null);
        if (player != null) {
            teleportmenuv menu = new teleportmenuv(player);
            menu.generateSpawnPoints();
        }
        Bukkit.getPluginManager().registerEvents(new TownPreDeleteHandler(), this);
        this.getCommand("provinces").setExecutor(new astra());
        this.getCommand("provinces").setTabCompleter(new comp());
        Bukkit.getPluginManager().registerEvents(new PlayerCommandPreprocessHandler(), this);
        Bukkit.getPluginManager().registerEvents(new NationPreTownLeaveHandler(), this);
        Bukkit.getPluginManager().registerEvents(new NationPreTownKickHandler(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinHandler(), this);
        Bukkit.getPluginManager().registerEvents(new TownSpawnHandler(), this);
        Bukkit.getPluginManager().registerEvents(new playerdeath(),this);
        this.getCommand("astraWars").setExecutor(new AstraWarsCommandExecutor());
        this.getCommand("astraWars").setTabCompleter(new AstraWarsCommandTab());
        (new WarLoop()).startLoop(this);
        getCommand("ah").setTabCompleter(new AhCompleter());
        getCommand("telegraph").setExecutor(new bc());
        getCommand("telegraph").setTabCompleter(new telecomp());
        getCommand("zreload").setExecutor(new reload());
        getCommand("zp").setTabCompleter(new zptab());
        getCommand("zp").setExecutor(new zonoff());
        getCommand("nlist").setExecutor(new listnation());
        getCommand("nlist").setTabCompleter(new ncomp());
        getCommand("event").setTabCompleter(new eventcomp());
        getCommand("event").setExecutor(new event());
        getCommand("order").setExecutor(new order());
        getCommand("warproc").setExecutor(new WarProcGive());
        getCommand("warproc").setTabCompleter(new WarProcGive());
        getCommand("mailmenu").setExecutor(new mail());
        getCommand("mailmenu").setTabCompleter(new mail());
        getCommand("teleportmenu").setExecutor(new teleportmenu());
        getCommand("teleportmenu").setTabCompleter(new teleportmenu());
        getCommand("rebuild").setExecutor(new rebuild());
        @Nullable AddonCommand command = TownyCommandAddonAPI.getAddonCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN, "builds");
        command.setTabCompleter(new AdminTabCompleter());
        command.setPermission("admin.build");
        @Nullable AddonCommand command1 = TownyCommandAddonAPI.getAddonCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN, "spec");
        command1.setTabCompleter(new speccompletor());
        command1.setPermission("admin.build");
        @Nullable AddonCommand command2 = TownyCommandAddonAPI.getAddonCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN, "warlimit");
        command2.setTabCompleter(new adminsoldiercomp());
        command2.setPermission("aw.admin");
        @Nullable AddonCommand command3 = TownyCommandAddonAPI.getAddonCommand(TownyCommandAddonAPI.CommandType.NATION, "Назначить-военного");
        command3.setTabCompleter(new nationlol());
        command3.setPermission("aw.admin");
        @Nullable AddonCommand command4 = TownyCommandAddonAPI.getAddonCommand(TownyCommandAddonAPI.CommandType.NATION, "Снять-военного");
        command4.setTabCompleter(new nationcomp());
        command4.setPermission("aw.admin");
        @Nullable AddonCommand command5 = TownyCommandAddonAPI.getAddonCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN, "war");
        command5.setTabCompleter(new waraddadmincomp());
        command5.setPermission("aw.admin");
        @Nullable AddonCommand command7 = TownyCommandAddonAPI.getAddonCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN, "infotown");
        command7.setTabCompleter(new infotownpay());
        command7.setPermission("admin.build");
        @Nullable AddonCommand command8 = TownyCommandAddonAPI.getAddonCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN, "zavodwar");
        command8.setTabCompleter(new warlimitcomp());
        command8.setPermission("aw.admin");
        AddonCommand command9 = TownyCommandAddonAPI.getAddonCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN, "fuel");
        command9.setTabCompleter(new setfuelcomp());
        addCompletor("sectorbuy", TownyCommandAddonAPI.CommandType.TOWN,new sectorbuy());
        addCompletor("market",TownyCommandAddonAPI.CommandType.TOWN,new marketcomp());
        addCompletor("port", TownyCommandAddonAPI.CommandType.TOWN,new raftopencomp());
        addCompletor("limitsinfo",TownyCommandAddonAPI.CommandType.TOWN,new limitsinfocomp());
        TownyCommandAddonAPI.getAddonCommand(TownyCommandAddonAPI.CommandType.TOWN,"market").setPermission("market.towny");
        if (DependencyManager.ITEMS_LANG_API.isLoaded()){
            this.langAPI = ItemsLangAPI.getApi();
            this.langAPI.load(Lang.EN_US, Lang.RU_RU);
        }
            MilitaryDatabaseManager.createColumns();
            GunsDatabaseManager.createTable();
            MilitaryDatabaseManager.loadTownData();
            GunsDatabaseManager.loadGunStatistics();
            AuctionDatabaseManager.createTable();
            AuctionDatabaseManager.load();
            eventdatabaseManager.createTable();
            eventjoinmanager.createTable();
            nationdatabaseManager.createColumns();
            RaftMenuManage.createTable();
            PostOfficeDatabaseManager.createTable();
            BuildsDatabase.loadBuilddata();
            this.getServer().getConsoleSender().sendMessage("§x§4§0§f§7§8§1[ASTRACORE] База данных подключена.");

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        SimpleSettings.HIDE_NASHORN_WARNINGS = true;
    }
    public void addCompletor(String command, TownyCommandAddonAPI.CommandType commandType, TabCompleter completor){
        AddonCommand command9 = TownyCommandAddonAPI.getAddonCommand(commandType, command);
        assert command9 != null;
        command9.setTabCompleter(completor);
    }
   private void createBossBar() {
        String barstyle = Settings.BARSTYLE;
        bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.valueOf(barstyle));
        bossBar.setVisible(true);
    }

    private void startUpdateTask() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                updateBossBar(player);
            }
        }, 0L, 20L);
    }

    private BossBar bossBar;
    public void updateBossBarTitle(String newTitle) {
        bossBar.setTitle(ChatColor.translateAlternateColorCodes('&', newTitle));
    }

    private void updateBossBar(Player player) {
        String placeholderValue = PlaceholderAPI.setPlaceholders(player,"%server_time_HH:mm:ss%");
        String placeholderValue2 = PlaceholderAPI.setPlaceholders(player,"%yearplugin_year%");
            updateBossBarTitle("&fСегодня &x&f&f&d&a&a&4"+placeholderValue2+"&f год &f● Время: &x&f&f&d&a&a&4"+placeholderValue);
            bossBar.setColor(BarColor.WHITE);
            String barstyle = Settings.BARSTYLE;
            bossBar.setStyle(BarStyle.valueOf(barstyle));

            if (!bossBar.getPlayers().contains(player)) {
                bossBar.addPlayer(player);
            }
    }
    @Override
    public void onPluginStop() {
        if (AstraWarsData.hasEnabledWar) {
            War war = DataUtil.getFirstEnabledWar();
            Iterator var2 = war.getNationList().iterator();

            while(var2.hasNext()) {
                Nation nation = (Nation)var2.next();
                Iterator var4 = nation.getTowns().iterator();

                while(var4.hasNext()) {
                    Town town = (Town)var4.next();
                    TownyUtil.setWarPermissions(town, false);
                }
            }
        }
        serviceLayer.disable();
        utilityLayer.disable();
        getLogger().log(Level.INFO,"Сохранение базы данных..");
        mySQL.getExecutor().shutdown();
        while (!mySQL.getExecutor().isTerminated());
        getLogger().log(Level.INFO,"Сохранение базы данных завершено.");
    }


    public void send(Player player, String message) {
        String var10001 = Settings.prefix;
        player.sendMessage(var10001 + message);
    }

    public void sendPublic(String message) {
        String var10000 = Settings.prefix;
        Bukkit.broadcastMessage(var10000 + message);
    }
    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> registeredServiceProvider = getServer().getServicesManager().getRegistration(Economy.class);


        if (registeredServiceProvider == null) {
            return false;
        }
        economy = registeredServiceProvider.getProvider();
        return true;
    }

    /**
     * Шорткаты для утилит
     */

    public ChatUtility getChatUtility() {
        return utilityLayer.getChatUtility();
    }

    public Object getseceltinteger() {
        MySQL mySQL = MySQL.getInstance();
        return mySQL.mySQLinteger;
    }
    public String getpost(){
        PostOfficeDatabaseManager postOfficeDatabaseManager = new PostOfficeDatabaseManager();
        return postOfficeDatabaseManager.getdata();
    }

    public SpiGUI getSpiGui() {
        return utilityLayer.getSpiGUI();
    }
    public int getItemAmount(Player player, Material material) {
        AtomicInteger out = new AtomicInteger();
        Arrays.stream(player.getInventory().getContents()).forEach(itemStack -> {
            if (itemStack != null && itemStack.getType() == material) out.addAndGet(itemStack.getAmount());
        });
        return out.get();
    }
    public void rebuild(){
        MilitaryDatabaseManager.createColumns();
        nationdatabaseManager.createColumns();
    }

}






































