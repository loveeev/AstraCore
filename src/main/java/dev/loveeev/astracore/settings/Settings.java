package dev.loveeev.astracore.settings;

import com.google.common.base.CaseFormat;
import org.mineacademy.fo.annotation.AutoStaticConfig;
import org.mineacademy.fo.settings.SimpleSettings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@AutoStaticConfig(format = CaseFormat.LOWER_UNDERSCORE)
public class Settings extends SimpleSettings {

    public static List<String> SELL_ITEM_LORE = new ArrayList<>();
    public static Integer AMOUNTCOPPER;
    public static Double PRICECOPPER;
    public static Double PRICEAGRAR;
    public static Double METALSECTOR;
    public static Double LESSECTOR;
    public static List<String> KITSITEMPVP = new ArrayList<>();
    public static List<String> KITSITEMPVPTWO = new ArrayList<>();

    public static Integer MONEYYARD;
    public static Boolean MARKETMONEY;
    public static Integer TIMEWAR;
    public static String BARSTYLE;
    public static Double INKPRICE;
    public static Boolean DEATHONWAR;
    public static Integer IRONAMOUNT;

    public static Integer COALAMOUNT;

    public static Double SELL_WHEAT;
    public static Double SELL_CARROT;
    public static Double SELL_POTATO;
    public static Double SELL_SEED;
    public static Double SELL_BEETROOT;
    public static Double SELL_SWEET;
    public static Double SELL_BEEF;
    public static Double SELL_MUTTON;
    public static Double SELL_CHICK;
    public static Double SELL_SN;
    public static Integer TOWNPRICE;
    public static Integer TIMEFOROCCUOATION;
    public static String PREFIXPLAYER;
    public static Integer CAPITULATIONPERCENT;
    public static String prefix;
    public static String forbiddenCommands;
    //ВОЕННЫЙ
    public static String NAMEWAR;
    public static List<String> LOREWAR = new ArrayList<>();
    //ВОЕННЫЙ+
    public static String NAMEWARP;
    public static List<String> LOREWARPREM = new ArrayList<>();
    //ПОРТ
    public static String NAMEPORT;
    public static List<String> LOREPORT = new ArrayList<>();
    //БАНК
    public static String NAMEBANK;
    public static List<String> LOREBANK = new ArrayList<>();
    //ТЕЛЕГРАФ
    public static String NAMET;
    public static List<String> LORET = new ArrayList<>();
    //ПРЕДПРИЯТИЯ
    public static String NAMEP;
    public static List<String> LOREPRED = new ArrayList<>();
    //СПЕЦ-ПРОЕКТ
    public static String NAMESPEC;
    public static List<String> LORESPEC = new ArrayList<>();
    //ФЕРМА
    public static String NAMEFARM;
    public static List<String> LOREFARM = new ArrayList<>();

    //#Нефтеперерабатывающий завод
    public static String NAMEFUELCRAFT;
    public static List<String> LOREFUELCRAFT = new ArrayList<>();
    public static String NAMEFUEL;
    public static List<String> LOREFUEL = new ArrayList<>();
    //Сталелитейный завод
    public static String STEELNAME;
    public static List<String> STEELLORE = new ArrayList<>();

    public static String FABRICNAME;
    public static List<String> FABRICLORE = new ArrayList<>();
    public static String YARDNAME;
    public static List<String> YARDLORE = new ArrayList<>();
    public static String NOT_IN_TOWN_MESSAGE;
    public static Double PRICEGOLD;
    public static Double PREMIUM_TOWN_DISCOUNT;
    public static Double BANK_COINS;
    public static String MYSQL_USERNAME;
    public static String MYSQL_HOST;
    public static String MYSQL_PASSWORD;
    public static String MYSQL_DATABASE;
    public static String AMOUNTCOBL;
    public static String ITEMA;
    public static String PRICEA;
    public static String AMOUNTA;
    public static String ITEME;
    public static String PRICEE;
    public static String AMOUNTE;
    public static String PRICEG;
    public static String ITEMIRON;
    public static String PRICEIRON;
    public static String AMOUNTIRON;
    public static String PRICEELI;
    public static String AMOUNTELI;
    public static String PRICEBER;
    public static String AMOUNTBEREZ;
    public static String PRICETROPIC;
    public static String AMOUNTTROPIC;
    public static String PRICEACA;
    public static String AMOUNTACA;
    public static String PRICENIGA;
    public static String AMOUNTNIGA;
    public static String PRICEDUB;
    public static String AMOUNTDUB;
    public static String ITEMCOBL;
    public static String PRICECOBL;
    public static Double PRICETANK1;
    public static Double PRICETANK2;
    public static Double PRICETANK3;
    public static Double PRICETRAIN1;
    public static Double PRICETRAIN2;
    public static Double PRICETRAIN3;
    public static Double PRICEPLANE1;
    public static Double PRICEPLANE2;
    public static Double PRICEPLANE3;
    public static Double PRICEVERF1;
    public static Double PRICEVERF2;
    public static Double PRICEVERF3;
    public static Double PRICEKG1;
    public static Double PRICEKG2;
    public static Double PRICEKG3;
    public static Double PRICEDRILL1;
    public static Double PRICEDRILL2;
    public static Double PRICEDRILL3;
    public static Double PRICEEZ1;
    public static Double PRICEEZ2;
    public static Double PRICEEZ3;
    public static Double PRICEBIKE1;
    public static Double PRICEBIKE2;
    public static Double PRICEBIKE3;


    @Override
    protected String getSettingsFileName() {
        return "config.yml";
    }
    public static void printAllValues(Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        try{
            for (Field f : fields){
                f.setAccessible(true);
                System.out.println(clazz.getSimpleName() + "." + f.getName() + " = " + f.get(null));
            }
            for (Class<?> cl : clazz.getDeclaredClasses()){
                printAllValues(cl);
            }
        } catch (IllegalAccessException ignored){}
    }
}
