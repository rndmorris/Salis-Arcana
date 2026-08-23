package dev.rndmorris.salisarcana.mixins.late.thaumcraft.api.visnet;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.api.visnet.TileVisNode;

@Mixin(value = TileVisNode.class, remap = false)
abstract class MixinTileVisNode_onRemoveThisNode extends TileEntity {

    @Inject(method = "invalidate", at = @At("HEAD"), remap = false)
    private void onInvalidate() {
        super.invalidate();
    }

    @WrapOperation(
        method = "removeThisNode",
        at = @At(value = "INVOKE", target = "Lthaumcraft/api/visnet/TileVisNode;parentChanged()V", remap = false))
    private void wrapParentChanged(TileVisNode instance, Operation<Void> original) {
        if (!this.isInvalid()) {
            original.call(instance);
        }
    }

    @WrapOperation(
        method = "removeThisNode",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;markBlockForUpdate(III)V", remap = true))
    private void wrapMarkForUpdate(World w, int x, int y, int z, Operation<Void> original) {
        if (!this.isInvalid()) {
            original.call(w, x, y, z);
        }
    }
}
