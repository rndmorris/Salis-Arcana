package dev.rndmorris.salisarcana;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import dev.rndmorris.salisarcana.client.ThaumicInventoryScanner;
import dev.rndmorris.salisarcana.common.commands.CommandExportResearch;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;

public class ClientProxy extends CommonProxy {

    ThaumicInventoryScanner scanner;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        if (ConfigModuleRoot.commands.exportResearch.isEnabled()) {
            ClientCommandHandler.instance.registerCommand(new CommandExportResearch());
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        if (ConfigModuleRoot.enhancements.thaumicInventoryScanning.isEnabled()) {
            scanner = new ThaumicInventoryScanner();
            MinecraftForge.EVENT_BUS.register(scanner);
            FMLCommonHandler.instance()
                .bus()
                .register(scanner);

            scanner.init(event);
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        if (ConfigModuleRoot.enhancements.thaumicInventoryScanning.isEnabled()) {
            scanner.postInit(event);
        }
    }

    @Override
    public boolean isSingleplayerClient() {
        return Minecraft.getMinecraft()
            .isSingleplayer();
    }
}
