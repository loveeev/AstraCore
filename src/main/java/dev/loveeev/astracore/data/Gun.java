package dev.loveeev.astracore.data;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.List;
import java.util.Objects;

/**
 * Этот класс представляет собой оружие, которое можно производить в меню Военного завода
 */
@Getter
public class Gun {

    /**
     * Название оружия (отображается в игре)
     */
    private final String name;
    /**
     * ModelData оружия (для отображения модельки из ресурспака)
     */
    private final int modelData;
    /**
     * Дневное ограничение на производство
     */
    /**
     * Список предметов, нужных для изготовления оружия
     */
    private final List<ItemStack> cost;

    private final String damage;


    private final String patron;

    private final double proc;

    private final boolean base;

    public Gun(String name, int modelData,String damage,double proc,Boolean base, String patron, List<ItemStack> cost) {
        this.name = name;
        this.modelData = modelData;
        this.cost = cost;
        this.damage = damage;
        this.patron = patron;
        this.proc = proc;
        this.base = base;

        GunStorage.addGun(this);
    }

    /**
     * Получить предмет оружия.
     * Этот предмет добавляется в меню Инвентаря завода, когда оружие произведено
     */
    public ItemStack getItemStack(){
        return ItemCreator.of(CompMaterial.CROSSBOW)
                .modelData(modelData)
                .name(name)
                .make();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gun gun = (Gun) o;
        return Objects.equals(name, gun.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
