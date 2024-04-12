package dev.loveeev.astracore.module.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.TownData;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.gui.RAFTMENU;
import dev.loveeev.astracore.gui.builds.*;
import dev.loveeev.astracore.gui.customcrafts.crafts;
import dev.loveeev.astracore.gui.impl.BuildingMenu;
import dev.loveeev.astracore.gui.impl.Donate;
import dev.loveeev.astracore.gui.impl.sell;
import dev.loveeev.astracore.gui.sellmenus.farmsell;
import dev.loveeev.astracore.gui.sellmenus.goldsell;
import dev.loveeev.astracore.module.SpigotService;
import dev.loveeev.astracore.settings.Settings;
import dev.loveeev.astracore.util.ItemBuilder;
import me.rubix327.itemslangapi.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Zimoxy DEV: loveeev
 */
public class ExchangeService extends SpigotService {

    PreparedStatement ps;

    ResultSet rs;

    private static ExchangeService instance;
    MySQL mySQL = MySQL.getInstance();

    public static ExchangeService getInstance() {
        return instance;
    }

    public ExchangeService(Main plugin) {
        super(plugin);
    }

    public void RaftMenu(Player player){
        Main.getInstance().getChatUtility().sendSuccessNotification(player,"Загрузка....");
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Town town = TownyAPI.getInstance().getTown(player);
            mySQL.selectstring("TOWNY_TOWNS", "name", town.getName(), "carg", "integer");
            Integer tf = (Integer) Main.getInstance().getseceltinteger();
            if (tf == 1) {
                new RAFTMENU(player, plugin).display();
            } else {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас не построен порт.");
            }
        });

    }
    public void MoneyYard(Player player){
        if(check(player,"MoneyYard")){
             new MoneyYardGui(player, plugin).display();
        }else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас не открыт монетный двор.");
        }
    }
    public Boolean check(Player player, String columnname) {
        Town town = TownyAPI.getInstance().getTown(player);
        try {
            ResultSet resultSet = MySQL.getInstance().executeQuery("SELECT * FROM `TOWNY_TOWNS` WHERE name = ?", town.getName());
            if (resultSet.next()) {
                Object o = resultSet.getObject(columnname);
                if(o == null){
                    return false;
                }else {
                    return (Integer) o != 0;
                }
            }
            return false; // Если запрос не вернул результатов, вернуть false
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void exchange(Player player) {
        plugin.getChatUtility().sendSuccessNotification(player, "Вы перешли на следующую страницу.");
    }
    public void exchanges(Player player) {
    }
    public void exchangegolden(int amount, Player player) {
        if (getGoldAmount(player) < amount) {
            plugin.getChatUtility().sendErrorNotification(player, "У вас недостаточно золота!");
            return;
        }
        if (amount <= 0) {
            plugin.getChatUtility().sendErrorNotification(player, "У вас нет золота в инвентаре!");
            return;
        }
        removeGold(player, amount);
        double exchangeRate = Settings.PRICEGOLD;
        Double totalMoney = exchangeRate * amount;
        plugin.getEconomy().depositPlayer(player, totalMoney);
        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы обменяли " + amount + " золота на " + totalMoney + " монет.");
    }

    public void fuelmenu(Player player) throws NotRegisteredException {
        data = TownData.getOrCreate(player);
        Resident resident =  TownyAPI.getInstance().getResident(player);
        mySQL.selectstring("TOWNY_TOWNS","name",resident.getTown().getName(),"cargfuel","integer");
        Integer tf = (Integer)Main.getInstance().getseceltinteger();
        if(tf == 1) {
            new fuelzavod(player, data,plugin).display();
        }else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас не построена нефтяная станция.");
        }
    }
    public void fabric(Player player) throws NotRegisteredException {
        data = TownData.getOrCreate(player);
        Town town = TownyAPI.getInstance().getTown(player);
        mySQL.selectstring("TOWNY_TOWNS","name",town.getName(),"cargfabric","integer");
        Integer tf = (Integer)Main.getInstance().getseceltinteger();
        if(tf == 1) {
            new stanok(player,plugin,data).display();
        }else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас не построена Текстильная фабрика.");
        }
    }
    public void tank(Player player){
        Town town = TownyAPI.getInstance().getTown(player);
        mySQL.selectstring("TOWNY_TOWNS","name",town.getName(),"cargtank","integer");
        Integer tf = (Integer)Main.getInstance().getseceltinteger();
        if(tf == 1) {
            new tankz(plugin).create(player).open(player);
        }else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас не построен Танкостроительный завод.");
        }
    }
    public void train(Player player){
        Town town = TownyAPI.getInstance().getTown(player);
        mySQL.selectstring("TOWNY_TOWNS","name",town.getName(),"cargtrain","integer");
        Integer tf = (Integer)Main.getInstance().getseceltinteger();
        if(tf == 1) {
            new trainz(plugin).create(player).open(player);
        }else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас не построен Локомотивный завод.");
        }
    }
    public void car(Player player){
        Town town = TownyAPI.getInstance().getTown(player);
        mySQL.selectstring("TOWNY_TOWNS","name",town.getName(),"cargcar","integer");
        Integer tf = (Integer)Main.getInstance().getseceltinteger();
        if(tf == 1) {
            new carz(plugin).create(player).open(player);
        }else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас не построен завод легкой техники.");
        }
    }
    public void drill(Player player){
        Town town = TownyAPI.getInstance().getTown(player);
        mySQL.selectstring("TOWNY_TOWNS","name",town.getName(),"cargdrill","integer");
        Integer tf = (Integer)Main.getInstance().getseceltinteger();
        if(tf == 1) {
            new drillz(plugin).create(player).open(player);
        }else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас не построен завод тяжелой техники.");
        }
    }
    public void plane(Player player){
        Town town = TownyAPI.getInstance().getTown(player);
        mySQL.selectstring("TOWNY_TOWNS","name",town.getName(),"cargplane","integer");
        Integer tf = (Integer)Main.getInstance().getseceltinteger();
        if(tf == 1) {
            new planez(plugin).create(player).open(player);
        }else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас не построен авиационно строительный завод.");
        }
    }
    public void raft(Player player){
        Town town = TownyAPI.getInstance().getTown(player);
        mySQL.selectstring("TOWNY_TOWNS","name",town.getName(),"cargraft","integer");
        Integer tf = (Integer)Main.getInstance().getseceltinteger();
        if(tf == 1) {
            new raftz(plugin).create(player).open(player);
        }else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас не построен авиационно строительный завод.");
        }
    }

    public void fuelcraftmenu(Player player) throws NotRegisteredException {
        data = TownData.getOrCreate(player);
        Resident resident =  TownyAPI.getInstance().getResident(player);
        mySQL.selectstring("TOWNY_TOWNS","name",resident.getTown().getName(),"cargfuelcraft","integer");
        Integer tf = (Integer)Main.getInstance().getseceltinteger();
        if(tf == 1) {
            new fuelcraftzavod(player, data,plugin).display();
        }else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас не построена Нефтеперерабатывающий завод.");
        }
    }


    private void removeGold(Player player, int amount) {
        for (ItemStack itemStack : player.getInventory()) {
            if (amount == 0) {
                break;
            }
            if (itemStack != null && itemStack.getType() == Material.GOLD_INGOT) {
                if (amount > itemStack.getAmount()) {
                    amount -= itemStack.getAmount();
                    itemStack.setAmount(0);
                    itemStack.setType(Material.AIR);
                } else {
                    itemStack.setAmount(itemStack.getAmount() - amount);
                    amount = 0;
                }
            }
        }
    }

    public void givenet(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (cooldownsnet.containsKey(playerUUID)) {
            long lastClaimTime = cooldownsnet.get(playerUUID);
            long currentTime = System.currentTimeMillis();

            long cooldown = 10 * 60 * 1000; // 10 минут

            if (currentTime < lastClaimTime + cooldown) {
                String cooldownMessage = "Ты уже обменял ресурсы, подожди еще " + formatTime(lastClaimTime + cooldown - currentTime) + ".";
                setCooldownText(cooldownMessage); // Установите текст кулдауна
                Main.getInstance().getChatUtility().sendSuccessNotification(player, cooldownMessage);
                return;
            }
        }

        exchangenet(Settings.IRONAMOUNT, Settings.COALAMOUNT, player);

    }

    private String cooldownText = "";
    public void setCooldownText(String text) {
        // Установите текст кулдауна
        this.cooldownText = text;
    }
    public String getCooldownText() {
        return this.cooldownText;
    }


    public void exchangenet(int amount,int amountcoal, Player player) {
        try {
            Town town = TownyAPI.getInstance().getTown(player);
            ResultSet rs = MySQL.getInstance().executeQuery("SELECT * FROM `TOWNY_TOWNS` WHERE name = ?", town.getName());
            if (rs.next()) {
                int pr = rs.getInt("WarProc");
                int amountnez = pr/5;
                if (getIronAmount(player) < amount * amountnez) {
                    plugin.getChatUtility().sendErrorNotification(player, "У вас недостаточно ресурсов");
                    return;
                }
                if (getCoalAmount(player) < amountcoal * amountnez) {
                    plugin.getChatUtility().sendErrorNotification(player, "У вас недостаточно ресурсов");
                    return;
                }
                if (amount* amountnez <= 0 || amountcoal* amountnez <= 0) {
                    plugin.getChatUtility().sendErrorNotification(player, "У вас недостаточно ресурсов в инвентаре!");
                    return;
                }
                removeiron(player, amount* amountnez);
                removeCoal(player, amountcoal* amountnez);
                ItemStack net = new ItemStack(Material.NETHERITE_SCRAP, amountnez);
                player.getInventory().addItem(net);
                player.updateInventory();
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы успешно обменяли ресурсы на незеритовый лом");
                UUID playerUUID = player.getUniqueId();
                cooldownsnet.put(playerUUID, System.currentTimeMillis());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void transferitem(int amountitem1,int amountitem2,int amountitem3,Material material1,Material material2,Material material3, Player player,Material giveitem,int amountgive,int modeldata) {
        if (getItemAmount(player,material1) < amountitem1 && getItemAmount(player,material2) < amountitem2 && getItemAmount(player,material3) < amountitem3) {
            plugin.getChatUtility().sendErrorNotification(player, "У вас недостаточно ресурсов");
            return;
        }
        if (amountitem1 <= 0 || amountitem2 <= 0 || amountitem3 <= 0) {
            plugin.getChatUtility().sendErrorNotification(player, "У вас недостаточно ресурсов в инвентаре!");
            return;
        }
        removeitem(player,amountitem1,material1);
        removeitem(player,amountitem2,material2);
        removeitem(player,amountitem3,material3);
        ItemStack net = new ItemStack(ItemBuilder
                .fromMaterial(giveitem)
                .setLore("Броня нации")
                .setAmount(amountgive)
                .setCustomModelData(modeldata)
                .build());
        player.getInventory().addItem(net);
        player.updateInventory();
        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Успешно!");
    }

    private void removeiron(Player player, int amount) {
        for (ItemStack itemStack : player.getInventory()) {
            if (amount == 0) {
                break;
            }
            if (itemStack != null && itemStack.getType() == Material.IRON_INGOT) {
                if (amount > itemStack.getAmount()) {
                    amount -= itemStack.getAmount();
                    itemStack.setAmount(0);
                    itemStack.setType(Material.AIR);
                } else {
                    itemStack.setAmount(itemStack.getAmount() - amount);
                    amount = 0;
                }
            }
        }
    }
    public void removeitem(Player player, int amount, Material material) {
        for (ItemStack itemStack : player.getInventory()) {
            if (amount == 0) {
                break;
            }
            if (itemStack != null && itemStack.getType() == material) {
                if (amount > itemStack.getAmount()) {
                    amount -= itemStack.getAmount();
                    itemStack.setAmount(0);
                    itemStack.setType(Material.AIR);
                } else {
                    itemStack.setAmount(itemStack.getAmount() - amount);
                    amount = 0;
                }
            }
        }
    }
    private void removeCoal(Player player, int amount) {
        for (ItemStack itemStack : player.getInventory()) {
            if (amount == 0) {
                break;
            }
            if (itemStack != null && itemStack.getType() == Material.COAL) {
                if (amount > itemStack.getAmount()) {
                    amount -= itemStack.getAmount();
                    itemStack.setAmount(0);
                    itemStack.setType(Material.AIR);
                } else {
                    itemStack.setAmount(itemStack.getAmount() - amount);
                    amount = 0;
                }
            }
        }
    }
    public int getIronAmount(Player player) {
        AtomicInteger out = new AtomicInteger();
        Arrays.stream(player.getInventory().getContents()).forEach(itemStack -> {
            if (itemStack != null && itemStack.getType() == Material.IRON_INGOT) out.addAndGet(itemStack.getAmount());
        });
        return out.get();
    }
    public int getItemAmount(Player player,Material material) {
        AtomicInteger out = new AtomicInteger();
        Arrays.stream(player.getInventory().getContents()).forEach(itemStack -> {
            if (itemStack != null && itemStack.getType() == material) out.addAndGet(itemStack.getAmount());
        });
        return out.get();
    }
    public int getCoalAmount(Player player) {
        AtomicInteger out = new AtomicInteger();
        Arrays.stream(player.getInventory().getContents()).forEach(itemStack -> {
            if (itemStack != null && itemStack.getType() == Material.COAL) out.addAndGet(itemStack.getAmount());
        });
        return out.get();
    }
    public int getGoldAmount(Player player) {
        AtomicInteger out = new AtomicInteger();
        Arrays.stream(player.getInventory().getContents()).forEach(itemStack -> {
            if (itemStack != null && itemStack.getType() == Material.GOLD_INGOT) out.addAndGet(itemStack.getAmount());
        });
        return out.get();
    }
    public void exchangechat(Player player){
        Main.getInstance().getChatUtility().sendSuccessNotification(player, "&f→ &7Ссылки на наши ресурсы");
        Main.getInstance().getChatUtility().sendSuccessNotification(player, "&fДонат&7: https://astraworld.pro");
        Main.getInstance().getChatUtility().sendSuccessNotification(player,"&fГруппа ВК&7: https://vk.com/astraworld");
        Main.getInstance().getChatUtility().sendSuccessNotification(player, "&fДискорд&7: https://discord.gg/gSJmxTsaP8");
    }


    public void exchangesell(Player player){
        Resident resident = TownyAPI.getInstance().getResident(player);
        if(resident.hasTown()) {
            new sell(player,plugin,data).display();
        }else {
            new goldsell(plugin).create(player).open(player);
        }
    }
    public void exchangesdon(Player player){
        new Donate(plugin).create(player).open(player);
    }
    public void exchangefarm(Player player) {
        Resident resident = TownyAPI.getInstance().getResident(player);

        TownData data;
        try {
            data = TownData.getOrCreate(player);
        } catch (NotRegisteredException e) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы не состоите в городе.");
            return;
        }
        Town town = TownyAPI.getInstance().getTown(player);
        mySQL.selectstring("TOWNY_TOWNS", "name", town.getName(), "farmer", "integer");

        int resulttf = (Integer)Main.getInstance().getseceltinteger();
        if (resulttf == 0) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас не аграрного сектора");
        } else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы открыли меню аграрного сектора");
            new farmsell(plugin).create(player).open(player);
        }
    }
    public void exchangefarmsell(Player player){
        new farmsell(plugin).create(player).open(player);
    }

    TownData data;
    public void sellmenuex(Player player){
        new sell(player,plugin,data).display();
    }
    public void givetransport(Player player, String type, String name,Double price){
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        String command = "give" + type + " " + player.getName() + " " + name;
        double balance = Main.getInstance().getEconomy().getBalance(player);
        if(balance >= price){
            Main.getInstance().getEconomy().withdrawPlayer(player,price);
            Bukkit.dispatchCommand(console,command);
        }else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас недостаточно средств.");
        }
    }
    public void exchangewar(Player player) {

        Resident resident = TownyAPI.getInstance().getResident(player);

        TownData data;
        try {
            data = TownData.getOrCreate(player);
        } catch (NotRegisteredException e) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы не состоите в городе.");
            return;
        }
            mySQL.selectstring("TOWNY_TOWNS","name",TownyAPI.getInstance().getTown(player).getName(),"cargw","integer");
            int resulttf;
            resulttf = (Integer)Main.getInstance().getseceltinteger();
                if (resulttf == 0) {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас не построен военный завод.");
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы открыли меню военного завода.");
                    new WarMenu(player, data).display();
                }
    }

    public void exchangewarprem(Player player) {

        Resident resident = TownyAPI.getInstance().getResident(player);
        TownData data;
        try {
            data = TownData.getOrCreate(player);
        } catch (NotRegisteredException e) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы не состоите в городе.");
            return;
        }
        try {
            ps = MySQL.getInstance().getCon().prepareStatement("SELECT * FROM `TOWNY_TOWNS` WHERE `name` = ?"); // Обязательно нужно делать с ?, на строчке ниже ты передаешь аргументы которые над вставить
            assert resident != null;
            ps.setString(1, resident.getTown().getName()); // ЛуЧШЕ каждый арг. делать в отдельной строке - для каждого типа, INT, Double и тд
            rs = ps.executeQuery();
            if (!rs.next()) { // rs.next() - тут проверяется есть ли результат такой в БД
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Ошибка базы данных, обратитесь за помощью к администратору.");
            } else {
                boolean resulttf;
                resulttf = rs.getBoolean("premium");
                if (!resulttf) {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас не куплен Военный завод+.");
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы открыли меню Военного завода+.");
                    new WarMenu(player, data).display();
                    System.out.println(data);
                }
            }
        } catch (SQLException | NotRegisteredException e) {
            throw new RuntimeException(e);
        }
    }

    public void exchangeah(Player player) {

        Resident resident = TownyAPI.getInstance().getResident(player);
        TownData data;
        try {
            data = TownData.getOrCreate(player);
        } catch (NotRegisteredException e) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы не состоите в городе.");
            return;
        }
            mySQL.selectstring("TOWNY_TOWNS","name",TownyAPI.getInstance().getTown(player).getName(),"carg","integer");
            int resulttf;
            resulttf = (Integer)Main.getInstance().getseceltinteger();
                if (resulttf == 0) {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас не построен порт. Он нужен для использования мирового рынка.");
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы открыли мировой рынок.");
                    new AuctionMenu(player).display();
                }
    }

    public void itemsell(Player player, Double price, Material material) {
        Double bal = Main.getInstance().getEconomy().getBalance(player);
        if(bal < price){
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас недостаточно средств для покупки ");
        }else {
            Main.getInstance().getEconomy().withdrawPlayer(player, price);
            ItemStack item = new ItemStack(material, 32);
            Inventory playerinv = player.getInventory();
            playerinv.addItem(item);
            player.updateInventory();
        }
    }

    public void itemsell2(Player player, Double price, Material material) {
        Double bal = Main.getInstance().getEconomy().getBalance(player);
        if(bal < price){
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас недостаточно средств для покупки ");
        }else {
            Main.getInstance().getEconomy().withdrawPlayer(player, price);
            ItemStack item = new ItemStack(material, 16);
            Inventory playerinv = player.getInventory();
            playerinv.addItem(item);
            player.updateInventory();
        }
    }

    public void sellfarm(Player player, Double price, Material material, int amount){
        Double bal = Main.getInstance().getEconomy().getBalance(player);
        if(bal < price){
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас недостаточно средств для покупки ");
        }else {
            Main.getInstance().getEconomy().withdrawPlayer(player, price);
            ItemStack item = new ItemStack(material, amount);
            Inventory playerinv = player.getInventory();
            playerinv.addItem(item);
            player.updateInventory();
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы успешно купили - " + Main.getInstance().getLangAPI().translate(item, Lang.RU_RU));
        }
    }
    public void sellfarmall(Player player, Double price, Material material, int amount) {
        if (player.getInventory().containsAtLeast(new ItemStack(material), amount)) {
            player.getInventory().removeItem(new ItemStack(material, amount));
            Main.getInstance().getEconomy().depositPlayer(player, price);
            player.updateInventory();
            String itemname;
            itemname = Main.getInstance().getLangAPI().translate(material, Lang.RU_RU);
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы продали " + itemname + " за " + price);
        } else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас недостаточно ресурсов!");
        }
    }

    public void exchangebuy(Player player) {
        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Успешная покупка.");
    }

    public void exchangedonate(Player player){
        new Donate(plugin).create(player).open(player);
    }


    public void exchangebank(Player player) {

        Resident resident = TownyAPI.getInstance().getResident(player);
        TownData data;
        try {
            data = TownData.getOrCreate(player);
        } catch (NotRegisteredException e) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы не состоите в городе.");
            return;
        }
            mySQL.selectstring("TOWNY_TOWNS","name",TownyAPI.getInstance().getTown(player).getName(),"bank","integer");
            int resulttf;
            resulttf = (Integer)Main.getInstance().getseceltinteger();
                if (resulttf == 0) {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас не построен банк.");
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы открыли меню банка.");
                    new bankgui(plugin).create(player).open(player);
                }
    }

    public void agrsector(Player player){
        Town town = TownyAPI.getInstance().getTown(player);
    }

    public void exchangeclose(Player player) {
        player.closeInventory();
    }
    public void exchangegunpowder(Player player){
        player.performCommand("recipe gunpowder -c");
    }
    public void exchangeblazerod(Player player){
        player.performCommand("recipe blazerod -c");
    }
    public void exchangecrimson(Player player){
        player.performCommand("recipe crimsonstem -c");
    }
    public void exchangenetherbricks(Player player){
        player.performCommand("recipe netherbricks -c");
    }
    public void exchangequartz(Player player){
        player.performCommand("recipe quartz -c");
    }
    public void exchangesaddle(Player player){
        player.performCommand("recipe saddle -c");
    }
    public void exchangechestplate(Player player){
        player.performCommand("recipe chainmailchestplate -c");
    }
    public void exchangeleggins(Player player){
        player.performCommand("recipe chainmailleggings -c");
    }
    public void exchangehelmet(Player player){
        player.performCommand("recipe chainmailhelmet -c");
    }
    public void exchangeboots(Player player){
        player.performCommand("recipe chainmailboots -c");
    }
    public void exchangebonemeal(Player player){
        player.performCommand("recipe bonemeal -c");
    }
    public void exchangewarped(Player player){
        player.performCommand("recipe warpedstem -c");
    }


    public void exchangecrafts(Player player){
        new crafts(plugin).create(player).open(player);
    }
    public void exhcnageguii(Player player) {
        new BuildingMenu(player).display();
    }

    public void bankgui(Player player) {
    }

    public void statopen(Player player){
        Town town = TownyAPI.getInstance().getTown(player);
        mySQL.selectstring("TOWNY_TOWNS","name",town.getName(),"stazavod","integer");
        Integer tf =(Integer) Main.getInstance().getseceltinteger();
        if(tf == 1){
            new stalzavod(plugin).create(player).open(player);
        }else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас не построен сталетейный завод");
        }
    }


    public void exchangeitems(Player player) {
        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Пока для строительства имеются только эти предметы, но вы можете предложить еще на нашем форуме -  https://forum.zimoxy.org");
    }

    public void exchangerab(Player player) {
        Main.getInstance().getChatUtility().sendSuccessNotification(player, "В разработке..");
    }

    private HashMap<UUID, Long> cooldowns = new HashMap<>();
    private HashMap<UUID, Long> cooldownsspec = new HashMap<>();
    private HashMap<UUID, Long> cooldownsmayor = new HashMap<>();

    public HashMap<UUID, Long> getCooldownsnet() {
        return cooldownsnet;
    }

    private HashMap<UUID, Long> cooldownsnet = new HashMap<>();

    public void givemoney(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (cooldowns.containsKey(playerUUID)) {
            long lastClaimTime = cooldowns.get(playerUUID);
            long currentTime = System.currentTimeMillis();

            long cooldown = 24 * 60 * 60 * 1000; // 24 часа в миллисекундах

            if (currentTime < lastClaimTime + cooldown) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Ты уже получил монеты, подожди еще " + formatTime(lastClaimTime + cooldown - currentTime) + ".");
                return;
            }
        }

        Double money;
        money = Settings.BANK_COINS;
        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вам выдано "+ money + " монет. Вы сможете получить еще через 24 часа.");
        Main.getInstance().getEconomy().depositPlayer(player, money);

        cooldowns.put(playerUUID, System.currentTimeMillis());
    }
    public boolean isCooldownActive(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (cooldownsnet.containsKey(playerUUID)) {
            long lastClaimTime = cooldownsnet.get(playerUUID);
            long currentTime = System.currentTimeMillis();

            long cooldown = 10 * 60 * 1000; // 10 минут

            if (currentTime < lastClaimTime + cooldown) {
                return true; // Кулдаун активен
            }
        }

        return false; // Кулдаун не активен
    }

    public void specmoney(Player player){
        Resident resident = TownyAPI.getInstance().getResident(player);
        TownData data;
        try {
            data = TownData.getOrCreate(player);
        } catch (NotRegisteredException e) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы не состоите в городе.");
            return;
        }
            mySQL.selectstring("TOWNY_TOWNS","name",TownyAPI.getInstance().getTown(player).getName(),"spec","integer");
            int resulttf;
            resulttf = (Integer)Main.getInstance().getseceltinteger();
                UUID playerUUID = player.getUniqueId();
                if (cooldownsspec.containsKey(playerUUID)) {
                    long lastClaimTime = cooldownsspec.get(playerUUID);
                    long currentTime = System.currentTimeMillis();

                    long cooldownsspec = 24 * 60 * 60 * 1000; // 24 часа в миллисекундах

                    if (currentTime < lastClaimTime + cooldownsspec) {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Ты уже получил монеты, подожди еще " + formatTime(lastClaimTime + cooldownsspec - currentTime) + ".");
                        return;
                    }
                }

                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вам выдано "+ resulttf + " монет. Вы сможете получить еще через 24 часа.");
                Main.getInstance().getEconomy().depositPlayer(player, resulttf);
                cooldownsspec.put(playerUUID, System.currentTimeMillis());
    }
    private String formatTime(long millis) {
        long seconds = millis / 1000 % 60;
        long minutes = millis / (60 * 1000) % 60;
        long hours = millis / (60 * 60 * 1000) % 24;
        long days = millis / (24 * 60 * 60 * 1000);

        String timeString = "";
        if (days > 0) {
            timeString += days + " дн. ";
        }
        if (hours > 0) {
            timeString += hours + " ч. ";
        }
        if (minutes > 0) {
            timeString += minutes + " мин. ";
        }
        if (seconds > 0) {
            timeString += seconds + " сек.";
        }

        return timeString.trim();
    }
    public void exchangetele(Player player){
        Resident resident = TownyAPI.getInstance().getResident(player);
        TownData data;
        try {
            data = TownData.getOrCreate(player);
        } catch (NotRegisteredException e) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы не состоите в городе.");
            return;
        }
            mySQL.selectstring("TOWNY_TOWNS","name",TownyAPI.getInstance().getTown(player).getName(),"bc","integer");
                int resulttf;
                resulttf = (Integer)Main.getInstance().getseceltinteger();
                if (resulttf == 0) {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас не построен телеграф");
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Используйте /telegraph текст");
                }
    }
}