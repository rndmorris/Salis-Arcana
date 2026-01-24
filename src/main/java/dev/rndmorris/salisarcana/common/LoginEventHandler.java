package dev.rndmorris.salisarcana.common;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import dev.rndmorris.salisarcana.network.MessageLogin;
import dev.rndmorris.salisarcana.network.NetworkHandler;

public class LoginEventHandler {

    public LoginEventHandler() {
        FMLCommonHandler.instance()
            .bus()
            .register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        NetworkHandler.instance.sendTo(new MessageLogin(), (EntityPlayerMP) event.player);
    }
}
