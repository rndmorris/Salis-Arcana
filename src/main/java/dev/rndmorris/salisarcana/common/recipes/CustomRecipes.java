package dev.rndmorris.salisarcana.common.recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import dev.rndmorris.salisarcana.common.blocks.CustomBlocks;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

public class CustomRecipes {

    public static void registerRecipes() {

        final var enhancements = ConfigModuleRoot.enhancements;

        if (enhancements.lookalikePlanks.isEnabled()) {
            registerPlankRecipes();
        }

        if (enhancements.lessPickyPrimalCharmRecipe.isEnabled()) {
            // noinspection unchecked
            ThaumcraftApi.getCraftingRecipes()
                .add(new RecipeForgivingPrimalCharm());
        }

        if (enhancements.rotatedThaumometerRecipe.isEnabled()) {
            registerRotatedThaumometer();
        }
    }

    public static void registerRecipesPostInit() {
        if (ConfigModuleRoot.enhancements.rotatedFociRecipes.isEnabled()) {
            // registered here because TC4 doesn't register its recipes until post init
            registerRotatedFoci();
        }
    }

    private static void registerPlankRecipes() {
        final var thaumGreatwoodPlanks = new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6);
        final var thaumSilverwoodPlanks = new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 7);
        final var arcanaGreatwoodPlanks = new ItemStack(CustomBlocks.blockPlank, 1, 0);
        final var arcanaSilverwoodPlanks = new ItemStack(CustomBlocks.blockPlank, 1, 1);

        ItemStack conversionOutput;

        conversionOutput = arcanaGreatwoodPlanks.copy();
        conversionOutput.stackSize = 8;

        GameRegistry.addShapelessRecipe(
            conversionOutput,
            thaumGreatwoodPlanks,
            thaumGreatwoodPlanks,
            thaumGreatwoodPlanks,
            thaumGreatwoodPlanks,
            thaumGreatwoodPlanks,
            thaumGreatwoodPlanks,
            thaumGreatwoodPlanks,
            thaumGreatwoodPlanks);

        conversionOutput = arcanaSilverwoodPlanks.copy();
        conversionOutput.stackSize = 8;
        GameRegistry.addShapelessRecipe(
            conversionOutput,
            thaumSilverwoodPlanks,
            thaumSilverwoodPlanks,
            thaumSilverwoodPlanks,
            thaumSilverwoodPlanks,
            thaumSilverwoodPlanks,
            thaumSilverwoodPlanks,
            thaumSilverwoodPlanks,
            thaumSilverwoodPlanks);

        conversionOutput = thaumGreatwoodPlanks.copy();
        conversionOutput.stackSize = 8;
        GameRegistry.addShapelessRecipe(
            conversionOutput,
            arcanaGreatwoodPlanks,
            arcanaGreatwoodPlanks,
            arcanaGreatwoodPlanks,
            arcanaGreatwoodPlanks,
            arcanaGreatwoodPlanks,
            arcanaGreatwoodPlanks,
            arcanaGreatwoodPlanks,
            arcanaGreatwoodPlanks);

        conversionOutput = thaumSilverwoodPlanks.copy();
        conversionOutput.stackSize = 8;
        GameRegistry.addShapelessRecipe(
            conversionOutput,
            arcanaSilverwoodPlanks,
            arcanaSilverwoodPlanks,
            arcanaSilverwoodPlanks,
            arcanaSilverwoodPlanks,
            arcanaSilverwoodPlanks,
            arcanaSilverwoodPlanks,
            arcanaSilverwoodPlanks,
            arcanaSilverwoodPlanks);

        // Greatwood Slabs
        final var greatwoodSlabs = new ItemStack(ConfigBlocks.blockSlabWood, 6, 0);
        registerSlabRecipes(greatwoodSlabs, thaumGreatwoodPlanks, arcanaGreatwoodPlanks);
        GameRegistry.addRecipe(new ShapedOreRecipe(greatwoodSlabs, "PPP", 'P', CustomBlocks.ORE_DICT_GREATWOOD_PLANKS));

        // Silverwood Slabs
        final var silverwoodSlabs = new ItemStack(ConfigBlocks.blockSlabWood, 6, 1);
        registerSlabRecipes(silverwoodSlabs, thaumSilverwoodPlanks, arcanaSilverwoodPlanks);
        GameRegistry
            .addRecipe(new ShapedOreRecipe(silverwoodSlabs, "PPP", 'P', CustomBlocks.ORE_DICT_SILVERWOOD_PLANKS));

        // Greatwood Stairs
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ConfigBlocks.blockStairsGreatwood, 4, 0),
                "P  ",
                "PP ",
                "PPP",
                'P',
                CustomBlocks.ORE_DICT_GREATWOOD_PLANKS));

        // Silverwood Stairs
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ConfigBlocks.blockStairsSilverwood, 4, 0),
                "P  ",
                "PP ",
                "PPP",
                'P',
                CustomBlocks.ORE_DICT_SILVERWOOD_PLANKS));
    }

    private static void registerSlabRecipes(ItemStack output, ItemStack tcPlanks, ItemStack tfPlanks) {
        // with one Arcana plank
        GameRegistry.addShapedRecipe(output, "CCF", 'C', tcPlanks, 'F', tfPlanks);
        GameRegistry.addShapedRecipe(output, "CFC", 'C', tcPlanks, 'F', tfPlanks);
        GameRegistry.addShapedRecipe(output, "FCC", 'C', tcPlanks, 'F', tfPlanks);

        // with two Arcana planks
        GameRegistry.addShapedRecipe(output, "CFF", 'C', tcPlanks, 'F', tfPlanks);
        GameRegistry.addShapedRecipe(output, "FCF", 'C', tcPlanks, 'F', tfPlanks);

        // only Arcana planks
        GameRegistry.addShapedRecipe(output, "FFF", 'F', tfPlanks);
    }

    private static void registerRotatedThaumometer() {
        final var recipe = new ShapedOreRecipe(
            ConfigItems.itemThaumometer,
            " I ",
            "SGS",
            " I ",
            'I',
            Items.gold_ingot,
            'G',
            Blocks.glass,
            'S',
            new ItemStack(ConfigItems.itemShard, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addRecipe(recipe);
    }

    private static void registerRotatedFoci() {
        final var toFind = new HashSet<Item>();
        final var toAdd = new ArrayList<ShapedArcaneRecipe>();
        Collections.addAll(
            toFind,
            ConfigItems.itemFocusFire,
            ConfigItems.itemFocusShock,
            ConfigItems.itemFocusFrost,
            ConfigItems.itemFocusTrade,
            ConfigItems.itemFocusExcavation,
            ConfigItems.itemFocusPrimal);

        for (var recipe : ThaumcraftApi.getCraftingRecipes()) {
            ItemStack output;
            Item outputItem;
            if (recipe instanceof ShapedArcaneRecipe arcaneRecipe && (output = arcaneRecipe.getRecipeOutput()) != null
                && (outputItem = output.getItem()) != null
                && toFind.contains(outputItem)) {
                toAdd.add(createCopy(arcaneRecipe));
                toFind.remove(outputItem);
            }
        }

        // noinspection unchecked
        ThaumcraftApi.getCraftingRecipes()
            .addAll(toAdd);
    }

    private static ShapedArcaneRecipe createCopy(ShapedArcaneRecipe inputRecipe) {
        final var newRecipe = new ShapedArcaneRecipe(
            "DUMMY",
            new ItemStack(Items.stick),
            new AspectList(),
            "   ",
            " S ",
            "   ",
            'S',
            Items.stick);
        newRecipe.output = inputRecipe.output;
        newRecipe.input = copyRotated(inputRecipe.input);
        newRecipe.aspects = inputRecipe.aspects.copy();
        newRecipe.research = inputRecipe.research;
        newRecipe.width = inputRecipe.width;
        newRecipe.height = inputRecipe.height;
        return newRecipe;
    }

    private static Object[] copyRotated(Object[] input) {
        final var output = new Object[9];
        for (var index = 0; index < input.length && index < 9; ++index) {
            final var newIndex = getRotatedIndex(index);
            if (newIndex < 0) {
                continue;
            }
            output[newIndex] = input[index];
        }
        return output;
    }

    private static int getRotatedIndex(int index) {
        return switch (index) {
            case 0 -> 1;
            case 1 -> 2;
            case 2 -> 5;
            case 3 -> 0;
            case 4 -> 4;
            case 5 -> 8;
            case 6 -> 3;
            case 7 -> 6;
            case 8 -> 7;
            default -> -1;
        };
    }

}
