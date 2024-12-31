package dev.rndmorris.salisarcana.common.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import dev.rndmorris.salisarcana.common.blocks.CustomBlocks;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.common.config.ConfigBlocks;

public class CustomRecipes {

    public static void registerRecipes() {

        final var enhancements = ConfigModuleRoot.enhancements;

        if (enhancements.lookalikePlanks.isEnabled()) {
            registerPlankRecipes();
        }

        if (enhancements.lessPickyPrimalCharmRecipe.isEnabled()) {
            final var primalCharmAlt = new RecipeForgivingPrimalCharm();
            if (primalCharmAlt.initializedSuccessfully) {
                // noinspection unchecked
                ThaumcraftApi.getCraftingRecipes()
                    .add(primalCharmAlt);
            }
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

}
