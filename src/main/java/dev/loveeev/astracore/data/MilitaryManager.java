package dev.loveeev.astracore.data;

import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.settings.Settings;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MilitaryManager {
    /*
     * Произвести оружие для всех городов.
     */
    public static int produceWeapons(){
        int produced = 0;

        // Пробежаться по всем городам
        for (TownData townData : TownData.getTownsData().values()){
            // Если города больше не существует, то пропускаем его
            if (!townData.isTownStillExist()) continue;
            // Если инвентарь военного завода города пустой, то пропускаем его (так как нет ингредиентов для крафта оружия)
            if (townData.getMilitaryInventory().isEmpty()) continue;
            // Получаем оружие, выбранное городом для производства
            Gun producing = townData.getProducingGun();
            // Если город не выбрал оружие для производства, то пропускаем его
            if (producing == null) continue;
            MySQL.getInstance().selectstring("TOWNY_TOWNS","name", townData.getTownName(), "WarProc","integer");
            Integer proc = (Integer)Main.getInstance().getseceltinteger();
            double dayLimit = proc / townData.getProducingGun().getProc();
            // Производим оружие столько раз, сколько лимит у этого оружия на дневное производство
            for (int i = 0; i < Math.ceil(dayLimit); i++){
                // Получаем стоимость производства одной штуки оружия
                Map<Integer, Integer> costs = getMilitaryInventoryCostItems(townData, producing.getCost());
                // Если стоимость пустая - значит у города недостаточно ресурсов для крафта оружия, завершаем производство
                if (costs.isEmpty()){
                    // Записываем в статистику количество произведенного оружия
                    townData.addGunStatistic(producing, produced);
                    // Обновляем инвентарь военного завода
                    townData.updateMilitaryInventory();
                    // Возвращаем количество произведенного оружия (от 0 до дневного ограничения этого типа оружия)
                    return produced;
                }

                // Пробегаемся по стоимости производства оружия (СЛОТ-ПРЕДМЕТ)
                // И удаляем из инвентаря военного завода те предметы, которые требуются для производства оружия
                for (Map.Entry<Integer, Integer> entry : costs.entrySet()){
                    ItemStack inventoryItem = townData.getMilitaryInventory().get(entry.getKey());
                    int initialAmount = inventoryItem.getAmount();
                    int removedAmount = entry.getValue();
                    if (initialAmount == removedAmount){
                        townData.getMilitaryInventory().remove(entry.getKey());
                    } else {
                        inventoryItem.setAmount(initialAmount - entry.getValue());
                    }
                }

                // Ищем первый пустой слот в инвентаре военного завода
                int firstEmptySlot = getFirstEmptySlot(townData.getMilitaryInventory());

                // Если инвентарь заполнен, то завершаем производство и не добавляем оружие
                if (firstEmptySlot == -1){
                    // Записываем в статистику количество произведенного оружия
                    townData.addGunStatistic(producing, produced);
                    // Обновляем инвентарь военного завода
                    townData.updateMilitaryInventory();
                    // Возвращаем количество произведенного оружия (от 0 до дневного ограничения этого типа оружия)
                    return produced;
                } else {
                    // Если у игрока в это время открыт инвентарь военного завода, то его нужно закрыть,
                    // чтобы игрок открыл меню заново с обновленными предметами
                    if (townData.isMilitaryFactoryMenuOpened()){
                        townData.getMilitaryInventoryMenu().closeMenu();
                    }
                    // Ставим произведенное оружие на первый пустой слот
                    townData.getMilitaryInventory().put(firstEmptySlot, producing.getItemStack());
                    // Добавляем 1 к счетчику произведенного оружия
                    produced++;
                }
            }

            // Записываем в статистику количество произведенного оружия
            townData.addGunStatistic(producing, produced);
            // Обновляем инвентарь военного завода
            townData.updateMilitaryInventory();
        }

        // Возвращаем количество произведенного оружия (от 0 до дневного ограничения этого типа оружия)
        return produced;
    }

    /**
     * Получить первый свободный слот в мапе СЛОТ-ПРЕДМЕТ
     * Этот метод пробегается по всем слотам от 0 до 56 и если видит, что он не заполнен, то возвращает его.
     * Возвращает -1 если все слоты от 0 до 56 заняты.
     */
    public static int getFirstEmptySlot(Map<Integer, ItemStack> items){
        for (int i = 0; i < 56; i++){
            if (items.get(i) == null){
                return i;
            }
        }
        return -1;

//        Set<Integer> busySlots = new HashSet<>(items.keySet());
//        Set<Integer> allSlots = IntStream.rangeClosed(0, 56).boxed().collect(Collectors.toSet());
//        allSlots.removeAll(busySlots);
//
//        if (allSlots.isEmpty()) return 0;
//        return Collections.min(allSlots);
    }

    public static Map<Integer, Integer> getMilitaryInventoryCostItems(TownData townData, List<ItemStack> requiredItems){
        final Map<Integer, Integer> itemsSlotsAmounts = new HashMap<>();

        for (ItemStack requiredItem : requiredItems){
            ItemStack req = requiredItem.clone();

            // Делаем цену в 2 раза ниже для премиум городов
            if (townData.isPremium()){
                // Но только если изначальная цена больше 1 предмета
                if (requiredItem.getAmount() > 1){
                    req.setAmount((int) (requiredItem.getAmount() / Settings.PREMIUM_TOWN_DISCOUNT));
                }
            }

            // Рассчитываем предметы, которые нужно забрать из инвентаря военного завода для изготовления оружия
            Map<Integer, Integer> map = getOneRequiredItemCost(townData.getMilitaryInventory(), req);
            itemsSlotsAmounts.putAll(map);
        }

        return itemsSlotsAmounts;
    }

    public static Map<Integer, Integer> getOneRequiredItemCost(Map<Integer, ItemStack> itemStacks, ItemStack requiredItem){
        final Map<Integer, Integer> itemsSlotsAmounts = new HashMap<>();
        int requiredAmount = requiredItem.getAmount();

        for (Map.Entry<Integer, ItemStack> slotItem : itemStacks.entrySet()){
            if (slotItem.getValue() == null) continue;
            ItemStack itemOnSlot = slotItem.getValue().clone();
            if (!itemOnSlot.isSimilar(requiredItem)) continue;

            int localFound = itemOnSlot.getAmount();

            // Если материалов уже хватает, сразу завершаем
            if (requiredAmount == 0){
                return itemsSlotsAmounts;
            }
            if (localFound < requiredAmount){
                itemsSlotsAmounts.put(slotItem.getKey(), localFound);
                requiredAmount -= localFound;
            } else {
                itemsSlotsAmounts.put(slotItem.getKey(), requiredAmount);
                return itemsSlotsAmounts;
            }
        }

        if (requiredAmount == 0){
            return itemsSlotsAmounts;
        }
        return new HashMap<>();
    }

    public static Map<Integer, ItemStack> itemsArrayToMap(ItemStack[] array){
        Map<Integer, ItemStack> map = new HashMap<>();
        for (int i = 0; i < array.length; i++){
            if (array[i] == null) continue;
            map.put(i, array[i]);
        }
        return map;
    }

}
