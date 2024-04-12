package dev.loveeev.astracore.gui;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.WorldCoord;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.gui.impl.BuildingMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.menu.AdvancedMenu;
import org.mineacademy.fo.menu.AdvancedMenuPagged;
import org.mineacademy.fo.menu.MenuSlots;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RAFTMENU extends AdvancedMenuPagged<Town> {

    private final Main plugin;

    public RAFTMENU(Player player, Main plugin) {
        super(player);
        this.plugin = plugin;
    }

    @Override
    protected List<Town> getElements() {
        List<Town> towns = new ArrayList<>();
        Player viewer = getPlayer();
        if (viewer == null) {
            return towns;
        }
            try {
                Connection connection = MySQL.getInstance().getCon();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM RaftMenu WHERE nationallow = ?");
                statement.setString(1, viewerNation.getName());

                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    String townName = rs.getString("nation");
                    Town town = TownyAPI.getInstance().getTown(townName);

                    if (town != null) {
                        towns.add(town);
                    }
                }

            } catch (SQLException e) {
                Main.getInstance().getLogger().severe("Error executing SQL query: " + e.getMessage());
            }

        return towns;
    }


    @Override
    protected ItemStack convertToItemStack(@NotNull Town town) {
        List<String> lores = new ArrayList<>();
        try {
            lores.add("&fМэр: &#ffdaa4" + town.getMayor().getName());
            lores.add("&fСпавн: X: &#ffdaa4" + town.getHomeBlock().getWorldCoord().getLowerMostCornerLocation().getX() +
                    "&fY: &#ffdaa4" + town.getHomeBlock().getWorldCoord().getLowerMostCornerLocation().getZ());

            return ItemCreator.of(CompMaterial.HEART_OF_THE_SEA)
                    .name("&fПорт города &#ffdaa4" + town.getName())
                    .lore(lores)
                    .make();
        } catch (TownyException e) {
            lores.add("&fОшибка при получении информации о мэре или HomeBlock.");
        }

        // Возвращаем пустой предмет, если город не соответствует условиям запроса
        return new ItemStack(Material.AIR);
    }




    @Override
    protected void setup() {
        setSize(9 * 6);
        setTitle("         Доступные порты");
        setWrapper(CompMaterial.fromMaterial(Material.BLACK_STAINED_GLASS_PANE));
        setLockedSlots(MenuSlots.Shape.BOUNDS);
        addButton(45, getBackButton0());
        setPreviousButtonItem(ItemCreator.of(CompMaterial.ARROW).name("&#FFDAA4Предыдущая страница").hideTags(true).make());
        setNextButtonItem(ItemCreator.of(CompMaterial.ARROW).name("&#FFDAA4Следующая страница").hideTags(true).make());
        setPageButtonsAlwaysEnabled(true);
    }

    private Button getBackButton0() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                new BuildingMenu(player).display();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.YELLOW_STAINED_GLASS_PANE)
                        .name("&eНазад")
                        .make();
            }
        };
    }

    @Override
    protected void onElementClick(Player player, Town town, int slot, ClickType clickType) {
        try {
            TownBlock homeBlock = town.getHomeBlock();
            WorldCoord homeBlockLocation = homeBlock.getWorldCoord();

            World world = Bukkit.getWorld(homeBlockLocation.getWorldName()); // Получаем мир по его имени
            double x = homeBlockLocation.getLowerMostCornerLocation().getX();
            double z = homeBlockLocation.getLowerMostCornerLocation().getZ();
            float yaw = 0; // Угол поворота
            float pitch = 0; // Угол наклона
            Block highestBlock = world.getHighestBlockAt((int) x, (int) z);
            int highestY = highestBlock.getY();
            Location location = new Location(world, x, highestY + 1, z, yaw, pitch);

            player.teleport(location);
        } catch (TownyException e) {
            throw new RuntimeException(e);
        }

    }
}