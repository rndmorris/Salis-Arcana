package dev.rndmorris.salisarcana.mixins.late.thaumcraft.api.visnet;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.api.visnet.TileVisNode;

@Mixin(TileVisNode.class)
public class MixinTileVisNode_onRemoveThisNode {

    private static final ThreadLocal<Boolean> isRemoving = ThreadLocal.withInitial(() -> false);

    @WrapMethod(method = "removeThisNode", remap = false)
    private void guardCircularCall(Operation<Void> original) {
        if (isRemoving.get()) {
            return;
        }
        isRemoving.set(true);
        try {
            original.call();
        } finally {
            isRemoving.remove();
        }
    }
}
