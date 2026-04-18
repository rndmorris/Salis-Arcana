package dev.rndmorris.salisarcana.lib;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import com.gtnewhorizons.tcwands.api.wrappers.AbstractWandWrapper;
import com.gtnewhorizons.tcwands.api.wrappers.CapWrapper;

import dev.rndmorris.salisarcana.common.compat.GTNHTCWandsCompat;
import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.config.ConfigItems;

public enum WandType {

    UNKNOWN,
    WAND,
    SCEPTER,
    STAFF,
    STAFFTER;

    public static @Nonnull WandType getWandType(@Nullable ItemStack itemStack) {
        final var wandInstance = WandHelper.getWandItem(itemStack);
        if (wandInstance == null) {
            return UNKNOWN;
        }
        if (wandInstance.isSceptre(itemStack) && wandInstance.isStaff(itemStack)) {
            return STAFFTER;
        }
        if (wandInstance.isSceptre(itemStack)) {
            return SCEPTER;
        }
        if (wandInstance.isStaff(itemStack)) {
            return STAFF;
        }
        return WAND;
    }

    public int getCraftingVisCost(WandCap forCap, WandRod forRod) {
        if (forCap == null || forRod == null) {
            return -1;
        }

        if (SalisConfig.modCompat.gtnhWands.cost.isEnabled()) {
            AbstractWandWrapper wandWrapper = GTNHTCWandsCompat.getWandWrapper(forRod, this);
            CapWrapper capWrapper = GTNHTCWandsCompat.getCapWrapper(forCap);
            if (wandWrapper == null || capWrapper == null) return -1;

            return wandWrapper.getRecipeCost(capWrapper);
        }

        return switch (this) {
            case WAND, STAFF -> {
                if (forCap == ConfigItems.WAND_CAP_IRON && forRod == ConfigItems.WAND_ROD_WOOD) {
                    yield 0;
                }
                yield forCap.getCraftCost() * forRod.getCraftCost();
            }
            case SCEPTER, STAFFTER -> forCap.getCraftCost() * forRod.getCraftCost() * 3 / 2; // * 1.5
            default -> -1;
        };

    }

    public int getRequiredCaps() {
        return switch (this) {
            case WAND, STAFF -> 2;
            case SCEPTER, STAFFTER -> 3;
            default -> -1;
        };
    }

    public <R extends WandRod> boolean isCoreSuitable(@Nullable R coreType) {
        return switch (this) {
            case WAND, SCEPTER -> coreType instanceof WandRod && !(coreType instanceof StaffRod);
            case STAFF, STAFFTER -> coreType instanceof StaffRod;
            default -> false;
        };
    }
}
