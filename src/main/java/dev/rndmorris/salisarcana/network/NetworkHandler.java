package dev.rndmorris.salisarcana.network;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import dev.rndmorris.salisarcana.SalisArcana;

public class NetworkHandler {

    public static final SimpleNetworkWrapper instance = new SimpleNetworkWrapper(SalisArcana.MODID);

    public static void init() {
        // long message type names make spotless weird
        // spotless:off
        instance.registerMessage(MessageScanSlot.class, MessageScanSlot.class, 0, Side.SERVER);
        instance.registerMessage(MessageScanSelf.class, MessageScanSelf.class, 1, Side.SERVER);
        instance.registerMessage(MessageScanContainer.class, MessageScanContainer.class, 2, Side.SERVER);
        instance.registerMessage(MessageScanIInventory.class, MessageScanIInventory.class, 3, Side.SERVER);
        instance.registerMessage(MessageDuplicateResearch.class, MessageDuplicateResearch.class, 4, Side.SERVER);
        instance.registerMessage(MessageForgetResearch.class, MessageForgetResearch.class, 6, Side.CLIENT);
        instance.registerMessage(MessageForgetScannedObjects.class, MessageForgetScannedObjects.class, 7, Side.CLIENT);
        instance.registerMessage(MessageForgetScannedCategory.class, MessageForgetScannedCategory.class, 8, Side.CLIENT);
        // spotless:on
    }
}
