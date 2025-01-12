package dev.rndmorris.salisarcana;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import dev.rndmorris.salisarcana.client.ThaumicInventoryScanner;

public class ClientProxy extends CommonProxy {

    ThaumicInventoryScanner scanner = new ThaumicInventoryScanner();

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(scanner);
        FMLCommonHandler.instance()
            .bus()
            .register(scanner);

        scanner.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        scanner.postInit(event);
    }

}
