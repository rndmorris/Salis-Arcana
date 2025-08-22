package dev.rndmorris.salisarcana.api;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

public final class NbtUtilities {

    private NbtUtilities() {}

    public static final String TAG_SALIS_ARCANA = "salisarcana";
    public static final String TAG_SUPPRESS_ASPECTS = "suppressAspects";

    /**
     * Add the NBT tag that suppresses the item's aspects, if it does not already have it.
     * 
     * @param stack The stack whose aspects should be suppressed.
     * @return The input {@link ItemStack}.
     */
    public static @Nonnull ItemStack setSuppressAspectsTag(@Nonnull ItemStack stack) {
        final var salisTag = getOrCreateSalisArcanaTag(stack);
        salisTag.setBoolean(TAG_SUPPRESS_ASPECTS, true);
        return stack;
    }

    /**
     * Remove the NBT tag that supresses the item's aspects, if it has it.
     * 
     * @param stack The stack whose aspects should no longer be suppressed.
     * @return The input {@link ItemStack}.
     */
    public static @Nullable ItemStack removeSupressAspectsTag(@Nullable ItemStack stack) {
        final var salisTag = getSalisArcanaTag(stack);
        if (salisTag == null) {
            return stack;
        }
        salisTag.removeTag(TAG_SUPPRESS_ASPECTS);
        removeEmptyTags(stack);
        return stack;
    }

    /**
     * Check if the given {@link ItemStack} has the tag to suppresses its aspects.
     * 
     * @param stack The stack whose tags should be queried.
     * @return <c>true</c> if <c>stack</c> both is not null and has the tag to suppress the item's aspects, <c>false</c>
     *         otherwise.
     */
    public static boolean hasSuppressAspectsTag(@Nullable ItemStack stack) {
        final var salisTag = getSalisArcanaTag(stack);
        if (salisTag == null) {
            return false;
        }
        return salisTag.hasKey(TAG_SUPPRESS_ASPECTS, Constants.NBT.TAG_BYTE);
    }

    /**
     * Try to get the Salis Arcana tag from the given {@link ItemStack}.
     *
     * @param itemStack The {@link ItemStack} for which the Salis Arcana tag should be retrieved.
     * @return The Salis Arcana {@link NBTTagCompound} if <c>itemStack</c> is not null and the tag was found, or
     *         <c>null</c> otherwise.
     */
    public static @Nullable NBTTagCompound getSalisArcanaTag(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        if (!itemStack.hasTagCompound()) {
            return null;
        }
        final var itemTag = itemStack.getTagCompound();
        if (!itemTag.hasKey(TAG_SALIS_ARCANA, Constants.NBT.TAG_COMPOUND)) {
            return null;
        }
        return itemTag.getCompoundTag(TAG_SALIS_ARCANA);
    }

    /**
     * Get the Salis Arcana tag for the given {@link ItemStack}, or create and attach it to the item if one is not
     * found.
     *
     * @param itemStack The {@link ItemStack} for which the Salis Arcana tag should be retrieved or created.
     * @return <c>null</c> if <c>itemStack</c> is null, or the existing or newly-created <c>salisarcana</c> tag from the
     *         item.
     */
    public static @Nonnull NBTTagCompound getOrCreateSalisArcanaTag(@Nonnull ItemStack itemStack) {
        Objects.requireNonNull(itemStack);

        final NBTTagCompound itemTag;
        if (itemStack.hasTagCompound()) {
            itemTag = itemStack.getTagCompound();
        } else {
            itemStack.setTagCompound(itemTag = new NBTTagCompound());
        }

        final NBTTagCompound salisTag;
        if (itemTag.hasKey(TAG_SALIS_ARCANA, Constants.NBT.TAG_COMPOUND)) {
            salisTag = itemTag.getCompoundTag(TAG_SALIS_ARCANA);
        } else {
            itemTag.setTag(TAG_SALIS_ARCANA, salisTag = new NBTTagCompound());
        }
        return salisTag;
    }

    /**
     * Remove the Salis Arcana tag if it exists but is empty, then remove the item's root tag if it exists but is empty.
     * 
     * @param itemStack The stack whose tags should be cleaned.
     * @return The input {@link ItemStack}.
     */
    public static @Nullable ItemStack removeEmptyTags(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        if (!itemStack.hasTagCompound()) {
            return itemStack;
        }
        final var itemTag = itemStack.getTagCompound();
        if (itemTag.hasKey(TAG_SALIS_ARCANA, Constants.NBT.TAG_COMPOUND)) {
            final var salisTag = itemTag.getCompoundTag(TAG_SALIS_ARCANA);
            if (salisTag.hasNoTags()) {
                itemTag.removeTag(TAG_SALIS_ARCANA);
            }
        }
        if (itemTag.hasNoTags()) {
            itemStack.setTagCompound(null);
        }
        return itemStack;
    }
}
