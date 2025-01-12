package dev.rndmorris.salisarcana.lib;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.items.wands.ItemWandCasting;

public class WandHelper {

    /**
     * Try to find the registered {@link WandCap} by its item form.
     *
     * @param itemStack The item form of the {@link WandCap} to look up.
     * @return The registered {@link WandCap} if one was found, or {@code null} otherwise.
     */
    public static @Nullable WandCap getWandCapFromItem(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        final var item = itemStack.getItem();
        if (item == null) {
            return null;
        }
        for (var cap : WandCap.caps.values()) {
            final var capItemStack = cap.getItem();
            if (capItemStack == null) {
                continue;
            }
            if (itemStack.isItemEqual(capItemStack)) {
                return cap;
            }
        }

        return null;
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
        if (itemStack == null) {
            return null;
        }
        final var item = itemStack.getItem();
        if (item == null) {
            return null;
        }
        for (var rod : WandRod.rods.values()) {
            final var rodItemStack = rod.getItem();
            if (rodItemStack == null) {
                continue;
            }
            if (itemStack.isItemEqual(rodItemStack)) {
                return rod;
            }
        }

        return null;
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

    public static boolean isScepter(@Nullable ItemStack itemStack) {
        final var wandInstance = getWandItem(itemStack);
        return wandInstance != null && wandInstance.isSceptre(itemStack);
    }

    public static boolean isStaff(@Nullable ItemStack itemStack) {
        final var wandInstance = getWandItem(itemStack);
        return wandInstance != null && wandInstance.isStaff(itemStack);
    }

    public static boolean isWand(@Nullable ItemStack itemStack) {
        final var wandInstance = getWandItem(itemStack);
        return wandInstance != null && !wandInstance.isSceptre(itemStack) && !wandInstance.isStaff(itemStack);
    }

    public static AspectList primalAspectList(int amount) {
        return primalAspectList(amount, amount, amount, amount, amount, amount);
    }

    public static AspectList primalAspectList(int airAmount, int earthAmount, int fireAmount, int waterAmount,
        int orderAmount, int entropyAmount) {
        return new AspectList().add(Aspect.AIR, airAmount)
            .add(Aspect.EARTH, earthAmount)
            .add(Aspect.FIRE, fireAmount)
            .add(Aspect.WATER, waterAmount)
            .add(Aspect.ORDER, orderAmount)
            .add(Aspect.ENTROPY, entropyAmount);
    }
}
