package dev.rndmorris.salisarcana.mixins.late.world;

import java.util.Random;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.lib.RandomHelper;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

@Mixin(value = ThaumcraftWorldGenerator.class, remap = false)
public class MixinThaumcraftWorldGenerator {

    @Inject(
        method = "createRandomNodeAt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getBiomeGenForCoords(II)Lnet/minecraft/world/biome/BiomeGenBase;"))
    private static void mixinCreateRandomNodeAt(World world, int x, int y, int z, Random random, boolean silverwood,
        boolean eerie, boolean small, CallbackInfo ci, @Local LocalRef<NodeType> type,
        @Local LocalRef<NodeModifier> modifier) {
        int rand;

        if (ConfigModuleRoot.enhancements.nodeModifierWeights.isEnabled()) {
            rand = RandomHelper.weightedRandom(random, ConfigModuleRoot.enhancements.nodeModifierWeights.getValue());
            modifier.set(rand == NodeModifier.values().length || rand == -1 ? null : NodeModifier.values()[rand]);
        }
        if (ConfigModuleRoot.enhancements.nodeTypeWeights.isEnabled()) {
            if (!silverwood && !eerie) {
                rand = RandomHelper.weightedRandom(random, ConfigModuleRoot.enhancements.nodeTypeWeights.getValue());
                type.set(rand == NodeType.values().length || rand == -1 ? NodeType.NORMAL : NodeType.values()[rand]);
            }
        }
    }

}
