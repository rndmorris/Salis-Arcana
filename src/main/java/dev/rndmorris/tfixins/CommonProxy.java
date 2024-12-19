package dev.rndmorris.tfixins;

import static dev.rndmorris.tfixins.config.ConfigModuleRoot.commandsModule;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import dev.rndmorris.tfixins.common.blocks.CustomBlocks;
import dev.rndmorris.tfixins.common.commands.CreateNodeCommand;
import dev.rndmorris.tfixins.common.commands.FixinsCommandBase;
import dev.rndmorris.tfixins.common.commands.ForgetResearchCommand;
import dev.rndmorris.tfixins.common.commands.ForgetScannedCommand;
import dev.rndmorris.tfixins.common.commands.HelpCommand;
import dev.rndmorris.tfixins.common.commands.ListResearchCommand;
import dev.rndmorris.tfixins.common.commands.PrerequisitesCommand;
import dev.rndmorris.tfixins.common.commands.UpdateNodeCommand;
import dev.rndmorris.tfixins.config.ConfigModuleRoot;
import dev.rndmorris.tfixins.config.ConfigPhase;
import dev.rndmorris.tfixins.config.settings.CommandSettings;
import thaumcraft.common.config.ConfigBlocks;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)

    public void preInit(FMLPreInitializationEvent event) {
        ConfigModuleRoot.synchronizeConfiguration(ConfigPhase.LATE);

        if (ConfigModuleRoot.enhancements.lookalikePlanks.isEnabled()) {
            CustomBlocks.registerBlocks();
            registerPlankRecipes();
        }
    }

    private void registerPlankRecipes() {
        final var thaumGreatwoodPlanks = new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6);
        final var thaumSilverwoodPlanks = new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 7);
        final var fixinsGreatwoodPlanks = new ItemStack(CustomBlocks.blockPlank, 1, 0);
        final var fixinsSilverwoodPlanks = new ItemStack(CustomBlocks.blockPlank, 1, 1);

        ItemStack conversionOutput;

        conversionOutput = fixinsGreatwoodPlanks.copy();
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

        conversionOutput = fixinsSilverwoodPlanks.copy();
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
            fixinsGreatwoodPlanks,
            fixinsGreatwoodPlanks,
            fixinsGreatwoodPlanks,
            fixinsGreatwoodPlanks,
            fixinsGreatwoodPlanks,
            fixinsGreatwoodPlanks,
            fixinsGreatwoodPlanks,
            fixinsGreatwoodPlanks);

        conversionOutput = thaumSilverwoodPlanks.copy();
        conversionOutput.stackSize = 8;
        GameRegistry.addShapelessRecipe(
            conversionOutput,
            fixinsSilverwoodPlanks,
            fixinsSilverwoodPlanks,
            fixinsSilverwoodPlanks,
            fixinsSilverwoodPlanks,
            fixinsSilverwoodPlanks,
            fixinsSilverwoodPlanks,
            fixinsSilverwoodPlanks,
            fixinsSilverwoodPlanks);

        // Greatwood Slabs
        final var greatwoodSlabs = new ItemStack(ConfigBlocks.blockSlabWood, 6, 0);
        registerSlabRecipes(greatwoodSlabs, thaumGreatwoodPlanks, fixinsGreatwoodPlanks);
        GameRegistry.addRecipe(new ShapedOreRecipe(greatwoodSlabs, "PPP", 'P', CustomBlocks.ORE_DICT_GREATWOOD_PLANKS));

        // Silverwood Slabs
        final var silverwoodSlabs = new ItemStack(ConfigBlocks.blockSlabWood, 6, 1);
        registerSlabRecipes(silverwoodSlabs, thaumSilverwoodPlanks, fixinsSilverwoodPlanks);
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

    private void registerSlabRecipes(ItemStack output, ItemStack tcPlanks, ItemStack tfPlanks) {
        // with one Fixins plank
        GameRegistry.addShapedRecipe(output, "CCF", 'C', tcPlanks, 'F', tfPlanks);
        GameRegistry.addShapedRecipe(output, "CFC", 'C', tcPlanks, 'F', tfPlanks);
        GameRegistry.addShapedRecipe(output, "FCC", 'C', tcPlanks, 'F', tfPlanks);

        // with two Fixins planks
        GameRegistry.addShapedRecipe(output, "CFF", 'C', tcPlanks, 'F', tfPlanks);
        GameRegistry.addShapedRecipe(output, "FCF", 'C', tcPlanks, 'F', tfPlanks);

        // only Fixins planks
        GameRegistry.addShapedRecipe(output, "FFF", 'F', tfPlanks);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {}

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        maybeRegister(event, commandsModule.createNode, CreateNodeCommand::new);
        maybeRegister(event, commandsModule.forgetResearch, ForgetResearchCommand::new);
        maybeRegister(event, commandsModule.forgetScanned, ForgetScannedCommand::new);
        maybeRegister(event, commandsModule.help, HelpCommand::new);
        maybeRegister(event, commandsModule.prerequisites, PrerequisitesCommand::new);
        maybeRegister(event, commandsModule.playerResearch, ListResearchCommand::new);
        maybeRegister(event, commandsModule.updateNode, UpdateNodeCommand::new);
    }

    private void maybeRegister(FMLServerStartingEvent event, CommandSettings settings,
        Supplier<FixinsCommandBase<?>> init) {
        if (settings.isEnabled()) {
            event.registerServerCommand(init.get());
        }
    }
}
