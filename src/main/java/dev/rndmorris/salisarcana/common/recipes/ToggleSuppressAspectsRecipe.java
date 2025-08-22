package dev.rndmorris.salisarcana.common.recipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import dev.rndmorris.salisarcana.api.NbtUtilities;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.common.items.ItemCrystalEssence;

public class ToggleSuppressAspectsRecipe implements IArcaneRecipe {

    private final static int[] SLOT_CRYSTALS = new int[] { 1, 3, 5, 7 };
    private final static int[] SLOT_EMPTY = new int[] { 0, 2, 6, 8 };
    private final static int SLOT_ITEM = 4;

    private static boolean isCorrectCrystal(ItemStack stack, Aspect aspect) {
        if (stack == null) {
            return false;
        }
        if (!(stack.getItem() instanceof ItemCrystalEssence crystal)) {
            return false;
        }
        return crystal.getAspects(stack)
            .getAmount(aspect) > 0;
    }

    @Override
    public boolean matches(IInventory inventory, World world, EntityPlayer player) {
        for (var slot : SLOT_EMPTY) {
            if (inventory.getStackInSlot(slot) != null) {
                return false;
            }
        }

        final var centerItem = inventory.getStackInSlot(SLOT_ITEM);
        if (centerItem == null) {
            return false;
        }

        final var action = NbtUtilities.hasSuppressAspectsTag(centerItem) ? Action.REMOVE_TAG : Action.ADD_TAG;
        for (var slot : SLOT_CRYSTALS) {
            if (!isCorrectCrystal(inventory.getStackInSlot(slot), action.aspect)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inventory) {
        final var source = inventory.getStackInSlot(SLOT_ITEM);
        final var output = source.splitStack(1);
        if (NbtUtilities.hasSuppressAspectsTag(source)) {
            return NbtUtilities.removeSupressAspectsTag(output);
        }
        return NbtUtilities.setSuppressAspectsTag(output);
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
        return null;
    }

    @Override
    public AspectList getAspects(IInventory inventory) {
        if (NbtUtilities.hasSuppressAspectsTag(inventory.getStackInSlot(SLOT_ITEM))) {
            return new AspectList().add(Aspect.ENTROPY, 1)
                .add(Aspect.FIRE, 1);
        }
        return new AspectList().add(Aspect.ORDER, 30)
            .add(Aspect.EARTH, 30);
    }

    @Override
    public String getResearch() {
        return "PHIAL"; // todo: replace this
    }

    private enum Action {

        ADD_TAG(Aspect.ARMOR),
        REMOVE_TAG(Aspect.WEAPON);

        public final Aspect aspect;

        Action(Aspect aspect) {
            this.aspect = aspect;
        }
    }
}
