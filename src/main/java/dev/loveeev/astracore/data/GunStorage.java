package dev.loveeev.astracore.data;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Этот класс содержит все оружия, доступные для производства
 */


public class GunStorage {

    @Getter
    private static final List<Gun> guns = new ArrayList<>();

    // Чтобы создать новое оружие, нужно добавить еще одну похожую строчку ниже
    // Первым аргументом указывается название оружия (Gewehr 98), вторым - modelData,
    // третьим - дневное ограничение, четвертым - список предметов нужных для производства
    public static Gun PISTOL = new Gun("Пистолет", 111, "6", 5, true, "Пистолетные патроны", List.of(new ItemStack(Material.IRON_INGOT, 16)));
    public static Gun REVOLVER = new Gun("Револьвер", 112, "8", 7, true, "Винтовочные патроны", List.of(new ItemStack(Material.IRON_INGOT, 16)));
    public static Gun RIFLE = new Gun("Винтовка", 121, "13", 10, true, "Винтовочные патроны", List.of(new ItemStack(Material.IRON_INGOT, 24)));
    public static Gun SUBMACHINE = new Gun("Пистолет-пулемёт", 131, "3", 15, true, "Пистолетные патроны", List.of(new ItemStack(Material.IRON_INGOT, 24)));
    public static Gun SHOTGUN = new Gun("Дробовик", 141, "2x10", 15, true, "Картечь", List.of(new ItemStack(Material.IRON_INGOT, 24)));

    public static void addGun(Gun gun){
        guns.add(gun);
    }

    /**
     * @param name название оружия (например, "P08 (Люгер)")Ы
     * @return экземпляр Gun или null
     */
    @Nullable
    public static Gun getByName(String name){
        for (Gun gun : guns) {
            if (gun.getName().equals(name)){
                return gun;
            }
        }
        return null;
    }
}
