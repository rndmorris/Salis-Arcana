package dev.rndmorris.salisarcana.mixins.late.events;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import dev.rndmorris.salisarcana.mixins.accessors.IVirtualSwapperAccessor;
import thaumcraft.common.lib.events.ServerTickEventsFML;

@Mixin(ServerTickEventsFML.VirtualSwapper.class)
public abstract class MixinServerTickEventsFML_VirtualSwapper implements IVirtualSwapperAccessor {

    @Shadow(remap = false)
    int lifespan;

    @Shadow(remap = false)
    int x;

    @Shadow(remap = false)
    int y;

    @Shadow(remap = false)
    int z;

    @Shadow(remap = false)
    Block bSource;

    @Shadow(remap = false)
    int mSource;

    @Shadow(remap = false)
    ItemStack target;

    @Shadow(remap = false)
    int wand;

    @Shadow(remap = false)
    EntityPlayer player;

    @Override
    public int sa$getLifespan() {
        return this.lifespan;
    }

    @Override
    public int sa$getX() {
        return x;
    }

    @Override
    public int sa$getY() {
        return y;
    }

    @Override
    public int sa$getZ() {
        return z;
    }

    @Override
    public Block sa$getSourceBlock() {
        return bSource;
    }

    @Override
    public int sa$getSourceMetadata() {
        return mSource;
    }

    @Override
    public ItemStack sa$getTarget() {
        return target;
    }

    @Override
    public int sa$getWand() {
        return wand;
    }

    @Override
    public EntityPlayer sa$getPlayer() {
        return player;
    }
}
