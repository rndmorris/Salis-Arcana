package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.fx;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.particle.EntityFX;
import net.minecraftforge.event.world.WorldEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.client.fx.ParticleEngine;

@Mixin(value = ParticleEngine.class, remap = false)
public class MixinParticleEngine_FixLeak {

    @Shadow
    private HashMap<Integer, ArrayList<EntityFX>>[] particles;

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.world.isRemote) {
            for (HashMap<Integer, ArrayList<EntityFX>> map : this.particles) {
                map.clear();
            }
        }
    }
}
