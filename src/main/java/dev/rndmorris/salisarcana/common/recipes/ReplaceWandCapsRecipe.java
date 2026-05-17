package dev.rndmorris.salisarcana.common.recipes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.github.bsideup.jabel.Desugar;

import dev.rndmorris.salisarcana.api.IMultipleResearchArcaneRecipe;
import dev.rndmorris.salisarcana.common.CustomResearch;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.AspectHelper;
import dev.rndmorris.salisarcana.lib.WandHelper;
import dev.rndmorris.salisarcana.lib.WandType;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.items.wands.ItemWandCasting;

public class ReplaceWandCapsRecipe implements IArcaneRecipe, IMultipleResearchArcaneRecipe {

    @Override
    public boolean matches(IInventory tableInv, World world, EntityPlayer player) {
        if (!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), getResearch())) {
            return false;
        }

        final var scanResult = scanTable(tableInv);
        if (scanResult == null || scanResult.invalidInputs()) {
            return false;
        }

        return ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), scanResult.newCaps.getResearch());
    }

    @Override
    public ItemStack getCraftingResult(IInventory tableInv) {
        final var scanResult = scanTable(tableInv);
        if (scanResult == null || scanResult.invalidInputs()) {
            return null;
        }
        final var wandInstance = scanResult.wandInstance();
        if (wandInstance == null) {
            return null;
        }

        final var outputItem = scanResult.wandItem()
            .copy();
        wandInstance.setCap(outputItem, scanResult.newCaps());

        if (!SalisConfig.features.preserveWandVis.isEnabled()) {
            wandInstance.storeAllVis(outputItem, AspectHelper.primalList(0));
        }

        final int cost = scanResult.wandType()
            .getCraftingVisCost(scanResult.newCaps(), scanResult.wandRod());
        outputItem.setItemDamage(Math.max(cost, 0));

        return outputItem;
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
    public AspectList getAspects(IInventory tableInv) {
        final var empty = new AspectList();
        final var scanResult = scanTable(tableInv);
        if (scanResult == null || scanResult.invalidInputs()) {
            return null;
        }
        final var wandInstance = scanResult.wandInstance();
        if (wandInstance == null) {
            return null;
        }

        final var cost = scanResult.wandType()
            .getCraftingVisCost(scanResult.newCaps(), scanResult.wandRod());
        if (cost < 0) {
            return empty;
        }

        return AspectHelper.primalList(cost);
    }

    @Override
    public String getResearch() {
        return CustomResearch.replaceCapsResearch.key;
    }

    private @Nullable InvScanResult scanTable(IInventory tableInv) {
        ItemStack wandItem = null;
        WandCap newCaps = null;
        var newCapsFound = 0;

        for (var slot = 0; slot < tableInv.getSizeInventory() && slot < 9; ++slot) {
            final var slotItem = tableInv.getStackInSlot(slot);

            final var maybeCap = WandHelper.getWandCapFromItem(slotItem);
            if (maybeCap != null) {
                if (newCaps == null) {
                    newCaps = maybeCap;
                }
                if (newCaps != maybeCap) {
                    // cap mismatch
                    return null;
                }
                newCapsFound += 1;
                continue;
            }

            final var maybeWand = WandHelper.getWandItem(slotItem);
            if (maybeWand != null) {
                if (wandItem != null) {
                    // we've already encountered a wand item
                    return null;
                }
                wandItem = slotItem;
                continue;
            }

            if (slotItem != null) {
                // if it's not a wand cap or a wand, it shouldn't be here
                return null;
            }
        }

        return new InvScanResult(wandItem, newCaps, newCapsFound);
    }

    @Override
    public String[] salisArcana$getResearches(IInventory inv, World world, EntityPlayer player) {
        final var scanResult = scanTable(inv);

        if (scanResult == null || scanResult.invalidInputs()) {
            return new String[] { getResearch() };
        }

        return new String[] { getResearch(), scanResult.newCaps.getResearch() };
    }

    @Desugar
    private record InvScanResult(ItemStack wandItem, WandCap newCaps, int newCapsFound) {

        public boolean invalidInputs() {
            if (wandItem == null) {
                return true;
            }
            if (wandType().getRequiredCaps() != newCapsFound) {
                return true;
            }
            final var oldCaps = oldCaps();
            if (newCaps == null || oldCaps == null) {
                return true;
            }
            return newCaps == oldCaps;
        }

        public @Nullable WandCap oldCaps() {
            return WandHelper.getWandCapFromWand(wandItem);
        }

        public @Nullable ItemWandCasting wandInstance() {
            return WandHelper.getWandItem(wandItem);
        }

        public @Nullable WandRod wandRod() {
            return WandHelper.getWandRodFromWand(wandItem);
        }

        public @Nonnull WandType wandType() {
            return WandType.getWandType(wandItem);
        }

    }
}
