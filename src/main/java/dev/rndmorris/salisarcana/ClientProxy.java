package dev.rndmorris.salisarcana;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import dev.rndmorris.salisarcana.client.ThaumicInventoryScanner;
import dev.rndmorris.salisarcana.client.handlers.GuiHandler;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;

public class ClientProxy extends CommonProxy {

    ThaumicInventoryScanner scanner;

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
        new GuiHandler();
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
