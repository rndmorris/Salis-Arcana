package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.world;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import thaumcraft.common.lib.world.WorldGenBigMagicTree;
import thaumcraft.common.lib.world.WorldGenGreatwoodTrees;
import thaumcraft.common.lib.world.WorldGenSilverwoodTreesOld;

@Mixin({ WorldGenBigMagicTree.class, WorldGenGreatwoodTrees.class, WorldGenSilverwoodTreesOld.class })
public abstract class MixinGenTrees_FixLeak {

    @Shadow(remap = false)
    World worldObj;

    @Inject(method = "generate(Lnet/minecraft/world/World;Ljava/util/Random;III)Z", at = @At("RETURN"))
    private void clearWorldRef(CallbackInfoReturnable<Boolean> cir) {
        this.worldObj = null;
    }
}
