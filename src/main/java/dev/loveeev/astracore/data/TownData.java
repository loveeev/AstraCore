package dev.loveeev.astracore.data;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.database.GunsDatabaseManager;
import dev.loveeev.astracore.database.MilitaryDatabaseManager;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.gui.builds.MilitaryInventoryMenu;
import dev.loveeev.astracore.gui.builds.invFuel;
import dev.loveeev.astracore.gui.builds.yardinv;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.constants.FoConstants;
import org.mineacademy.fo.menu.AdvancedMenu;

import java.util.HashMap;
import java.util.Map;

/**
 * Этот класс содержит дополнительную информацию о городе:
 * - производимое оружие
 * - инвентарь военного завода
 * - меню инвентаря военного завода (если открыто)
 * - статистика произведенного оружия
 * - является ли город премиальным
 */
@Getter
@Setter
@ToString
public class TownData {

    @Getter
    private static final Map<String, TownData> townsData = new HashMap<>();

    private final String townName;
    private Gun producingGun;
    private Map<Integer, ItemStack> militaryInventory;
    private Map<Integer, ItemStack> fuelinv;
    private Map<Integer, ItemStack> moneyInventory;
    private invFuel invfuel;
    private yardinv moneyinv;
    private MilitaryInventoryMenu militaryInventoryMenu;
    private Map<Gun, Integer> gunStatistics = new HashMap<>();
    private boolean isPremium;
    public boolean isiron;
    public boolean iscoal;

    private TownData(String townName) {
        this(townName, null, new HashMap<>(), false,new HashMap<>(),new HashMap<>());
    }

    /**
     * Этот конструктор нужен, чтобы создавать экземпляр TownData из данных, которые пришли из БД
     */
    public TownData(String townName, Gun producingGun, @NotNull Map<Integer, ItemStack> militaryInventory, boolean isPremium,@NotNull Map<Integer, ItemStack> fuelinv,@NotNull Map<Integer, ItemStack> moneyInventory) {
        this.townName = townName;
        this.producingGun = producingGun;
        this.militaryInventory = militaryInventory;
        this.isPremium = isPremium;
        this.fuelinv = fuelinv;
        this.moneyInventory = moneyInventory;
        townsData.put(townName, this);
    }

    /**
     * Смотри {@link #getOrCreate(String)}
     * @param player игрок
     * @return экземпляр TownData
     * @throws NotRegisteredException если игрок не состоит в городе
     */
    public static TownData getOrCreate(Player player) throws NotRegisteredException {
        Town town = getPlayerTown(player);
        return TownData.getOrCreate(town.getName());
    }

    /**
     * Получить TownData по названию города, или создать, если оно еще не существует.
     * @param name название города
     * @return экземпляр TownData
     */
    public static TownData getOrCreate(String name){
        if (townsData.containsKey(name)){
            return townsData.get(name);
        }
        return new TownData(name);
    }

    /**
     * Установить производимое оружие
     * @param producingGun оружие
     */
    public void setProducingGun(Gun producingGun) {
        this.producingGun = producingGun;
        updateProducingGun();
    }

    /**
     * Установить инвентарь военного завода в виде СЛОТ-ПРЕДМЕТ этому городу, и обновить его в БД
     * @param militaryInventory инвентарь в виде СЛОТ-ПРЕДМЕТ
     */
    public void setMilitaryInventory(Map<Integer, ItemStack> militaryInventory) {
        this.militaryInventory = militaryInventory;
        updateMilitaryInventory();
    }
    /**
     * Установить инвентарь военного завода в виде СЛОТ-ПРЕДМЕТ этому городу, и обновить его в БД
     * @param fuelinv инвентарь в виде СЛОТ-ПРЕДМЕТ
     */
    public void setFuelInventory(Map<Integer, ItemStack> fuelinv) {
        this.fuelinv = fuelinv;
        updateFuelInventory();
    }
    public void setMoneyInventory(Map<Integer, ItemStack> moneyInventory) {
        this.moneyInventory = moneyInventory;
        updateMoneyInventory();
    }

    /**
     * Установить (заменить) количество произведенного оружия для оружия с данным названием
     * @param gunName название оружия
     * @param stat количество, которое нужно установить
     */
    public void setGunStatistic(String gunName, int stat){
        Gun gun = GunStorage.getByName(gunName);
        if (gun == null) return;
        gunStatistics.put(gun, stat);
        updateGunStats();
    }

    /**
     * Установить статус премиум для этого города и обновить значение в БД
     * @param premium новый статус
     */
    public void setPremium(boolean premium) {
        isPremium = premium;
        updatePremium();
    }

    public void setIron(boolean proiziron) {
        isiron = proiziron;
    }
    public void setCoal(boolean proizcoal) {
        iscoal = proizcoal;
    }
    /**
     * Добавить данное количество произведенного оружия к данному оружию
     * @param gun данное оружие
     * @param stat количество, которое было произведено
     */
    public void addGunStatistic(Gun gun, int stat){
        Integer existingStat = gunStatistics.get(gun);
        if (existingStat == null){
            gunStatistics.put(gun, stat);
        } else {
            gunStatistics.put(gun, stat + existingStat);
        }
        updateGunStats();
    }

    @Nullable
    public static TownData getByName(String name){
        return townsData.get(name);
    }

    public static Town getPlayerTown(Player player) throws NotRegisteredException {
        Resident resident = TownyAPI.getInstance().getResident(player.getUniqueId());
        if (resident == null){
            throw new NotRegisteredException();
        }
        return resident.getTown();
    }

    /**
     * Проверить, находится ли указанный игрок в городе
     * @param player игрок
     * @return true если игрок состоит в городе
     */
    public static boolean isPlayerInTown(Player player){
        try{
            getPlayerTown(player);
            return true;
        } catch (NotRegisteredException e){
            return false;
        }
    }

    /**
     * Проверить, существует ли город, именуемый названием из этого экземпляра TownData
     * @return true если существует
     */
    public boolean isTownStillExist(){
        return TownyAPI.getInstance().getTown(this.townName) != null;
    }

    public boolean isNationExist() {
        if (isTownStillExist()) {
            Town town = TownyAPI.getInstance().getTown(townName);
            assert town != null;
            return town.hasNation(); // Check if the town belongs to any nation
        } else {
            return false;
        }
    }

    public Nation isNation() {
        if (isTownStillExist()) {
            try {
                Town town = TownyAPI.getInstance().getTown(this.townName);
                assert town != null;
                if (town.hasNation()) {
                    return town.getNation(); // Return the nation object
                } else {
                    return null; // Town doesn't belong to any nation
                }
            } catch (NotRegisteredException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null; // Town doesn't exist
        }
    }


    /**
     * Проверить, открыто ли меню инвентаря военного завода.
     * Нужно, чтобы не дать игроку открыть меню, если его уже просматривает другой игрок.
     * @return true если открыто
     */
    public boolean isMilitaryFactoryMenuOpened(){
        if (militaryInventoryMenu == null) return false;
        return militaryInventoryMenu.isReallyOpened();
    }
    public boolean isFuelMenuOpened(){
        if (invfuel == null) return false;
        return invfuel.isReallyOpened();
    }
    public boolean isMoneyYardOpened(){
        if (moneyinv == null) return false;
        return moneyinv.isReallyOpened();
    }

//    /**
//     * Получить экземпляр открытого меню инвентаря военного завода у указанного игрока
//     * @param player игрок
//     * @return экземпляр меню
//     */
//    @Nullable
//    public static MilitaryInventoryMenu getOpenedMilitaryMenu(final Player player){
//        if (player == null || !player.isOnline()) return null;
//        if (player.hasMetadata("MILITARY_MENU")) {
//            final Object object = player.getMetadata("MILITARY_MENU").get(0).value();
//
//            if (object instanceof MilitaryInventoryMenu){
//                return (MilitaryInventoryMenu) object;
//            }
//        }
//        return null;
//    }

    /**
     * Получить экземпляр открытого меню (AdvancedMenu) у указанного игрока
     * @param player игрок
     * @return экземпляр меню
     */
    @Nullable
    public static AdvancedMenu getOpenedMenu(final Player player){
        if (player.hasMetadata(FoConstants.NBT.TAG_MENU_CURRENT)){
            return (AdvancedMenu) player.getMetadata(FoConstants.NBT.TAG_MENU_CURRENT).get(0).value();
        }
        return null;
    }

    /**
     * Обновить инвентарь военного завода в БД
     */
    public void updateMilitaryInventory(){
        if (isDatabaseConnected()){
            MilitaryDatabaseManager.setInventory(townName, militaryInventory);
        }
    }
    public void updateFuelInventory(){
        if (isDatabaseConnected()){
            MilitaryDatabaseManager.setInventoryFuel(townName, fuelinv);
        }
    }
    public void updateMoneyInventory(){
        if (isDatabaseConnected()){
            MilitaryDatabaseManager.setMoneyYard(townName, moneyInventory);
        }
    }
    /**
     * Обновить производимое оружие в БД
     */
    public void updateProducingGun(){
        if (isDatabaseConnected()) {
            MilitaryDatabaseManager.setGun(townName, producingGun);
        }
    }

    /**
     * Обновить статистику произведенного оружия в БД
     */
    public void updateGunStats(){
        if (isDatabaseConnected()){
            GunsDatabaseManager.setGunStat(townName, producingGun, gunStatistics.get(producingGun));
        }
    }

    public void updatePremium(){
        if (isDatabaseConnected()){
            MilitaryDatabaseManager.setPremium(townName, isPremium);
        }
    }

    /**
     * Сбросить все данные города на нулевые значения.
     * Используется, когда город удаляется из Towny.
     */
    public void resetTown(){
        this.producingGun = null;
        this.militaryInventory = new HashMap<>();
        this.militaryInventoryMenu = null;
        this.gunStatistics = new HashMap<>();
        this.fuelinv = new HashMap<>();
        this.invfuel = null;
        this.moneyInventory = new HashMap<>();
        this.moneyinv = null;
        resetGunStats();
    }

    private void resetGunStats(){
        if (MySQL.getInstance().isConnected()){
            GunsDatabaseManager.resetGunStats(townName);
        }
    }

    private boolean isDatabaseConnected(){
        return MySQL.getInstance().isConnected();
    }

}
