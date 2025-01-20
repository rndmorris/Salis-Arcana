package dev.rndmorris.salisarcana.notifications;

import net.minecraft.util.ChatComponentTranslation;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;

public class StartupNotifications {

    private boolean deprecationNoticeSent = false;

    private boolean tcInventoryScanNoticeSent = !Loader.isModLoaded("tcinventoryscan");

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        sendDeprecationNotice(event);
        sendTCInventoryScanNotice(event);
    }

    private void sendDeprecationNotice(PlayerEvent.PlayerLoggedInEvent event) {
        final var biomeColors = ConfigModuleRoot.biomeColors;
        if (!deprecationNoticeSent && biomeColors.isEnabled() && !biomeColors.acknowledgeDeprecation.isEnabled()) {
            deprecationNoticeSent = true;
            event.player.addChatMessage(new ChatComponentTranslation("salisarcana:biome_color_deprecation"));
            event.player.addChatMessage(new ChatComponentTranslation("salisarcana:biome_color_deprecation_suppress"));
        }
    }

    private void sendTCInventoryScanNotice(PlayerEvent.PlayerLoggedInEvent event) {
        if (!tcInventoryScanNoticeSent) {
            tcInventoryScanNoticeSent = true;
            event.player.addChatMessage(new ChatComponentTranslation("salisarcana:tcinventoryscan_notice"));
        }
    }

}
