package dev.rndmorris.salisarcana.common.recipes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.github.bsideup.jabel.Desugar;
import com.gtnewhorizons.tcwands.api.wandinfo.WandDetails;
import com.gtnewhorizons.tcwands.api.wrappers.AbstractWandWrapper;

import dev.rndmorris.salisarcana.api.IMultipleResearchArcaneRecipe;
import dev.rndmorris.salisarcana.common.CustomResearch;
import dev.rndmorris.salisarcana.common.compat.GTNHTCWandsCompat;
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

public class ReplaceWandCoreRecipe implements IArcaneRecipe, IMultipleResearchArcaneRecipe {

    @Override
    public boolean matches(IInventory tableInv, World world, EntityPlayer player) {
        if (!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), getResearch())) {
            return false;
        }
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

        if (SalisConfig.features.preserveWandVis.isEnabled()) {
            final var maxVis = wandInstance.getMaxVis(outputItem);
            final var newVis = new AspectList();
            final var originalVis = wandInstance.getAllVis(outputItem);
            for (var entry : originalVis.aspects.entrySet()) {
                newVis.add(entry.getKey(), Integer.min(maxVis, entry.getValue()));
            }
            wandInstance.storeAllVis(outputItem, newVis);
        } else {
            wandInstance.storeAllVis(outputItem, AspectHelper.primalList(0));
        }

        final int cost = scanResult.wandType()
            .getCraftingVisCost(scanResult.wandCaps(), scanResult.newRod());
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
            return new AspectList();
        }

        return AspectHelper.primalList(cost);
    }

    @Override
    public String getResearch() {
        return CustomResearch.replaceCoreResearch.key;
    }

    private @Nullable InvScanResult scanTable(IInventory tableInv) {
        ItemStack wandItem = null;
        WandRod newRod = null;

        int screwOreID = -1;
        ItemStack conductor = null;
        int screws = 0;
        int conductors = 0;

        // First pass: find only one wand and one new rod
        for (int slot = 0; slot < tableInv.getSizeInventory() && slot < 9; ++slot) {
            final ItemStack slotItem = tableInv.getStackInSlot(slot);
            if (slotItem == null) continue;

            final var maybeRod = WandHelper.getWandRodFromItem(slotItem);
            if (maybeRod != null) {
                if (newRod != null) return null; // Multiple rods
                newRod = maybeRod;

                continue;
            }

            final var maybeWand = WandHelper.getWandItem(slotItem);
            if (maybeWand != null) {
                if (wandItem != null) return null; // Multiple wands
                wandItem = slotItem;
            }
        }

        if (SalisConfig.modCompat.gtnhWands.coreSwapMaterials.isEnabled() && wandItem != null && newRod != null) {
            AbstractWandWrapper wrapper = GTNHTCWandsCompat.getWandWrapper(newRod, WandType.getWandType(wandItem));
            if (wrapper == null) return null;
            WandDetails props = wrapper.getDetails();
            screwOreID = OreDictionary.getOreID(props.getScrew());
            conductor = props.getConductor();
        }

        // Second pass: check screws & conductors with GTNHTCWands or find garbage
        for (int slot = 0; slot < tableInv.getSizeInventory() && slot < 9; ++slot) {
            final ItemStack slotItem = tableInv.getStackInSlot(slot);
            if (slotItem == null) continue;

            // Skip wand and rod slots
            if (slotItem == wandItem) continue;
            if (WandHelper.getWandRodFromItem(slotItem) == newRod) continue;

            if (!SalisConfig.modCompat.gtnhWands.coreSwapMaterials.isEnabled()) {
                return null; // Any leftover item
            }

            boolean matched = false;

            if (conductor != null && slotItem.isItemEqual(conductor)) {
                conductors++;
                matched = true;
            } else if (screwOreID != -1) {
                for (int id : OreDictionary.getOreIDs(slotItem)) {
                    if (id == screwOreID) {
                        screws++;
                        matched = true;
                        break;
                    }
                }
            }

            if (!matched) {
                return null; // Any leftover item
            }
        }

        return new InvScanResult(wandItem, newRod, screws, conductors);
    }

    @Override
    public String[] salisArcana$getResearches(IInventory inv, World world, EntityPlayer player) {
        final var scan = scanTable(inv);
        if (scan == null || scan.invalidInputs()) {
            return new String[] { getResearch() };
        }
        return new String[] { getResearch(), scan.newRod()
            .getResearch() };
    }

    @Desugar
    private record InvScanResult(ItemStack wandItem, WandRod newRod, int screws, int conductors) {

        public boolean invalidInputs() {
            if (wandItem == null || newRod == null) {
                return true;
            }
            if (SalisConfig.features.enforceWandCoreTypes.isEnabled() && !wandType().isCoreSuitable(newRod)) {
                return true;
            }
            final var oldRod = oldRod();
            if (oldRod == null || newRod == oldRod) {
                return true;
            }

            if (!SalisConfig.modCompat.gtnhWands.coreSwapMaterials.isEnabled()) return false;

            return screws != getRequiredScrews() || conductors != 2;
        }

        public int getRequiredScrews() {
            if (wandType() == WandType.SCEPTER || wandType() == WandType.STAFFTER) {
                return 2;
            }
            return 4;
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
