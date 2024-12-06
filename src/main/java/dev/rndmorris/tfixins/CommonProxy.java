package dev.rndmorris.tfixins;

import static dev.rndmorris.tfixins.config.FixinsConfig.commandsModule;

import java.util.function.Supplier;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import dev.rndmorris.tfixins.common.biomes.BiomeOverrides;
import dev.rndmorris.tfixins.common.commands.CreateNodeCommand;
import dev.rndmorris.tfixins.common.commands.FixinsCommandBase;
import dev.rndmorris.tfixins.common.commands.ForgetResearchCommand;
import dev.rndmorris.tfixins.common.commands.ForgetScannedCommand;
import dev.rndmorris.tfixins.common.commands.HelpCommand;
import dev.rndmorris.tfixins.common.commands.ListResearchCommand;
import dev.rndmorris.tfixins.common.commands.PrerequisitesCommand;
import dev.rndmorris.tfixins.common.commands.UpdateNodeCommand;
import dev.rndmorris.tfixins.config.commands.CommandSettings;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        BiomeOverrides.apply();
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
