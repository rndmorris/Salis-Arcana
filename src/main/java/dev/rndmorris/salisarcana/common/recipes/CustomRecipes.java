package dev.rndmorris.salisarcana.common.recipes;

import static dev.rndmorris.salisarcana.SalisArcana.MODID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import dev.rndmorris.salisarcana.api.INeiOnlyRecipeRegistry;
import dev.rndmorris.salisarcana.common.blocks.CustomBlocks;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.lib.AspectHelper;
import dev.rndmorris.salisarcana.lib.R;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.config.ConfigResearch;

public class CustomRecipes {

    public static final CleanFocusRecipe[] cleanFocusRecipes = new CleanFocusRecipe[5];
    public static @Nullable ReplaceWandCapsRecipe replaceWandCapsRecipe = null;
    public static @Nullable ReplaceWandCoreRecipe replaceWandCoreRecipe = null;

    public static class Examples {

        public static List<ShapelessArcaneRecipe> oneUpgrade = new ArrayList<>();
        public static List<ShapelessArcaneRecipe> twoUpgrade = new ArrayList<>();
        public static List<ShapelessArcaneRecipe> threeUpgrade = new ArrayList<>();
        public static List<ShapelessArcaneRecipe> fourUpgrade = new ArrayList<>();
        public static List<ShapelessArcaneRecipe> fiveUpgrade = new ArrayList<>();
    }

    private static final HashMap<ItemFocusBasic, R> focusReflectors = new HashMap<>();

    public static void withItemFocusReflection(ItemFocusBasic itemFocusBasic, Consumer<R> callback) {
        final R reflector;
        synchronized (focusReflectors) {
            reflector = focusReflectors.computeIfAbsent(itemFocusBasic, (key) -> new R(itemFocusBasic));
        }
        callback.accept(reflector);
    }

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

        if (ConfigModuleRoot.bugfixes.slabBurnTimeFix.isEnabled()) {
            MinecraftForge.EVENT_BUS.register(new FuelBurnTimeEventHandler());
        }

        if (enhancements.replaceWandCapsSettings.isEnabled()) {
            // noinspection unchecked
            ThaumcraftApi.getCraftingRecipes()
                .add(replaceWandCapsRecipe = new ReplaceWandCapsRecipe());
        }

        if (enhancements.replaceWandCoreSettings.isEnabled()) {
            // noinspection unchecked
            ThaumcraftApi.getCraftingRecipes()
                .add(replaceWandCoreRecipe = new ReplaceWandCoreRecipe());
        }

        if (enhancements.rottenFleshRecipe.isEnabled()) {
            GameRegistry
                .addShapelessRecipe(new ItemStack(Items.rotten_flesh, 9), new ItemStack(ConfigBlocks.blockTaint, 1, 2));
        }

        if (enhancements.crystalClusterUncrafting.isEnabled()) {
            for (var metadata = 0; metadata <= 5; ++metadata) {
                GameRegistry.addShapelessRecipe(
                    new ItemStack(ConfigItems.itemShard, 6, metadata),
                    new ItemStack(ConfigBlocks.blockCrystal, 1, metadata));
            }
        }
    }

    public static void registerRecipesPostInit() {
        if (ConfigModuleRoot.enhancements.rotatedFociRecipes.isEnabled()) {
            // registered here because TC4 doesn't register its recipes until post init
            registerRotatedFoci();
        }
        if (ConfigModuleRoot.bugfixes.fixEFRRecipes.isEnabled() && Loader.isModLoaded("etfuturum")) {
            registerEFRRecipes();
        }
        if (ConfigModuleRoot.enhancements.focusDowngradeRecipe.isEnabled()) {
            registerFocusDowngradeRecipes();
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

    public static void registerEFRRecipes() {
        HashMap<Item, String> map = new HashMap<>();
        map.put(Item.getItemFromBlock(Blocks.trapdoor), "trapdoorWood");

        for (Map.Entry<String, Object> entry : ConfigResearch.recipes.entrySet()) {
            if (entry.getValue() instanceof ShapedArcaneRecipe recipe) {
                Object[] input = recipe.getInput();
                for (int i = 0; i < recipe.getInput().length; i++) {
                    if (input[i] instanceof ItemStack item) {
                        if (map.containsKey(item.getItem())) {
                            input[i] = OreDictionary.getOres(map.get(item.getItem()));
                        }
                    }
                }
            } else if (entry.getValue() instanceof ShapelessArcaneRecipe recipe) {
                // noinspection unchecked
                ArrayList<Object> input = recipe.getInput();
                for (int i = 0; i < input.size(); i++) {
                    if (input.get(i) instanceof ItemStack item) {
                        if (map.containsKey(item.getItem())) {
                            input.set(i, OreDictionary.getOres(map.get(item.getItem())));
                        }
                    }
                }
            }
        }
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

    private static void registerFocusDowngradeRecipes() {
        for (var strength = 0; strength < 5; ++strength) {
            // noinspection unchecked
            ThaumcraftApi.getCraftingRecipes()
                .add(cleanFocusRecipes[strength] = new CleanFocusRecipe(strength + 1));
        }

        final var salt = new ItemStack(ConfigItems.itemResource, 1, 14);
        final var cloth = new ItemStack(ConfigItems.itemResource, 1, 7);

        final var registry = INeiOnlyRecipeRegistry.getInstance();
        for (var item : Item.itemRegistry) {
            if (!(item instanceof ItemFocusBasic itemFocus)) {
                continue;
            }
            final var subItemStacks = new ArrayList<ItemStack>();
            itemFocus.getSubItems(itemFocus, null, subItemStacks);
            for (var subItemStack : subItemStacks) {
                final var inputStacks = prepareFocusInputStacks(itemFocus, subItemStack);
                final var outputStack = prepareFocusOutputFocus(itemFocus, subItemStack);

                for (var index = 0; index < 5; ++index) {
                    final var inputs = new ArrayList<ItemStack>(3 + index);
                    inputs.add(inputStacks.get(index));
                    inputs.add(cloth);
                    for (var strength = 0; strength <= index; ++strength) {
                        inputs.add(salt);
                    }

                    final var recipe = registry.registerFakeShapelessArcaneRecipe(
                        MODID + ":" + ConfigModuleRoot.enhancements.focusDowngradeRecipe.researchName,
                        outputStack,
                        AspectHelper.primalList((index + 1) * 10),
                        inputs.toArray(new Object[0]));
                    (switch (index) {
                        case 0 -> Examples.oneUpgrade;
                        case 1 -> Examples.twoUpgrade;
                        case 2 -> Examples.threeUpgrade;
                        case 3 -> Examples.fourUpgrade;
                        default -> Examples.fiveUpgrade;
                    }).add(recipe);
                }
            }
        }
    }

    private static List<ItemStack> prepareFocusInputStacks(ItemFocusBasic itemFocus, ItemStack inputStack) {
        final var outputList = new ArrayList<ItemStack>();

        final var upgrades = new short[] { -1, -1, -1, -1, -1, };

        for (var index = 0; index < upgrades.length; ++index) {
            upgrades[index] = FocusUpgradeType.frugal.id;
            final var upgradedStack = inputStack.copy();
            withItemFocusReflection(itemFocus, (r) -> r.call("setFocusUpgradeTagList", upgradedStack, upgrades));
            outputList.add(upgradedStack);
        }

        return outputList;
    }

    private static ItemStack prepareFocusOutputFocus(ItemFocusBasic itemFocus, ItemStack outputStack) {
        final NBTTagCompound itemTag;

        withItemFocusReflection(
            itemFocus,
            (r) -> r.call("setFocusUpgradeTagList", outputStack, new short[] { -1, -1, -1, -1, -1, }));

        return outputStack;
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
