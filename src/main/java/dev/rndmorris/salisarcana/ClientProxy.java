package dev.rndmorris.salisarcana;

import net.minecraft.client.Minecraft;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import dev.rndmorris.salisarcana.client.ThaumicInventoryScanner;
import dev.rndmorris.salisarcana.client.handlers.GuiHandler;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.WandPartTooltipEventHandler;

public class ClientProxy extends CommonProxy {

    ThaumicInventoryScanner scanner;

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        if (SalisConfig.features.thaumicInventoryScanning.isEnabled()) {
            scanner = new ThaumicInventoryScanner();
            MinecraftForge.EVENT_BUS.register(scanner);
            FMLCommonHandler.instance()
                .bus()
                .register(scanner);

            scanner.init(event);
        }
        if (SalisConfig.features.wandPartStatsTooltip.isEnabled()) {
            MinecraftForge.EVENT_BUS.register(new WandPartTooltipEventHandler());
        }
        new GuiHandler();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        if (SalisConfig.features.thaumicInventoryScanning.isEnabled()) {
            scanner.postInit(event);
        }
    }

    @Override
    public boolean isSingleplayerClient() {
        return Minecraft.getMinecraft()
            .isSingleplayer();
    }

    @Override
    public World getFakePlayerWorld() {
        if (Minecraft.getMinecraft()
            .isSingleplayer()) {
            return MinecraftServer.getServer()
                .worldServerForDimension(0);
        } else {
            return Minecraft.getMinecraft().theWorld;
        }
    }

    @Override
    public Profiler getProfiler() {
        return Minecraft.getMinecraft().mcProfiler;
    }
}
