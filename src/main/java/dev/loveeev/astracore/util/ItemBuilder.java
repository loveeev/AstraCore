package dev.loveeev.astracore.util;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author loveeev
 * @since 0.1dev
 */
public abstract class ItemBuilder<B extends ItemBuilder<?>> {

    private List<Function<ItemStack, ItemStack>> itemChangers = new ArrayList<>();
    private List<Consumer<ItemStack>> itemModifications = new ArrayList<>();
    private List<Consumer<ItemMeta>> metaModifications = new ArrayList<>();

    public static MaterialItemBuilder fromMaterial(Material material) {
        return new MaterialItemBuilder(material);
    }

    public static MaterialItemBuilder fromMaterial(Material material, int data) {
        return new MaterialItemBuilder(material).setDurability(data);
    }

    public static ItemStackItemBuilder fromItem(ItemStack itemStack) {
        return new ItemStackItemBuilder(itemStack);
    }

    public B setName(String name) {
        metaModifications.add(meta -> meta.setDisplayName(TextUtility.colorize("&f" + name)));
        return getThis();
    }

    public B setCustomModelData(int customModelData) {
        metaModifications.add(meta -> meta.setCustomModelData(customModelData));
        return getThis();
    }

    public B setNameUncolored(String name) {
        metaModifications.add((meta) -> meta.setDisplayName(name));
        return getThis();
    }

    public B setLore(Collection<String> lore) {
        metaModifications.add(meta -> meta.setLore(new ArrayList<>(TextUtility.colorize(lore))));
        return getThis();
    }

    public B setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public B appendLore(String... lines) {
        metaModifications.add(meta -> {
            List<String> lore = getLore(meta);
            lore.addAll(Arrays.asList(lines));
            meta.setLore(TextUtility.colorize(lore));
        });
        return getThis();
    }

    public B formatLore(Function<List<String>, List<String>> formatter) {
        metaModifications.add(meta -> {
            List<String> lore = getLore(meta);
            meta.setLore(TextUtility.colorize(formatter.apply(lore)));
        });
        return getThis();
    }

    public B formatLoreLines(Function<String, String> formatter) {
        metaModifications.add(meta -> {
            List<String> lore = new ArrayList<>();
            getLore(meta).forEach(line -> lore.add(formatter.apply(line)));
            meta.setLore(TextUtility.colorize(lore));
        });
        return getThis();
    }

    private List<String> getLore(ItemMeta meta) {
        List<String> lore = meta.getLore();
        return lore == null ? new ArrayList<>() : lore;
    }

    public B setAmount(int amount) {
        itemModifications.add(item -> item.setAmount(amount));
        return getThis();
    }

    public B setDurability(short durability) {
        itemModifications.add(item -> item.setDurability(durability));
        return getThis();
    }

    public B setDurability(int durability) {
        return setDurability((short) durability);
    }

    public B setUnbreakable(boolean unbreakable) {
        metaModifications.add(meta -> meta.setUnbreakable(unbreakable));
        return getThis();
    }

    public B addItemFlag(ItemFlag itemFlag) {
        metaModifications.add(meta -> meta.addItemFlags(itemFlag));
        return getThis();
    }

    public B addEnchantment(Enchantment enchantment, int level) {
        metaModifications.add(meta -> meta.addEnchant(enchantment, level, true));
        return getThis();
    }

    public LeatherArmorBuilder leatherArmorBuilder() {
        return new LeatherArmorBuilder();
    }

    public PotionBuilder potionBuilder() {
        return new PotionBuilder();
    }

    public BannerBuilder bannerBuilder() {
        return new BannerBuilder();
    }

    @SuppressWarnings("unchecked")
    B getThis() {
        return (B) this;
    }

    protected abstract ItemStack getItemStack();

    public ItemStack build() {
        ItemStack temporaryItem = getItemStack();
        for (Function<ItemStack, ItemStack> changer : itemChangers) {
            temporaryItem = changer.apply(temporaryItem);
        }

        ItemStack item = temporaryItem;
        itemModifications.forEach(modification -> modification.accept(item));

        ItemMeta meta = item.getItemMeta();
        metaModifications.forEach(modification -> modification.accept(meta));
        item.setItemMeta(meta);
        return item;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MaterialItemBuilder extends ItemBuilder<MaterialItemBuilder> {

        private Material material;

        public MaterialItemBuilder setMaterial(Material material) {
            this.material = material;
            return getThis();
        }

        @Override
        protected ItemStack getItemStack() {
            return new ItemStack(material);
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ItemStackItemBuilder extends ItemBuilder<ItemStackItemBuilder> {

        @Getter(AccessLevel.PROTECTED)
        private ItemStack itemStack;

        public ItemStackItemBuilder setItemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
            return getThis();
        }

    }

    public class LeatherArmorBuilder {

        private Color color;

        public LeatherArmorBuilder setColor(Color color) {
            this.color = color;
            return this;
        }

        public B apply() {
            if (color != null) {
                metaModifications.add(meta -> {
                    LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
                    armorMeta.setColor(color);
                });
            }
            return getThis();
        }

    }

    public class PotionBuilder {

        private PotionEffectType mainEffectType;
        private List<PotionEffect> effects = new ArrayList<>();
        private Color color;

        public PotionBuilder mainEffectType(PotionEffectType type) {
            mainEffectType = type;
            return this;
        }

        public PotionBuilder customEffect(PotionEffect effect) {
            effects.add(effect);
            return this;
        }

        public PotionBuilder color(Color color) {
            this.color = color;
            return this;
        }

        public B apply() {
            if (mainEffectType != null) {
                metaModifications.add(meta -> ((PotionMeta) meta).setMainEffect(mainEffectType));
            }
            if (!effects.isEmpty()) {
                metaModifications.add(meta -> effects.forEach(effect -> ((PotionMeta) meta).addCustomEffect(effect, true)));
            }
            if (color != null) {
                metaModifications.add(meta -> ((PotionMeta) meta).setColor(color));
            }
            return getThis();
        }

    }

    public class BannerBuilder {

        private final List<Pattern> patterns = new ArrayList<>();

        public BannerBuilder addPattern(Pattern pattern) {
            patterns.add(pattern);
            return this;
        }

        public BannerBuilder addPattern(DyeColor color, PatternType type) {
            return addPattern(new Pattern(color, type));
        }

        public B apply() {
            if (!patterns.isEmpty()) {
                metaModifications.add(meta -> {
                    BannerMeta bannerMeta = (BannerMeta) meta;
                    bannerMeta.setPatterns(patterns);
                });
            }
            return getThis();
        }

    }

}