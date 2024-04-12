package dev.loveeev.astracore.Auction;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import dev.loveeev.astracore.database.AuctionDatabaseManager;
import dev.loveeev.astracore.database.MySQL;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Zimoxy DEV: loveeev
 */
@Getter
@AllArgsConstructor
public class ItemToSell {

    private final String sellerName;
    private final UUID sellerUuid;
    private final ItemStack item;
    private Integer price;
    private Instant bidTime;
    private final String nation;
    private final String town;
    private final String data;
    public ItemToSell(Player seller, ItemStack item, Integer price,String nation,String town,String data){
        this.sellerName = seller.getName();
        this.sellerUuid = seller.getUniqueId();
        this.item = item;
        this.price = price;
        this.bidTime = Instant.now();
        this.nation = nation;
        this.town = town;
        this.data = data;
    }

    public void setPrice(Integer price) {
        this.price = price;
        AuctionDatabaseManager.setNewPrice(this);
    }

    public ItemStack getMenuView(Player player) throws TownyException {
        Resident resident = TownyAPI.getInstance().getResident(player);
        Nation nation1 = resident.getNation();
        try (Connection connection = MySQL.getInstance().getCon();
             PreparedStatement ps = connection.prepareStatement("SELECT poshlin FROM AHN_ALLOW WHERE nationowner = ? AND nationallow = ?")) {
            ps.setString(1, nation);
            ps.setString(2, nation1.getName());

            ResultSet rs = ps.executeQuery(); // ResultSet не в try-with-resources
            if(Objects.equals(nation1.getName(), nation)){
                return ItemCreator.of(item)
                        .lore("",
                                "&fЦена: &x&f&f&d&a&a&4" + price,
                                "&fПродавец: &x&f&f&d&a&a&4" + sellerName,
                                "&fНация: &#FFDAA4" + nation,
                                "&fГород: &#FFDAA4" + town,
                                "&fДата выставления: &#ffdaa4" + data,
                                "",
                                "&3Этот предмет продается вашей нацией",
                                "&3Пошлина не накладывается",
                                "",
                                "&8Нажмите &x&f&f&d&a&a&4ЛКМ&8, чтобы купить"

                        )
                        .make();
            }
            if (!rs.next()) {
                return ItemCreator.of(item)
                        .lore("",
                                "&fЦена: &x&f&f&d&a&a&4" + price,
                                "&fПродавец: &x&f&f&d&a&a&4" + sellerName,
                                "&fНация: &#FFDAA4" + nation,
                                "&fГород: &#FFDAA4" + town,
                                "",
                                "&3Вы не можете купить этот предмет.",
                                "&3Для покупки вас должна добавить нация &#FFDAA4" + nation,
                                "",
                                "&8Нажмите &x&f&f&d&a&a&4ЛКМ&8, чтобы купить"
                        )
                        .make();
            } else {
                double ro = rs.getDouble("poshlin");
                if (ro == 0) {
                    return ItemCreator.of(item)
                            .lore("",
                                    "&fЦена: &x&f&f&d&a&a&4" + price,
                                    "&fПродавец: &x&f&f&d&a&a&4" + sellerName,
                                    "&fНация: &#FFDAA4" + nation,
                                    "&fГород: &#FFDAA4" + town,
                                    "",
                                    "&3Пошлина равна: &#ffdaa40",
                                    "",
                                    "&8Нажмите &x&f&f&d&a&a&4ЛКМ&8, чтобы купить"
                            )
                            .make();
                } else {
                    return ItemCreator.of(item)
                            .lore("",
                                    "&fЦена: &x&f&f&d&a&a&4" + price,
                                    "&fПродавец: &x&f&f&d&a&a&4" + sellerName,
                                    "&fНация: &#FFDAA4" + nation,
                                    "&fГород: &#FFDAA4" + town,
                                    "",
                                    "&3Для вашей нации установленна пошлина: &#FFDAA4" + ro,
                                    "",
                                    "&8Нажмите &x&f&f&d&a&a&4ЛКМ&8, чтобы купить"
                            )
                            .make();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ItemStack getOwnMenuView(){
        List<String> lores = new ArrayList<>();
        lores.add("");
        lores.add("&fЦена: &x&f&f&d&a&a&4" + price);
        lores.add("");
        lores.add("&8Это ваш предмет.");
        lores.add("&8Нажмите, чтобы снять его с продажи.");
        if (isExpired()){
            lores.add("");
            lores.add("&cЭтот предмет не покупали более 2 дней,");
            lores.add("&cпоэтому его не видно на общем мировом рынке.");
            lores.add("");
        }

        return ItemCreator.of(item).lore(lores).make();
    }

    public boolean isOwner(Player player){
        return sellerUuid.equals(player.getUniqueId());
    }

    public boolean isExpired(){
        return getBidTime().plus(48, ChronoUnit.HOURS).isBefore(Instant.now());
    }
}