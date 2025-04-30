package dev.rndmorris.salisarcana.notifications;

import net.minecraft.util.ChatComponentTranslation;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class StartupNotifications {

    private boolean tcInventoryScanNoticeSent = !Loader.isModLoaded("tcinventoryscan");

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        sendTCInventoryScanNotice(event);
    }

    private void sendTCInventoryScanNotice(PlayerEvent.PlayerLoggedInEvent event) {
        if (!tcInventoryScanNoticeSent) {
            tcInventoryScanNoticeSent = true;
            event.player.addChatMessage(new ChatComponentTranslation("salisarcana:tcinventoryscan_notice"));
        }
    }

}
