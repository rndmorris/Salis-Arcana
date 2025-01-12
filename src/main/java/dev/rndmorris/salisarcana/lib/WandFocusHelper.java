package dev.rndmorris.salisarcana.lib;

import static dev.rndmorris.salisarcana.lib.ArrayHelper.tryGet;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;

public class WandFocusHelper {

    public static List<FocusUpgradeType> getAppliedUpgrades(ItemFocusBasic focus, ItemStack itemStack) {
        final var applied = new ArrayList<FocusUpgradeType>(5);
        for (var upgradeId : focus.getAppliedUpgrades(itemStack)) {
            if (upgradeId < 0) {
                break;
            }
            final var tryGetResult = tryGet(FocusUpgradeType.types, upgradeId);
            if (!tryGetResult.success()) {
                break;
            }
            applied.add(tryGetResult.data());
        }
        return applied;
    }

    public static @Nullable ItemFocusBasic getFocusFrom(@Nullable ItemStack heldItem) {
        if (heldItem != null && heldItem.getItem() instanceof ItemFocusBasic focus) {
            return focus;
        }
        return null;
    }

}
