package dev.rndmorris.tfixins.mixins.late.world;

import java.util.Random;
import java.util.stream.IntStream;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import dev.rndmorris.tfixins.config.FixinsConfig;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

@Mixin(ThaumcraftWorldGenerator.class)
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

        if (FixinsConfig.tweaksModule.nodeModifierWeights.isEnabled()) {
            rand = tfixins$weightedRandom(random, FixinsConfig.tweaksModule.nodeModifierWeights.getValue());
            modifier.set(rand == NodeType.values().length || rand == -1 ? null : NodeModifier.values()[rand]);
        }
        if (FixinsConfig.tweaksModule.nodeTypeWeights.isEnabled()) {
            if (!silverwood && !eerie) {
                rand = tfixins$weightedRandom(random, FixinsConfig.tweaksModule.nodeTypeWeights.getValue());
                type.set(rand == NodeType.values().length || rand == -1 ? null : NodeType.values()[rand]);
            }
        }
    }

    @Unique
    private static int tfixins$weightedRandom(Random random, int[] weights) {
        int fullWeight = IntStream.of(weights)
            .sum();
        int r = random.nextInt(fullWeight);
        int cumulativeWeight = 0;

        for (int i = 0; i < weights.length; i++) {
            cumulativeWeight += weights[i];
            if (r < cumulativeWeight) { // Check against cumulative weight
                return i;
            }
        }
        return -1;
    }
}
