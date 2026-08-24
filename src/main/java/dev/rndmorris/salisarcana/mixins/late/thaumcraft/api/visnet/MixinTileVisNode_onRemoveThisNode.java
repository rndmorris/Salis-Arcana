package dev.rndmorris.salisarcana.mixins.late.thaumcraft.api.visnet;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;

import thaumcraft.api.visnet.TileVisNode;

@Mixin(value = TileVisNode.class, remap = false)
abstract class MixinTileVisNode_onRemoveThisNode extends TileEntity {

    @Inject(method = "invalidate", at = @At("HEAD"), remap = true)
    private void onInvalidate(CallbackInfo ci) {
        super.invalidate();
    }

    @WrapWithCondition(
        method = "removeThisNode",
        at = @At(value = "INVOKE", target = "Lthaumcraft/api/visnet/TileVisNode;parentChanged()V"))
    private boolean shouldDoParentChanged(TileVisNode instance) {
        return !this.isInvalid();
    }

    @WrapWithCondition(
        method = "removeThisNode",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;markBlockForUpdate(III)V", remap = true))
    private boolean shouldDoMarkForUpdate(World w, int x, int y, int z) {
        return !this.isInvalid();
    }
}
