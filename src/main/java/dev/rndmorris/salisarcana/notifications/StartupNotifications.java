package dev.rndmorris.salisarcana.notifications;

import java.util.ArrayList;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class StartupNotifications {

    private static final ArrayList<IChatComponent> queue = new ArrayList<>();

    static {
        if (Loader.isModLoaded("tcinventoryscan")) {
            queue.add(new ChatComponentTranslation("salisarcana:tcinventoryscan_notice"));
        }
    }

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        for (IChatComponent message : queue) {
            event.player.addChatMessage(message);
        }
        queue.clear();
    }

    /**
     * Queues a one-time message to be sent to players when they log in.
     * Should be used for startup notifications for things like mod compatibility, config errors, etc.
     *
     * @param message The message to queue.
     */
    public static void queueMessage(IChatComponent message) {
        queue.add(message);
    }
}
