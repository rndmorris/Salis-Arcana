package dev.rndmorris.salisarcana.common.recipes;

import java.util.ArrayList;
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

    private static boolean hasCloth(ItemStack i) {
        final var clothDamage = 7;
        return i != null && i.getItem() == ConfigItems.itemResource && i.getItemDamage() == clothDamage;
    }

    private static boolean isSalt(ItemStack i) {
        final var saltDamage = 14;
        return i != null && i.getItem() == ConfigItems.itemResource && i.getItemDamage() == saltDamage;
    }

    private final int strength;

    private int focusIndex;

    private final PredicateList predicates;

    public CleanFocusRecipe(int strength) {
        this.strength = Integer.min(Integer.max(strength, 1), 5);
        final var balancedShardDamage = 6;

        predicates = new PredicateList(2 + strength) {

            {
                focusIndex = this.autoCount;
                add(CleanFocusRecipe.this::hasFocus);
                add(CleanFocusRecipe::hasCloth);

                for (var index = 0; index < strength; ++index) {
                    add(CleanFocusRecipe::isSalt);
                }
            }
        };
    }

    private boolean hasFocus(ItemStack i) {
        if (i == null || !(i.getItem() instanceof ItemFocusBasic itemFocusBasic)) {
            return false;
        }
        var upgradeCount = 0;
        for (var upgrade : itemFocusBasic.getAppliedUpgrades(i)) {
            if (upgrade >= 0) {
                upgradeCount += 1;
            }
        }
        return strength <= upgradeCount;
    }

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
        final var upgrades = itemFocus.getAppliedUpgrades(outputStack);
        var removed = 0;
        for (var index = upgrades.length - 1; index >= 0 && removed < strength; --index) {
            if (upgrades[index] >= 0) {
                removed += 1;
                upgrades[index] = -1;
            }
        }

        // private method, so reflect to call it
        CustomRecipes
            .withItemFocusReflection(itemFocus, (r) -> r.call("setFocusUpgradeTagList", outputStack, upgrades));

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
        return AspectHelper.primalList(strength * 10);
    }

    @Override
    public String getResearch() {
        return ConfigModuleRoot.enhancements.focusDowngradeRecipe.researchName;
    }
}

class PredicateList extends ArrayList<SlotPredicate> {

    public PredicateList(int initialCapacity) {
        super(initialCapacity);
    }

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
