package dev.rndmorris.salisarcana.notifications;

import net.minecraft.util.ChatComponentTranslation;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;

public class StartupNotifications {

    private boolean noticeSent = false;

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        final var biomeColors = ConfigModuleRoot.biomeColors;
        if (noticeSent || !biomeColors.isEnabled() || biomeColors.acknowledgeDeprecation.isEnabled()) {
            return;
        }
        noticeSent = true;
        event.player.addChatMessage(new ChatComponentTranslation("salisarcana:biome_color_deprecation"));
        event.player.addChatMessage(new ChatComponentTranslation("salisarcana:biome_color_deprecation_suppress"));
    }

}
