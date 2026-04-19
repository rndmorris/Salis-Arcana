package dev.rndmorris.salisarcana.common.compat.nei;

import static dev.rndmorris.salisarcana.lib.WandHelper.GOLD_GREATWOOD;
import static dev.rndmorris.salisarcana.lib.WandHelper.GOLD_GREATWOOD_SCEPTER;
import static dev.rndmorris.salisarcana.lib.WandHelper.GOLD_GREATWOOD_STAFF;
import static dev.rndmorris.salisarcana.lib.WandHelper.GOLD_GREATWOOD_STAFFTER;
import static dev.rndmorris.salisarcana.lib.WandHelper.IRON_STICK;
import static dev.rndmorris.salisarcana.lib.WandHelper.IRON_STICK_SCEPTER;
import static dev.rndmorris.salisarcana.lib.WandHelper.SCEPTRE_RESEARCH;
import static dev.rndmorris.salisarcana.lib.WandHelper.THAUMIUM_SILVERWOOD_STAFF;
import static dev.rndmorris.salisarcana.lib.WandHelper.THAUMIUM_SILVERWOOD_STAFFTER;

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

import dev.rndmorris.salisarcana.common.CustomResearch;
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
    private static final String REPLACE_CORE_RESEARCH = CustomResearch.replaceCoreResearch.key;

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("item")) return;
        loadUsageRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        // NO OP
    }

    @Override
    public void loadUsageRecipes(String inputId, Object... results) {
        if (inputId.equals("item")) {
            super.loadUsageRecipes(inputId, results);
            return;
        }
        if (inputId.equals(this.getOverlayIdentifier())) {
            ItemStack outputWand = replaceCore(GOLD_GREATWOOD, ConfigItems.WAND_ROD_WOOD);
            boolean shouldShowRecipe = WandRecipeHandler.shouldShowWandRecipe(outputWand)
                && Util.shouldShowRecipe(REPLACE_CORE_RESEARCH);
            new WandRodSubstitutionCachedRecipe(
                GOLD_GREATWOOD,
                ConfigItems.WAND_ROD_WOOD,
                outputWand,
                shouldShowRecipe,
                false);
            generateAllCoreSubstitutionRecipes(IRON_STICK, (ItemWandCasting) IRON_STICK_SCEPTER.getItem());
            outputWand = replaceCore(GOLD_GREATWOOD_SCEPTER, ConfigItems.WAND_ROD_WOOD);
            new WandRodSubstitutionCachedRecipe(
                GOLD_GREATWOOD,
                ConfigItems.WAND_ROD_WOOD,
                outputWand,
                shouldShowRecipe && Util.shouldShowRecipe(SCEPTRE_RESEARCH),
                false);
            generateAllCoreSubstitutionRecipes(IRON_STICK_SCEPTER, (ItemWandCasting) IRON_STICK.getItem());
            outputWand = replaceCore(THAUMIUM_SILVERWOOD_STAFF, ConfigItems.STAFF_ROD_GREATWOOD);
            shouldShowRecipe = WandRecipeHandler.shouldShowWandRecipe(outputWand)
                && Util.shouldShowRecipe(REPLACE_CORE_RESEARCH);
            new WandRodSubstitutionCachedRecipe(
                THAUMIUM_SILVERWOOD_STAFF,
                ConfigItems.STAFF_ROD_GREATWOOD,
                outputWand,
                shouldShowRecipe,
                false);
            generateAllCoreSubstitutionRecipes(GOLD_GREATWOOD_STAFF, (ItemWandCasting) GOLD_GREATWOOD_STAFF.getItem());
            outputWand = replaceCore(THAUMIUM_SILVERWOOD_STAFFTER, ConfigItems.STAFF_ROD_GREATWOOD);
            new WandRodSubstitutionCachedRecipe(
                THAUMIUM_SILVERWOOD_STAFFTER,
                ConfigItems.STAFF_ROD_GREATWOOD,
                outputWand,
                shouldShowRecipe && Util.shouldShowRecipe(SCEPTRE_RESEARCH),
                false);
            generateAllCoreSubstitutionRecipes(
                GOLD_GREATWOOD_STAFFTER,
                (ItemWandCasting) GOLD_GREATWOOD_STAFFTER.getItem());
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (!Util.shouldShowRecipe(REPLACE_CORE_RESEARCH)) return;
        if (ingredient.getItem() instanceof ItemWandCasting wand) {
            generateAllCoreSubstitutionRecipes(ingredient, wand);
            return;
        }
        for (WandRod rod : WandRod.rods.values()) {
            if (!OreDictionary.itemMatches(ingredient, rod.getItem(), false)) {
                continue;
            }
            if (!WandRecipeHandler.show(rod.getResearch())) return;
            generateCoreSubstitutionRecipe(rod, WandType.WAND);
            generateCoreSubstitutionRecipe(rod, WandType.STAFF);
            if (!WandRecipeHandler.show(SCEPTRE_RESEARCH)) return;
            generateCoreSubstitutionRecipe(rod, WandType.SCEPTER);
            generateCoreSubstitutionRecipe(rod, WandType.STAFFTER);
            return;
        }

    }

    private void generateCoreSubstitutionRecipe(WandRod rod, WandType type) {
        final boolean scepter = type == WandType.SCEPTER || type == WandType.STAFFTER;
        if (!type.isCoreSuitable(rod) || !Util.shouldShowRecipe(rod.getResearch())
            || (scepter && !Util.shouldShowRecipe(SCEPTRE_RESEARCH))) return;
        new WandRodSubstitutionCachedRecipe(
            WandHelper.createWand(getDisplayRod(rod), ConfigItems.WAND_CAP_IRON, scepter),
            rod,
            WandHelper.createWand(rod, ConfigItems.WAND_CAP_IRON, scepter),
            true,
            scepter);
    }

    @Override
    public String getOverlayIdentifier() {
        return OVERLAY;
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("tc.research_name." + REPLACE_CORE_RESEARCH);
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

    private WandRod getDisplayRod(WandRod rod) {
        if (rod instanceof StaffRod) {
            return rod == ConfigItems.STAFF_ROD_GREATWOOD ? ConfigItems.STAFF_ROD_SILVERWOOD
                : ConfigItems.STAFF_ROD_GREATWOOD;
        }
        return rod == ConfigItems.WAND_ROD_WOOD ? ConfigItems.WAND_ROD_GREATWOOD : ConfigItems.WAND_ROD_WOOD;
    }

    private void generateAllCoreSubstitutionRecipes(ItemStack wandItem, ItemWandCasting wand) {
        WandType type = WandType.getWandType(wandItem);
        boolean scepter = type == WandType.SCEPTER || type == WandType.STAFFTER;
        boolean replaceCoreResearch = Util.shouldShowRecipe(REPLACE_CORE_RESEARCH);
        boolean scepterResearch = !scepter || Util.shouldShowRecipe(SCEPTRE_RESEARCH);
        for (WandRod rod : WandRod.rods.values()) {
            if (!WandRecipeHandler.validResearch(rod.getResearch()) || rod == wand.getRod(wandItem)
                || !type.isCoreSuitable(rod)) continue;
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
                WandHelper.wandVisCost(output));

            addResearch(REPLACE_CORE_RESEARCH);
            addResearch(rod.getResearch());
            if (isScepter) addResearch(SCEPTRE_RESEARCH);
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
