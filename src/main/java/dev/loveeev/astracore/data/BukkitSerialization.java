package dev.loveeev.astracore.data;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class BukkitSerialization {

    /**
     * Закодировать предмет в строку с помощью алгоритма Base64
     * @param itemStack предмет
     * @return закодированная строка
     * @throws IllegalStateException
     */
    public static String itemToBase64(ItemStack itemStack) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(itemStack);

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stack.", e);
        }
    }

    /**
     * Получить предмет из закодированной строки
     * @param data закодированная строка
     * @return предмет
     * @throws IOException
     */
    public static ItemStack itemFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            ItemStack itemsMap = (ItemStack) dataInput.readObject();

            dataInput.close();
            return itemsMap;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    /**
     * Закодировать мапу слоты-предметы в строку Base64
     * @param itemsMap мапа
     * @return
     * @throws IllegalStateException
     */
    public static String itemsMapToBase64(Map<Integer, ItemStack> itemsMap) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Записать всю мапу
            dataOutput.writeObject(itemsMap);

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * Получить мапу слоты-предметы из закодированной строки Base64
     * @param data закодированная строка
     * @return
     * @throws IOException
     */
    public static Map<Integer, ItemStack> itemsMapFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            // Получить мапу из закодированной строки
            Map<Integer, ItemStack> itemsMap = (Map<Integer, ItemStack>) dataInput.readObject();

            dataInput.close();
            return itemsMap;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

}
