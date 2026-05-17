package dev.rndmorris.salisarcana.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import dev.rndmorris.salisarcana.SalisArcana;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;

public class WandHelper {

    private static final HashMap<Item, HashMap<Integer, WandCap>> WAND_CAPS = new HashMap<>();
    private static final HashMap<Item, HashMap<Integer, WandRod>> WAND_RODS = new HashMap<>();

    public static void loadWandParts() {
        for (final var cap : WandCap.caps.values()) {
            final var itemStack = cap.getItem();
            if (itemStack == null) continue;

            final var item = itemStack.getItem();
            if (item == null) continue;

            WAND_CAPS.computeIfAbsent(item, ignore -> new HashMap<>())
                .put(itemStack.getItemDamage(), cap);
        }

        for (final var rod : WandRod.rods.values()) {
            final var itemStack = rod.getItem();
            if (itemStack == null) continue;

            final var item = itemStack.getItem();
            if (item == null) continue;

            WAND_RODS.computeIfAbsent(item, ignore -> new HashMap<>())
                .put(itemStack.getItemDamage(), rod);
        }
    }

    /**
     * Try to find the registered {@link WandCap} by its item form.
     *
     * @param itemStack The item form of the {@link WandCap} to look up.
     * @return The registered {@link WandCap} if one was found, or {@code null} otherwise.
     */
    public static @Nullable WandCap getWandCapFromItem(@Nullable ItemStack itemStack) {
        if (itemStack == null) return null;

        final var item = itemStack.getItem();
        if (item == null) return null;

        final var subMap = WAND_CAPS.get(item);
        return subMap != null ? subMap.get(itemStack.getItemDamage()) : null;
    }

    /**
     * Try to find the {@link WandCap} component of a wand.
     *
     * @param itemStack A wand whose caps should be found.
     * @return If {@code itemStack} is a wand, the caps that make up that wand, or {@code null} otherwise.
     */
    public static @Nullable WandCap getWandCapFromWand(@Nullable ItemStack itemStack) {
        final var wandInstance = getWandItem(itemStack);
        if (wandInstance == null) {
            return null;
        }
        return wandInstance.getCap(itemStack);

    }

    /**
     * Try to find the registered {@link WandRod} by its item form.
     *
     * @param itemStack The item form of the {@link WandRod} to look up.
     * @return The registered {@link WandRod} if one was found, or {@code null} otherwise.
     */
    public static @Nullable WandRod getWandRodFromItem(@Nullable ItemStack itemStack) {
        if (itemStack == null) return null;

        final var item = itemStack.getItem();
        if (item == null) return null;

        final var subMap = WAND_RODS.get(item);
        return subMap != null ? subMap.get(itemStack.getItemDamage()) : null;
    }

    /**
     * Try to find the {@link WandRod} component of a wand.
     *
     * @param itemStack A wand whose rod should be found.
     * @return If {@code itemStack} is a wand, the rod that makes up that wand, or {@code null} otherwise.
     */
    public static @Nullable WandRod getWandRodFromWand(@Nullable ItemStack itemStack) {
        final var wandInstance = getWandItem(itemStack);
        if (wandInstance == null) {
            return null;
        }
        return wandInstance.getRod(itemStack);
    }

    public static @Nullable ItemWandCasting getWandItem(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        final var item = itemStack.getItem();
        if (item == null) {
            return null;
        }
        if (!(item instanceof ItemWandCasting wandInstance)) {
            return null;
        }
        return wandInstance;
    }

    private static ArrayList<WandCap> allVanillaCaps;

    public static List<WandCap> allVanillaCaps() {
        if (allVanillaCaps == null) {
            final var allCaps = new WandCap[] { ConfigItems.WAND_CAP_IRON, ConfigItems.WAND_CAP_COPPER,
                ConfigItems.WAND_CAP_GOLD, ConfigItems.WAND_CAP_SILVER, ConfigItems.WAND_CAP_THAUMIUM,
                ConfigItems.WAND_CAP_VOID, };
            allVanillaCaps = new ArrayList<>(allCaps.length);
            for (var wandCap : allCaps) {
                if (wandCap == null) {
                    continue;
                }
                allVanillaCaps.add(wandCap);
            }
        }
        return new ArrayList<>(allVanillaCaps);
    }

    private static ArrayList<WandRod> allVanillaRods;

    public static List<WandRod> allVanillaRods() {
        if (allVanillaRods == null) {
            final var allRods = new WandRod[] { ConfigItems.WAND_ROD_WOOD, ConfigItems.WAND_ROD_GREATWOOD,
                ConfigItems.WAND_ROD_OBSIDIAN, ConfigItems.WAND_ROD_BLAZE, ConfigItems.WAND_ROD_ICE,
                ConfigItems.WAND_ROD_QUARTZ, ConfigItems.WAND_ROD_BONE, ConfigItems.WAND_ROD_REED,
                ConfigItems.WAND_ROD_SILVERWOOD, ConfigItems.STAFF_ROD_GREATWOOD, ConfigItems.STAFF_ROD_OBSIDIAN,
                ConfigItems.STAFF_ROD_BLAZE, ConfigItems.STAFF_ROD_ICE, ConfigItems.STAFF_ROD_QUARTZ,
                ConfigItems.STAFF_ROD_BONE, ConfigItems.STAFF_ROD_REED, ConfigItems.STAFF_ROD_SILVERWOOD,
                ConfigItems.STAFF_ROD_PRIMAL, };
            allVanillaRods = new ArrayList<>(allRods.length);
            for (var wandRod : allRods) {
                if (wandRod == null) {
                    continue;
                }
                allVanillaRods.add(wandRod);
            }
        }
        return new ArrayList<>(allVanillaRods);
    }

    // Unknown Wand Components
    public static final WandCap CAP_UNKNOWN = new WandCap("salisarcana:unknown", 1f, new ItemStack(Blocks.bedrock), 0);
    public static final WandRod ROD_UNKNOWN = new WandRod("salisarcana:unknown", 0, new ItemStack(Blocks.bedrock), 0);
    public static final StaffRod STAFF_UNKNOWN = new StaffRod(
        "salisarcana:unknown",
        0,
        new ItemStack(Blocks.bedrock),
        0);

    static {
        CAP_UNKNOWN.setTexture(new ResourceLocation(SalisArcana.MODID, "textures/models/unknown_cap.png"));
        ROD_UNKNOWN.setTexture(new ResourceLocation(SalisArcana.MODID, "textures/models/unknown_rod.png"));
        STAFF_UNKNOWN.setTexture(new ResourceLocation(SalisArcana.MODID, "textures/models/unknown_rod.png"));
    }
}
