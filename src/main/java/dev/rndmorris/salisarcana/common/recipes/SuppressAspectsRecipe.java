package dev.rndmorris.salisarcana.common.recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import dev.rndmorris.salisarcana.api.NbtUtilities;
import dev.rndmorris.salisarcana.common.CustomResearch;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemCrystalEssence;
import thaumcraft.common.items.wands.ItemWandCasting;

public abstract class SuppressAspectsRecipe implements IArcaneRecipe {

    private final static int[] SLOT_CRYSTALS = new int[] { 1, 3, 5, 7 };
    private final static int[] SLOT_SECONDARY = new int[] { 0, 2, 6, 8 };
    private final static int SLOT_ITEM = 4;

    private final @Nonnull Aspect crystalAspect;
    private final @Nullable ItemStack secondaryItem;
    private final @Nonnull AspectList craftingCost;

    protected SuppressAspectsRecipe(@Nonnull Aspect crystalAspect, @Nullable ItemStack secondaryItem,
        @Nonnull AspectList craftingCost) {
        this.crystalAspect = crystalAspect;
        this.secondaryItem = secondaryItem;
        this.craftingCost = craftingCost;
    }

    private boolean isCorrectCrystal(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (!(stack.getItem() instanceof ItemCrystalEssence crystal)) {
            return false;
        }
        return crystal.getAspects(stack)
            .getAmount(crystalAspect) > 0;
    }

    @Override
    public boolean matches(IInventory inventory, World world, EntityPlayer player) {
        final var centerItem = inventory.getStackInSlot(SLOT_ITEM);
        if (centerItem == null) {
            return false;
        }

        for (var slot : SLOT_CRYSTALS) {
            if (!isCorrectCrystal(inventory.getStackInSlot(slot))) {
                return false;
            }
        }

        for (var slot : SLOT_SECONDARY) {
            final var secondaryItem = inventory.getStackInSlot(slot);
            if (this.secondaryItem == null) {
                if (secondaryItem != null) {
                    return false;
                }
            } else {
                if (!this.secondaryItem.isItemEqual(secondaryItem)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inventory) {
        final var source = inventory.getStackInSlot(SLOT_ITEM);
        final var output = source.splitStack(1);
        return updateItemTag(output);
    }

    @Override
    public int getRecipeSize() {
        return 9;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public AspectList getAspects() {
        return craftingCost;
    }

    @Override
    public AspectList getAspects(IInventory inventory) {
        return craftingCost;
    }

    @Override
    public String getResearch() {
        return CustomResearch.alchemicalSealant.key;
    }

    protected abstract ItemStack updateItemTag(ItemStack item);

    protected List<ItemStack> exampleItems() {
        final var itemWandCasting = (ItemWandCasting) ConfigItems.itemWandCasting;
        final var wandItem = new ItemStack(itemWandCasting);
        itemWandCasting.setCap(wandItem, ConfigItems.WAND_CAP_THAUMIUM);
        itemWandCasting.setRod(wandItem, ConfigItems.WAND_ROD_SILVERWOOD);
        wandItem.getTagCompound()
            .setBoolean("sceptre", true);
        final var results = new ArrayList<ItemStack>();
        Collections.addAll(
            results,
            new ItemStack(Items.diamond),
            new ItemStack(Blocks.red_flower, 1, 2),
            new ItemStack(Items.wooden_pickaxe),
            new ItemStack(ConfigItems.itemResource, 1, 15),
            new ItemStack(ConfigItems.itemFocusPouch),
            new ItemStack(ConfigItems.itemHoeVoid),
            wandItem);
        return results;
    }

    public IArcaneRecipe[] getExampleRecipes() {
        final var research = getResearch();
        final var aspects = getAspects();
        final var crystalItem = new ItemStack(ConfigItems.itemCrystalEssence);
        ((ItemCrystalEssence) ConfigItems.itemCrystalEssence)
            .setAspects(crystalItem, new AspectList().add(crystalAspect, 1));

        final var edge = secondaryItem == null ? " C " : "SCS";
        final var center = "CIC";

        final Object[] recipe = new Object[secondaryItem == null ? 7 : 9];
        recipe[0] = edge;
        recipe[1] = center;
        recipe[2] = edge;
        recipe[3] = 'C';
        recipe[4] = crystalItem;
        recipe[5] = 'I';
        // index 6 is the output
        if (secondaryItem != null) {
            recipe[7] = 'S';
            recipe[8] = secondaryItem;
        }

        final var exampleItems = exampleItems();
        final var results = new IArcaneRecipe[exampleItems.size()];
        for (var index = 0; index < results.length; ++index) {
            final var centerItem = exampleItems.get(index);
            final var resultItem = updateItemTag(centerItem.splitStack(1));
            recipe[6] = centerItem;
            results[index] = new ShapedArcaneRecipe(research, resultItem, aspects, recipe);
        }
        return results;
    }

    public static final class AddTagRecipe extends SuppressAspectsRecipe {

        public AddTagRecipe() {
            super(
                Aspect.ARMOR,
                new ItemStack(ConfigItems.itemResource, 1, 4),
                new AspectList().add(Aspect.ORDER, 30)
                    .add(Aspect.EARTH, 30));
        }

        @Override
        protected ItemStack updateItemTag(ItemStack item) {
            return NbtUtilities.setSuppressAspectsTag(item);
        }
    }

    public static final class RemoveTagRecipe extends SuppressAspectsRecipe {

        public RemoveTagRecipe() {
            super(
                Aspect.WEAPON,
                null,
                new AspectList().add(Aspect.ENTROPY, 1)
                    .add(Aspect.FIRE, 1));
        }

        @Override
        protected ItemStack updateItemTag(ItemStack item) {
            return NbtUtilities.removeSupressAspectsTag(item);
        }

        @Override
        protected List<ItemStack> exampleItems() {
            final var exampleItems = super.exampleItems();
            for (var item : exampleItems) {
                NbtUtilities.setSuppressAspectsTag(item);
            }
            return exampleItems;
        }
    }
}
