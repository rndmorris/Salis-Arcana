package dev.rndmorris.salisarcana.common.compat.nei;

import static dev.rndmorris.salisarcana.lib.WandHelper.GOLD_GREATWOOD;
import static dev.rndmorris.salisarcana.lib.WandHelper.GOLD_GREATWOOD_STAFF;
import static dev.rndmorris.salisarcana.lib.WandHelper.IRON_STICK;
import static dev.rndmorris.salisarcana.lib.WandHelper.THAUMIUM_SILVERWOOD_STAFF;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizons.aspectrecipeindex.nei.arcaneworkbench.ShapelessArcaneRecipeHandler;
import com.gtnewhorizons.aspectrecipeindex.nei.arcaneworkbench.WandRecipeHandler;
import com.gtnewhorizons.aspectrecipeindex.util.Util;
import com.gtnewhorizons.tcwands.api.TCWandAPI;
import com.gtnewhorizons.tcwands.api.wandinfo.WandDetails;
import com.gtnewhorizons.tcwands.api.wrappers.AbstractWandWrapper;

import dev.rndmorris.salisarcana.common.compat.GTNHTCWandsCompat;
import dev.rndmorris.salisarcana.common.recipes.ReplaceWandCoreRecipe;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.WandHelper;
import dev.rndmorris.salisarcana.lib.WandType;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;

public class WandCoreSubstitutionHandler extends ShapelessArcaneRecipeHandler {

    public static final String OVERLAY = "salisarcana.substitution.core";

    @Override
    public void loadUsageRecipes(String inputId, Object... results) {
        if (inputId.equals("item")) {
            super.loadUsageRecipes(inputId, results);
            return;
        }
        if (inputId.equals(this.getOverlayIdentifier())) {
            ItemStack outputWand = replaceCore(GOLD_GREATWOOD, ConfigItems.WAND_ROD_WOOD);
            if (WandRecipeHandler.shouldShowWandRecipe(outputWand)) {
                new WandRodSubstitutionCachedRecipe(GOLD_GREATWOOD, ConfigItems.WAND_ROD_WOOD, outputWand, true, false);
            }
            generateAllCoreSubstitutionRecipes(IRON_STICK, (ItemWandCasting) IRON_STICK.getItem());
            if (SalisConfig.features.enforceWandCoreTypes.isEnabled()) { // Hides duplicate substitution recipes
                outputWand = replaceCore(THAUMIUM_SILVERWOOD_STAFF, ConfigItems.STAFF_ROD_GREATWOOD);
                if (WandRecipeHandler.shouldShowWandRecipe(outputWand)) {
                    new WandRodSubstitutionCachedRecipe(
                        THAUMIUM_SILVERWOOD_STAFF,
                        ConfigItems.STAFF_ROD_GREATWOOD,
                        outputWand,
                        true,
                        false);
                }
                generateAllCoreSubstitutionRecipes(
                    GOLD_GREATWOOD_STAFF,
                    (ItemWandCasting) GOLD_GREATWOOD_STAFF.getItem());
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (ingredient.getItem() instanceof ItemWandCasting wand) {
            generateAllCoreSubstitutionRecipes(ingredient, wand);
            return;
        }
        if (!Util.shouldShowRecipe("salisarcana:REPLACEWANDCORE")) return;
        for (WandRod rod : WandRod.rods.values()) {
            if (!OreDictionary.itemMatches(ingredient, rod.getItem(), false)) {
                continue;
            }
            if (!WandRecipeHandler.validResearch(rod.getResearch())) return;
            ItemStack inputWand = getDisplayWand(rod);
            ItemStack outputWand = replaceCore(inputWand, rod);
            final WandType type = WandType.getWandType(inputWand);
            if (!WandRecipeHandler.shouldShowWandRecipe(outputWand)) return;
            final boolean scepter = type == WandType.SCEPTER || type == WandType.STAFFTER;
            new WandRodSubstitutionCachedRecipe(inputWand, rod, outputWand, true, scepter);
            return;
        }

    }

    @Override
    public String getOverlayIdentifier() {
        return OVERLAY;
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("tc.research_name.salisarcana:REPLACEWANDCORE");
    }

    /**
     * @return a new wand with the old wand's caps and the provided rod.
     */
    private ItemStack replaceCore(ItemStack inputWand, WandRod rod) {
        NBTTagCompound tag = inputWand.getTagCompound();
        ItemStack outputWand = inputWand.copy();
        if (!(inputWand.getItem() instanceof ItemWandCasting wand) || tag == null) {
            return outputWand;
        }
        WandType type = WandType.getWandType(inputWand);
        wand.setRod(outputWand, rod);
        ReplaceWandCoreRecipe.setNewWandVis(wand, outputWand);
        Items.feather.setDamage(outputWand, type.getCraftingVisCost(wand.getCap(outputWand), rod));
        return outputWand;
    }

    private ItemStack getDisplayWand(WandRod rod) {
        if (rod instanceof StaffRod) {
            return rod == ConfigItems.STAFF_ROD_GREATWOOD ? THAUMIUM_SILVERWOOD_STAFF : GOLD_GREATWOOD_STAFF;
        }
        return rod == ConfigItems.WAND_ROD_WOOD ? GOLD_GREATWOOD : IRON_STICK;
    }

    private void generateAllCoreSubstitutionRecipes(ItemStack wandItem, ItemWandCasting wand) {
        WandType type = WandType.getWandType(wandItem);
        boolean scepter = type == WandType.SCEPTER || type == WandType.STAFFTER;
        boolean replaceCoreResearch = Util.shouldShowRecipe("salisarcana:REPLACEWANDCORE");
        boolean scepterResearch = !scepter || Util.shouldShowRecipe("SCEPTRE");
        for (WandRod rod : WandRod.rods.values()) {
            if (!WandRecipeHandler.validResearch(rod.getResearch()) || rod == wand.getRod(wandItem)
                || (SalisConfig.features.enforceWandCoreTypes.isEnabled() && !type.isCoreSuitable(rod))) continue;
            ItemStack outputWand = replaceCore(wandItem, rod);
            boolean shouldShowRecipe = replaceCoreResearch && Util.shouldShowRecipe(rod.getResearch())
                && scepterResearch;
            new WandRodSubstitutionCachedRecipe(wandItem, rod, outputWand, shouldShowRecipe, scepter);
        }
    }

    protected class WandRodSubstitutionCachedRecipe extends ArcaneShapelessCachedRecipe {

        protected WandRodSubstitutionCachedRecipe(ItemStack input, WandRod rod, ItemStack output,
            boolean shouldShowRecipe, boolean isScepter) {
            super(
                SalisConfig.modCompat.gtnhWands.coreSwapMaterials.isEnabled()
                    ? gtnhIngredients(input, rod, output, isScepter)
                    : new Object[] { input, rod.getItem() },
                output,
                shouldShowRecipe,
                WandHelper.wandCost(output));

            addResearch("salisarcana:REPLACEWANDCORE");
            addResearch(rod.getResearch());
            if (isScepter) addResearch("SCEPTRE");
        }

        protected static Object[] gtnhIngredients(ItemStack input, WandRod rod, ItemStack output, boolean isScepter) {
            WandType wandType = WandType.getWandType(output);
            AbstractWandWrapper wrapper = GTNHTCWandsCompat.getWandWrapper(rod, wandType);
            if (wrapper == null) wrapper = TCWandAPI.getWandWrappers()
                .get(0);
            WandDetails props = wrapper.getDetails();
            ItemStack screw = OreDictionary.getOres(props.getScrew())
                .get(0);
            ItemStack conductor = props.getConductor();
            if (isScepter) {
                return new Object[] { input, rod.getItem(), screw, screw, conductor, conductor };
            }
            return new Object[] { input, rod.getItem(), screw, screw, screw, screw, conductor, conductor };
        }

    }

}
