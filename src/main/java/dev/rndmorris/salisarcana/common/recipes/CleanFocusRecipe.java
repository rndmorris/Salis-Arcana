package dev.rndmorris.salisarcana.common.recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.lib.ArrayHelper;
import dev.rndmorris.salisarcana.lib.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.config.ConfigItems;

public class CleanFocusRecipe implements IArcaneRecipe {

    private int focusIndex;

    private final List<SlotPredicate> predicates = Collections.unmodifiableList(new PredicateList() {

        {
            final var balancedShardDamage = 6;
            final var clothDamage = 7;
            focusIndex = this.autoCount;
            add(i -> i != null && i.getItem() instanceof ItemFocusBasic);
            add(i -> i != null && i.getItem() == ConfigItems.itemShard && i.getItemDamage() == balancedShardDamage);
            add(i -> i != null && i.getItem() == ConfigItems.itemResource && i.getItemDamage() == clothDamage);
        }
    });

    private ItemStack[] getMatchedItems(IInventory inventory) {
        final var outputs = new ItemStack[predicates.size()];

        final var predicates = new ArrayList<>(this.predicates);
        for (var slot = 0; slot < inventory.getSizeInventory() - 2 /* exclude the output and wand slots */ ; slot++) {
            int matchedPredicateIndex = -1;
            final var slotItem = inventory.getStackInSlot(slot);
            for (var predIndex = 0; predIndex < predicates.size(); ++predIndex) {
                final var predicate = predicates.get(predIndex);
                if (!predicate.predicate.test(slotItem)) {
                    continue;
                }
                outputs[predicate.index] = slotItem;
                matchedPredicateIndex = predIndex;
                break;
            }
            if (matchedPredicateIndex > -1) {
                predicates.remove(matchedPredicateIndex);
            } else if (slotItem != null) {
                return null;
            }
        }

        if (!predicates.isEmpty()) {
            return null;
        }

        return outputs;
    }

    @Override
    public boolean matches(IInventory inventory, World world, EntityPlayer player) {
        final var invItems = getMatchedItems(inventory);

        final var focusStack = ArrayHelper.tryGet(invItems, focusIndex)
            .data();
        // extra sanity check, also to get the ItemFocusBasic instance
        if (focusStack == null || !(focusStack.getItem() instanceof ItemFocusBasic focusItem)) {
            return false;
        }
        final var appliedUpgrades = focusItem.getAppliedUpgrades(focusStack);
        ArrayHelper.TryGetShortResult getResult;
        // we want to have at least one focus upgrade on the focus
        if ((getResult = ArrayHelper.tryGet(appliedUpgrades, 0)).success() && getResult.data() < 0) {
            return false;
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inventory) {
        final var invItems = getMatchedItems(inventory);
        final var focusStack = ArrayHelper.tryGet(invItems, focusIndex)
            .data();

        if (focusStack == null || !(focusStack.getItem() instanceof ItemFocusBasic itemFocus)) {
            return null;
        }

        final var outputStack = focusStack.copy();

        // private method, so reflect to call it
        CustomRecipes.withItemFocusReflection(
            itemFocus,
            (r) -> r.call("setFocusUpgradeTagList", outputStack, new short[] { -1, -1, -1, -1, -1 }));

        return outputStack;
    }

    @Override
    public int getRecipeSize() {
        return 3;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public AspectList getAspects() {
        return null;
    }

    @Override
    public AspectList getAspects(IInventory inventory) {
        final var invItems = getMatchedItems(inventory);
        final var focusStack = ArrayHelper.tryGet(invItems, focusIndex)
            .data();
        if (focusStack == null || !(focusStack.getItem() instanceof ItemFocusBasic itemFocus)) {
            return new AspectList();
        }
        final var focusUpgrades = itemFocus.getAppliedUpgrades(focusStack);
        var costMultiplier = 0;

        for (var upgrade : focusUpgrades) {
            if (upgrade > 0) {
                costMultiplier += 1;
            }
        }

        return AspectHelper.primalList(costMultiplier * 10);
    }

    @Override
    public String getResearch() {
        return ConfigModuleRoot.enhancements.focusDowngradeRecipe.researchName;
    }
}

class PredicateList extends ArrayList<SlotPredicate> {

    public int autoCount = 0;

    /**
     * Automatically register a predicate with a sequential output index.
     */
    public boolean add(Predicate<ItemStack> predicate) {
        return this.add(new SlotPredicate(autoCount++, predicate));
    }

}

class SlotPredicate {

    public int index;
    public Predicate<ItemStack> predicate;

    public SlotPredicate(int index, Predicate<ItemStack> predicate) {
        this.index = index;
        this.predicate = predicate;
    }
}
