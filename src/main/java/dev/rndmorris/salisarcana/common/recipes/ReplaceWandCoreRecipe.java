package dev.rndmorris.salisarcana.common.recipes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.github.bsideup.jabel.Desugar;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.lib.AspectHelper;
import dev.rndmorris.salisarcana.lib.WandHelper;
import dev.rndmorris.salisarcana.lib.WandType;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.items.wands.ItemWandCasting;

public class ReplaceWandCoreRecipe implements IArcaneRecipe {

    @Override
    public boolean matches(IInventory tableInv, World world, EntityPlayer player) {
        final var scanResult = scanTable(tableInv);
        if (scanResult == null || scanResult.invalidInputs()) {
            return false;
        }

        return ThaumcraftApiHelper.isResearchComplete(
            player.getCommandSenderName(),
            scanResult.newRod()
                .getResearch());
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
        wandInstance.setRod(outputItem, scanResult.newRod());
        wandInstance.storeAllVis(outputItem, AspectHelper.primalList(0));

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
            .getCraftingVisCost(scanResult.wandCaps(), scanResult.newRod());
        if (cost < 0) {
            return empty;
        }

        return AspectHelper.primalList(cost);
    }

    @Override
    public String getResearch() {
        return "";
    }

    private @Nullable InvScanResult scanTable(IInventory tableInv) {
        ItemStack wandItem = null;
        WandRod newRod = null;

        for (var slot = 0; slot < tableInv.getSizeInventory() && slot < 9; ++slot) {
            final var slotItem = tableInv.getStackInSlot(slot);

            final var maybeRod = WandHelper.getWandRodFromItem(slotItem);
            if (maybeRod != null) {
                if (newRod != null) {
                    return null;
                }
                newRod = maybeRod;
                continue;
            }

            final var maybeWand = WandHelper.getWandItem(slotItem);
            if (maybeWand != null) {
                if (wandItem != null) {
                    return null;
                }
                wandItem = slotItem;
                continue;
            }

            if (slotItem != null) {
                return null;
            }
        }

        return new InvScanResult(wandItem, newRod);
    }

    @Desugar
    private record InvScanResult(ItemStack wandItem, WandRod newRod) {

        public boolean invalidInputs() {
            if (wandItem == null) {
                return true;
            }

            if (ConfigModuleRoot.enhancements.enforceWandCoreTypes.isEnabled() && !wandType().isCoreSuitable(newRod)) {
                return true;
            }

            final var oldRod = oldRod();
            if (newRod == null || oldRod == null) {
                return true;
            }
            return newRod == oldRod;
        }

        public @Nullable WandCap wandCaps() {
            return WandHelper.getWandCapFromWand(wandItem);
        }

        public @Nullable ItemWandCasting wandInstance() {
            return WandHelper.getWandItem(wandItem);
        }

        public @Nullable WandRod oldRod() {
            return WandHelper.getWandRodFromWand(wandItem);
        }

        public @Nonnull WandType wandType() {
            return WandType.getWandType(wandItem);
        }
    }
}
