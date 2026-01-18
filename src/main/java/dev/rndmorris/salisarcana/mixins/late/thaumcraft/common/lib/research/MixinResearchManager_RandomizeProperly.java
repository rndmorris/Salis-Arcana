package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.research;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.common.lib.research.ResearchManager;

@Mixin(value = ResearchManager.class, remap = false)
public class MixinResearchManager_RandomizeProperly {

    @WrapOperation(method = "findHiddenResearch", at = @At(value = "NEW", target = "(J)Ljava/util/Random;"))
    private static Random useWorldRandom(long seed, Operation<Random> original, EntityPlayer player) {
        return player.worldObj.rand;
    }
}
