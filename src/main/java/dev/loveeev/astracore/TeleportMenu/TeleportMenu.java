package dev.loveeev.astracore.TeleportMenu;

import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.Teleports;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.menu.AdvancedMenuPagged;
import org.mineacademy.fo.menu.MenuSlots;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

public class TeleportMenu extends AdvancedMenuPagged<TeleportButton> {
    private static Queue<Player> teleportQueue = new ConcurrentLinkedQueue<>();

    public TeleportMenu(Player player) {
        super(player);
    }

    @Override
    protected List<TeleportButton> getElements() {
        return Teleports.getTELEPORT_BUTTONS();
    }

    @Override
    protected ItemStack convertToItemStack(@NotNull TeleportButton teleportButton) {
        List<String> lore = new ArrayList<>();

        return ItemCreator.of(CompMaterial.GRASS_BLOCK) // Используем голову игрока
                .name("&#FFDAA4" + teleportButton.getName())
                .lore("") // Добавляем необходимые подсказки
                .make();
    }

    @Override
    protected void setup() {
        setSize(9 * 5);
        setTitle("Выбрать точку спавна");
        setLockedSlots(MenuSlots.Shape.BOUNDS);
        setPreviousButtonItem(ItemCreator.of(CompMaterial.SPECTRAL_ARROW).name("&#FFDAA4Предыдущая страница").hideTags(true).make());
        setNextButtonItem(ItemCreator.of(CompMaterial.TIPPED_ARROW).name("&#FFDAA4Следующая страница").hideTags(true).make());
        setPageButtonsAlwaysEnabled(true);
    }

    @Override
    protected void onElementClick(Player player, TeleportButton teleportButton, int slot, ClickType clickType) {
        int x1 = teleportButton.getX1();
        int x2 = teleportButton.getX2();
        int y1 = teleportButton.getY1();
        int y2 = teleportButton.getY2();

        if (teleportQueue.offer(player)) {
            int queuePosition = teleportQueue.size();
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы добавлены в очередь на телепортацию. Место в очереди: " + queuePosition);
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> teleportPlayer(player, teleportButton, x1, x2, y1, y2));
        } else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы уже находитесь в очереди на телепортацию.");
        }
    }

    private void teleportPlayer(Player player, TeleportButton teleportButton, int x1, int x2, int y1, int y2) {
        Location teleportLocation = getRandomLocation(x1, x2, y1, y2);
        Main.getInstance().getChatUtility().sendSuccessNotification(player,"Происходит телепортация...");
        if (teleportLocation != null) {
            // Загрузка чанка для местоположения телепортации
            Chunk chunk = teleportLocation.getChunk();
            if (!chunk.isLoaded()) {
                chunk.load();
            }

            teleportLocation.setZ(teleportLocation.getZ() + 1);

            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                player.teleport(teleportLocation);
                player.getActivePotionEffects().clear();
                removeFromQueue(player);
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы успешно доставлены в " + teleportButton.getName());
            });
        } else {
            removeFromQueue(player);
            Main.getInstance().getChatUtility().sendErrorNotification(player, "Не удалось найти подходящее местоположение для телепортации.");
        }
    }


    private void removeFromQueue(Player player) {
        teleportQueue.remove(player);
    }

    private Location getRandomLocation(int x1, int x2, int y1, int y2) {
        World world = Bukkit.getWorld("world");

        for (int i = 0; i < 10; i++) {
            int randomX = getRandomIntInRange(x1, x2);
            int randomZ = getRandomIntInRange(y1, y2);

            int chunkX = randomX >> 4;
            int chunkZ = randomZ >> 4;

            Chunk chunk = world.getChunkAt(chunkX, chunkZ);

            int blockX = randomX & 15;
            int blockZ = randomZ & 15;

            int y = chunk.getWorld().getMaxHeight();

            for (; y > 0; y--) {
                if (!chunk.getBlock(blockX, y, blockZ).isEmpty()) {
                    break;
                }
            }

            Location randomLocation = new Location(world, randomX, y, randomZ);
            if (y > 0 && !randomLocation.getBlock().isLiquid()) {
                return randomLocation;
            }
        }

        return null;
    }

    private int getRandomIntInRange(int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}