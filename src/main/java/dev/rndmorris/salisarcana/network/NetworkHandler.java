package dev.rndmorris.salisarcana.network;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import dev.rndmorris.salisarcana.SalisArcana;

public class NetworkHandler {

    public static final SimpleNetworkWrapper instance = new SimpleNetworkWrapper(SalisArcana.MODID);

    public static void init() {
        instance.registerMessage(MessageScanContainer.class, MessageScanContainer.class, 2, Side.SERVER);
        instance.registerMessage(MessageScanIInventory.class, MessageScanIInventory.class, 3, Side.SERVER);
        instance.registerMessage(MessageDuplicateResearch.class, MessageDuplicateResearch.class, 4, Side.SERVER);
        instance.registerMessage(MessageForgetResearch.class, MessageForgetResearch.class, 6, Side.CLIENT);
    }
}
