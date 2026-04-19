package dev.rndmorris.salisarcana.common.compat.nei;

import static dev.rndmorris.salisarcana.lib.WandHelper.GOLD_GREATWOOD;
import static dev.rndmorris.salisarcana.lib.WandHelper.GOLD_GREATWOOD_SCEPTER;
import static dev.rndmorris.salisarcana.lib.WandHelper.GOLD_GREATWOOD_STAFF;
import static dev.rndmorris.salisarcana.lib.WandHelper.GOLD_GREATWOOD_STAFFTER;
import static dev.rndmorris.salisarcana.lib.WandHelper.IRON_GREATWOOD_STAFF;
import static dev.rndmorris.salisarcana.lib.WandHelper.IRON_GREATWOOD_STAFFTER;
import static dev.rndmorris.salisarcana.lib.WandHelper.IRON_STICK;
import static dev.rndmorris.salisarcana.lib.WandHelper.IRON_STICK_SCEPTER;
import static dev.rndmorris.salisarcana.lib.WandHelper.SCEPTRE_RESEARCH;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizons.aspectrecipeindex.nei.arcaneworkbench.ShapelessArcaneRecipeHandler;
import com.gtnewhorizons.aspectrecipeindex.nei.arcaneworkbench.WandRecipeHandler;
import com.gtnewhorizons.aspectrecipeindex.util.Util;

import dev.rndmorris.salisarcana.common.CustomResearch;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.WandHelper;
import dev.rndmorris.salisarcana.lib.WandType;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;

public class WandCapSubstitutionHandler extends ShapelessArcaneRecipeHandler {

    public static final String OVERLAY = "salisarcana.substitution.caps";
    private static final String REPLACE_CAPS_RESEARCH = CustomResearch.replaceCapsResearch.key;

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
            ItemStack outputWand = replaceCap(GOLD_GREATWOOD, ConfigItems.WAND_CAP_IRON);
            boolean shouldShowRecipe = Util.shouldShowRecipe(REPLACE_CAPS_RESEARCH)
                && Util.shouldShowRecipe(ConfigItems.WAND_CAP_IRON.getResearch());
            new WandCapSubstitutionCachedRecipe(
                GOLD_GREATWOOD,
                ConfigItems.WAND_CAP_IRON,
                outputWand,
                shouldShowRecipe,
                false);
            generateAllCapSubstitutionRecipes(IRON_STICK, (ItemWandCasting) IRON_STICK.getItem());
            outputWand = replaceCap(GOLD_GREATWOOD_SCEPTER, ConfigItems.WAND_CAP_IRON);
            new WandCapSubstitutionCachedRecipe(
                GOLD_GREATWOOD_SCEPTER,
                ConfigItems.WAND_CAP_IRON,
                outputWand,
                shouldShowRecipe && Util.shouldShowRecipe(SCEPTRE_RESEARCH),
                true);
            generateAllCapSubstitutionRecipes(IRON_STICK_SCEPTER, (ItemWandCasting) IRON_STICK_SCEPTER.getItem());
            shouldShowRecipe = Util.shouldShowRecipe(REPLACE_CAPS_RESEARCH)
                && Util.shouldShowRecipe(ConfigItems.WAND_CAP_IRON.getResearch());
            outputWand = replaceCap(GOLD_GREATWOOD_STAFF, ConfigItems.WAND_CAP_IRON);
            new WandCapSubstitutionCachedRecipe(
                GOLD_GREATWOOD_STAFF,
                ConfigItems.WAND_CAP_IRON,
                outputWand,
                shouldShowRecipe,
                false);
            generateAllCapSubstitutionRecipes(IRON_GREATWOOD_STAFF, (ItemWandCasting) IRON_GREATWOOD_STAFF.getItem());
            outputWand = replaceCap(GOLD_GREATWOOD_STAFFTER, ConfigItems.WAND_CAP_IRON);
            new WandCapSubstitutionCachedRecipe(
                GOLD_GREATWOOD_STAFFTER,
                ConfigItems.WAND_CAP_IRON,
                outputWand,
                shouldShowRecipe && Util.shouldShowRecipe(SCEPTRE_RESEARCH),
                true);
            generateAllCapSubstitutionRecipes(
                IRON_GREATWOOD_STAFFTER,
                (ItemWandCasting) IRON_GREATWOOD_STAFFTER.getItem());
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (!Util.shouldShowRecipe(REPLACE_CAPS_RESEARCH)) return;
        if (ingredient.getItem() instanceof ItemWandCasting wand) {
            generateAllCapSubstitutionRecipes(ingredient, wand);
            return;
        }
        for (WandCap cap : WandCap.caps.values()) {
            if (!OreDictionary.itemMatches(ingredient, cap.getItem(), false)) {
                continue;
            }
            if (!WandRecipeHandler.show(cap.getResearch())) return;
            generateCapSubstitutionRecipe(cap, WandType.WAND);
            generateCapSubstitutionRecipe(cap, WandType.STAFF);
            if (!WandRecipeHandler.show(SCEPTRE_RESEARCH)) return;
            generateCapSubstitutionRecipe(cap, WandType.SCEPTER);
            generateCapSubstitutionRecipe(cap, WandType.STAFFTER);
            return;
        }
    }

    private void generateCapSubstitutionRecipe(WandCap cap, WandType type) {
        final boolean scepter = type == WandType.SCEPTER || type == WandType.STAFFTER;
        WandRod displayRod = getDisplayRod(type);
        WandCap displayCap = getDisplayCap(cap);
        ItemStack displayWand = WandHelper.createWand(displayRod, displayCap, scepter);
        // It's checked this way unlike the core handler to prevent cases of showing uses with a display greatwood staff
        // when the greatwood staff core is not unlocked
        if (!WandRecipeHandler.shouldShowWandRecipe(displayWand)) return;
        new WandCapSubstitutionCachedRecipe(
            displayWand,
            cap,
            WandHelper.createWand(displayRod, cap, scepter),
            true,
            scepter);
    }

    private WandCap getDisplayCap(WandCap cap) {
        return cap == ConfigItems.WAND_CAP_GOLD ? ConfigItems.WAND_CAP_IRON : ConfigItems.WAND_CAP_GOLD;
    }

    private WandRod getDisplayRod(WandType type) {
        return type == WandType.STAFF || type == WandType.STAFFTER ? ConfigItems.STAFF_ROD_GREATWOOD
            : ConfigItems.WAND_ROD_GREATWOOD;
    }

    @Override
    public String getOverlayIdentifier() {
        return OVERLAY;
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("tc.research_name." + REPLACE_CAPS_RESEARCH);
    }

    /**
     * @return a new wand with the old wand's rod and the provided cap.
     */
    private ItemStack replaceCap(ItemStack inputWand, WandCap cap) {
        NBTTagCompound tag = inputWand.getTagCompound();
        if (!(inputWand.getItem() instanceof ItemWandCasting wand) || tag == null) {
            return inputWand.copy();
        }
        WandType type = WandType.getWandType(inputWand);
        ItemStack newWand = inputWand.copy();
        wand.setCap(newWand, cap);
        if (!SalisConfig.features.preserveWandVis.isEnabled()) {
            for (Aspect a : Aspect.getPrimalAspects()) {
                newWand.stackTagCompound.removeTag(a.getTag());
            }
        }
        Items.feather.setDamage(newWand, type.getCraftingVisCost(cap, wand.getRod(newWand)));
        return newWand;
    }

    private void generateAllCapSubstitutionRecipes(ItemStack wandItem, ItemWandCasting wand) {
        WandType type = WandType.getWandType(wandItem);
        boolean scepter = type == WandType.SCEPTER || type == WandType.STAFFTER;
        boolean replaceCapsResearch = Util.shouldShowRecipe(REPLACE_CAPS_RESEARCH);
        boolean scepterResearch = !scepter || Util.shouldShowRecipe("SCEPTRE");
        for (WandCap cap : WandCap.caps.values()) {
            if (!WandRecipeHandler.validResearch(cap.getResearch()) || cap == wand.getCap(wandItem)) continue;
            ItemStack outputWand = replaceCap(wandItem, cap);
            boolean shouldShowRecipe = replaceCapsResearch && Util.shouldShowRecipe(cap.getResearch())
                && scepterResearch;
            new WandCapSubstitutionCachedRecipe(wandItem, cap, outputWand, shouldShowRecipe, scepter);
        }
    }

    protected class WandCapSubstitutionCachedRecipe extends ArcaneShapelessCachedRecipe {

        protected WandCapSubstitutionCachedRecipe(ItemStack input, WandCap cap, ItemStack output,
            boolean shouldShowRecipe, boolean isScepter) {
            super(ingredients(input, cap, isScepter), output, shouldShowRecipe, WandHelper.wandVisCost(output));
            addResearch(REPLACE_CAPS_RESEARCH);
            addResearch(cap.getResearch());
            if (isScepter) addResearch("SCEPTRE");
        }

        private static Object[] ingredients(ItemStack input, WandCap cap, boolean isScepter) {
            ItemStack capItem = cap.getItem();
            if (isScepter) return new Object[] { input, capItem, capItem, capItem };
            return new Object[] { input, capItem, capItem };
        }
    }
}
