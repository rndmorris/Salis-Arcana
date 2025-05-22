package dev.rndmorris.salisarcana.mixins.late.blocks;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.blocks.BlockEldritch;

@Mixin(BlockEldritch.class)
public class MixinBlockEldritch_RemoveBrokenDoor {
    @Inject(method = "breakBlock", at = @At("HEAD"))
    public void removeBrokenDoor(World world, int x, int y, int z, Block block, int meta, CallbackInfo ci) {
        if(!world.isRemote && meta == 8) {
        }
    }
}
