package dev.rndmorris.salisarcana;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;

@Mod(
    modid = SalisArcana.MODID,
    version = Tags.VERSION,
    name = "Salis Arcana",
    acceptedMinecraftVersions = "[1.7.10]",
    dependencies = "required-after:Thaumcraft;after:tc4tweak@[1.5.32,);after:gtnhtcwands")
public class SalisArcana {

    public static final String MODID = "salisarcana";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @SidedProxy(
        clientSide = "dev.rndmorris.salisarcana.ClientProxy",
        serverSide = "dev.rndmorris.salisarcana.CommonProxy")
    public static CommonProxy proxy;
    public static boolean isServerSideInstalled;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    @Mod.EventHandler
    public void onServerStopped(FMLServerStoppedEvent event) {
        proxy.onServerStopped(event);
    }

    @NetworkCheckHandler
    public boolean checkNetwork(Map<String, String> map, Side side) {
        if (side == Side.SERVER) {
            isServerSideInstalled = map.containsKey(MODID);
        }
        return true;
    }
}
