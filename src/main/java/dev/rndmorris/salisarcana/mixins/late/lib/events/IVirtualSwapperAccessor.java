package dev.rndmorris.salisarcana.mixins.late.lib.events;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import thaumcraft.common.lib.events.ServerTickEventsFML;

@Mixin(ServerTickEventsFML.VirtualSwapper.class)
public interface IVirtualSwapperAccessor {

    @Accessor
    Block getBSource();

    @Accessor
    int getMSource();

    @Accessor
    EntityPlayer getPlayer();
    
}
