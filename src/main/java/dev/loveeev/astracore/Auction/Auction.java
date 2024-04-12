package dev.loveeev.astracore.Auction;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zimoxy DEV: loveeev
 */

public class Auction {

    @Getter
    private static final Auction instance = new Auction();

    /**
     * Список всех предметов, выставленных на аукцион
     */
    @Getter
    private final List<ItemToSell> items = new ArrayList<>();

    /**
     * Возвращает количество предметов, выставленное указанным игроком на аукцион.
     *
     * @param player игрок
     */
    public int countItemsByPlayer(Player player) {
        return getPlayerItems(player).size();
    }

    /**
     * Получить предметы, которые выставил указанный игрок на аукцион
     *
     * @param player игрок
     * @return список предметов ItemToSell
     */
    public List<ItemToSell> getPlayerItems(Player player) {
        List<ItemToSell> selfItems = new ArrayList<>();
        for (ItemToSell item : items) {
            if (item.getSellerUuid().equals(player.getUniqueId())) {
                selfItems.add(item);
            }
        }
        return selfItems;
    }

    public List<ItemToSell> getNotExpiredItems() {
        return items.stream().filter(i -> !i.isExpired()).toList();

    }
}



