package dev.rndmorris.salisarcana;

import static dev.rndmorris.salisarcana.config.ConfigModuleRoot.commands;

import java.util.function.Supplier;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import dev.rndmorris.salisarcana.common.blocks.CustomBlocks;
import dev.rndmorris.salisarcana.common.commands.ArcanaCommandBase;
import dev.rndmorris.salisarcana.common.commands.CreateNodeCommand;
import dev.rndmorris.salisarcana.common.commands.ForgetResearchCommand;
import dev.rndmorris.salisarcana.common.commands.ForgetScannedCommand;
import dev.rndmorris.salisarcana.common.commands.HelpCommand;
import dev.rndmorris.salisarcana.common.commands.InfusionSymmetryCommand;
import dev.rndmorris.salisarcana.common.commands.ListResearchCommand;
import dev.rndmorris.salisarcana.common.commands.PrerequisitesCommand;
import dev.rndmorris.salisarcana.common.commands.UpdateNodeCommand;
import dev.rndmorris.salisarcana.common.recipes.CustomRecipes;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.settings.CommandSettings;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)

    public void preInit(FMLPreInitializationEvent event) {
        ConfigModuleRoot.synchronizeConfiguration(ConfigPhase.LATE);

        CustomBlocks.registerBlocks();
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {

        CustomRecipes.registerRecipes();
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        maybeRegister(event, commands.createNode, CreateNodeCommand::new);
        maybeRegister(event, commands.forgetResearch, ForgetResearchCommand::new);
        maybeRegister(event, commands.forgetScanned, ForgetScannedCommand::new);
        maybeRegister(event, commands.help, HelpCommand::new);
        maybeRegister(event, commands.infusionSymmetry, InfusionSymmetryCommand::new);
        maybeRegister(event, commands.prerequisites, PrerequisitesCommand::new);
        maybeRegister(event, commands.playerResearch, ListResearchCommand::new);
        maybeRegister(event, commands.updateNode, UpdateNodeCommand::new);
    }

    private void maybeRegister(FMLServerStartingEvent event, CommandSettings settings,
        Supplier<ArcanaCommandBase<?>> init) {
        if (settings.isEnabled()) {
            event.registerServerCommand(init.get());
        }
    }
}
