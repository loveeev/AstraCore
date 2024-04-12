package dev.loveeev.astracore.data;

import dev.loveeev.astracore.TeleportMenu.TeleportButton;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Teleports {

    @Getter
    private static final List<TeleportButton> TELEPORT_BUTTONS = new ArrayList<>();

    public static TeleportButton AUSTRALIA_AND_OCEANIA = new TeleportButton("Австралия и Океания", 13000, -760, 24560, 6800);
    public static TeleportButton SOUTH_AFRICA = new TeleportButton("Южная Африка", 1200, -400, 7000, 4800);
    public static TeleportButton NORTHWEST_AFRICA = new TeleportButton("Северо-западная Африка", -2500, -4900, 3200, -450);
    public static TeleportButton MIDDLE_EAST = new TeleportButton("Ближний Восток", 3600, -5750, 8400, -1400);
    public static TeleportButton INDOSTAN = new TeleportButton("Индостан", 8350, -4750, 13300, -850);
    public static TeleportButton SOUTH_ASIA = new TeleportButton("Южная Азия", 13300, -3400, 17400, -740);
    public static TeleportButton CENTRAL_CHINA = new TeleportButton("Центральный Китай", 13300, -3400, 16700, -4800);
    public static TeleportButton FAR_EAST = new TeleportButton("Дальний Восток", 16800, -4150, 20600, -7500);
    public static TeleportButton GREAT_STEPPE = new TeleportButton("Великая степь", 7000, -5150, 15480, -6800);
    public static TeleportButton URAL = new TeleportButton("Урал", 7000, -6800, 9200, -9300);
    public static TeleportButton WEST_SIBERIA = new TeleportButton("Западная Сибирь", 9200, -10300, 12600, -6800);
    public static TeleportButton EAST_SIBERIA = new TeleportButton("Восточная Сибирь", 12600, -10300, 18400, -6800);
    public static TeleportButton BEZUMEZ = new TeleportButton("Все регионы", -24578, -12288, 24576, 12288);
    public static void addAll(TeleportButton teleportButton){
        TELEPORT_BUTTONS.add(teleportButton);
    }
}
