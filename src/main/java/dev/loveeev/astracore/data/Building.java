package dev.loveeev.astracore.data;

import dev.loveeev.astracore.settings.Settings;
import lombok.Getter;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.List;

public class Building {

    @Getter
    private static final List<BuildingData> BUILDING_DATA = new ArrayList<>();

    public static BuildingData ZavodWar = new BuildingData(Settings.NAMEWAR,Settings.LOREWAR, CompMaterial.IRON_INGOT,1);
    public static BuildingData WarPrem = new BuildingData(Settings.NAMEWARP,Settings.LOREWARPREM,CompMaterial.GOLD_INGOT,2);
    public static BuildingData Port = new BuildingData(Settings.NAMEPORT,Settings.LOREPORT,CompMaterial.PRISMARINE_SHARD,3);
    //1 ЗОЛОТО В ЧАС
    public static BuildingData Bank = new BuildingData(Settings.NAMEBANK, Settings.LOREBANK,CompMaterial.GOLD_BLOCK,4);
    public static BuildingData TeleGraph = new BuildingData(Settings.NAMET,Settings.LORET,CompMaterial.BROWN_WOOL,5);
    public static BuildingData pred = new BuildingData(Settings.NAMEP,Settings.LOREPRED,CompMaterial.DIAMOND,6);
    //public static BuildingData fuelcraft = new BuildingData(Settings.NAMEFUELCRAFT,Settings.LOREFUELCRAFT,CompMaterial.INK_SAC,7);
    //public static BuildingData fuel = new BuildingData(Settings.NAMEFUEL,Settings.LOREFUEL,CompMaterial.INK_SAC,8);
    public static BuildingData steel = new BuildingData(Settings.STEELNAME,Settings.STEELLORE,CompMaterial.NETHERITE_SCRAP,9);
    public static BuildingData fabric = new BuildingData(Settings.FABRICNAME,Settings.FABRICLORE,CompMaterial.LEATHER_CHESTPLATE,10);
    public static BuildingData yard = new BuildingData(Settings.YARDNAME,Settings.YARDLORE,CompMaterial.SANDSTONE,11);
    public static void addAll(BuildingData buildingData){
        BUILDING_DATA.add(buildingData);
    }
}
