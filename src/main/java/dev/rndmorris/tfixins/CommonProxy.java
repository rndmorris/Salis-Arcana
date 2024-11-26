package dev.rndmorris.tfixins;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import dev.rndmorris.tfixins.common.biomes.BiomeOverrides;
import dev.rndmorris.tfixins.common.commands.CreateNodeCommand;
import dev.rndmorris.tfixins.common.commands.ForgetResearchCommand;
import dev.rndmorris.tfixins.common.commands.HelpCommand;
import dev.rndmorris.tfixins.common.commands.ListResearchCommand;
import dev.rndmorris.tfixins.common.commands.UpdateNodeCommand;
import dev.rndmorris.tfixins.config.FixinsConfig;

public class CommonProxy {

    public static CreateNodeCommand createNodeCommand = null;
    public static ForgetResearchCommand forgetResearchCommand = null;
    public static HelpCommand helpCommand = null;
    public static ListResearchCommand playerResearchCommand = null;
    public static UpdateNodeCommand updateNodeCommand = null;

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        FixinsConfig.synchronizeConfiguration(event.getSuggestedConfigurationFile());
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
            event.registerServerCommand(createNodeCommand = new CreateNodeCommand());
        }
        if (commands.forgetResearch.isEnabled()) {
            event.registerServerCommand(forgetResearchCommand = new ForgetResearchCommand());
        }
        if (commands.help.isEnabled()) {
            event.registerServerCommand(helpCommand = new HelpCommand());
        }
        if (commands.playerResearch.isEnabled()) {
            event.registerServerCommand(playerResearchCommand = new ListResearchCommand());
        }
        if (commands.updateNode.isEnabled()) {
            event.registerServerCommand(updateNodeCommand = new UpdateNodeCommand());
        }
    }
}
