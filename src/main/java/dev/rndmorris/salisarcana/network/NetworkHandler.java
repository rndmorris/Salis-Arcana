package dev.rndmorris.salisarcana.network;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import dev.rndmorris.salisarcana.SalisArcana;

public class NetworkHandler {

    public static final SimpleNetworkWrapper instance = new SimpleNetworkWrapper(SalisArcana.MODID);

    public static void init() {
        instance.registerMessage(HandlerScanSlot.class, MessageScanSlot.class, 0, Side.SERVER);
        instance.registerMessage(HandlerScanSelf.class, MessageScanSelf.class, 1, Side.SERVER);
        instance.registerMessage(HandlerScanContainer.class, MessageScanContainer.class, 2, Side.SERVER);
    }
}
