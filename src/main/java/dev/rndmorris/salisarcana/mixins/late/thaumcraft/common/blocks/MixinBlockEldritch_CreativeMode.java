package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import thaumcraft.common.blocks.BlockEldritch;

@Mixin(BlockEldritch.class)
public class MixinBlockEldritch_CreativeMode {

    @Inject(
        method = "onBlockActivated",
        at = @At(value = "INVOKE", target = "Lthaumcraft/common/tiles/TileEldritchAltar;markDirty()V"))
    private void onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7,
        float par8, float par9, CallbackInfoReturnable<Boolean> cir) {
        if (player.capabilities.isCreativeMode) {
            player.getHeldItem().stackSize += 1;
        }
    }
}
