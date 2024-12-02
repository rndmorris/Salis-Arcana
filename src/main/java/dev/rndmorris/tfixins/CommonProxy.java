package dev.rndmorris.tfixins;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import dev.rndmorris.tfixins.common.biomes.BiomeOverrides;
import dev.rndmorris.tfixins.common.commands.CreateNodeCommand;
import dev.rndmorris.tfixins.common.commands.ForgetResearchCommand;
import dev.rndmorris.tfixins.common.commands.ForgetScannedCommand;
import dev.rndmorris.tfixins.common.commands.HelpCommand;
import dev.rndmorris.tfixins.common.commands.ListResearchCommand;
import dev.rndmorris.tfixins.common.commands.UpdateNodeCommand;
import dev.rndmorris.tfixins.config.FixinsConfig;

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
        final var commands = FixinsConfig.commandsModule;
        if (commands.createNode.isEnabled()) {
            event.registerServerCommand(new CreateNodeCommand());
        }
        if (commands.forgetResearch.isEnabled()) {
            event.registerServerCommand(new ForgetResearchCommand());
        }
        if (commands.forgetScanned.isEnabled()) {
            event.registerServerCommand(new ForgetScannedCommand());
        }
        if (commands.help.isEnabled()) {
            event.registerServerCommand(new HelpCommand());
        }
        if (commands.playerResearch.isEnabled()) {
            event.registerServerCommand(new ListResearchCommand());
        }
        if (commands.updateNode.isEnabled()) {
            event.registerServerCommand(new UpdateNodeCommand());
        }
    }
}
