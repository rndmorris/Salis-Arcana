package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.world;

import java.util.Random;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.RandomHelper;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

@Mixin(value = ThaumcraftWorldGenerator.class, remap = false)
public class MixinThaumcraftWorldGenerator_NodeGenerationWeights {

    @Unique
    private static final NodeModifier[] sa$modifiers = new NodeModifier[] { null, NodeModifier.BRIGHT,
        NodeModifier.PALE, NodeModifier.FADING, };

    @Unique
    private static final NodeType[] sa$types = new NodeType[] { NodeType.NORMAL, NodeType.UNSTABLE, NodeType.DARK,
        NodeType.PURE, NodeType.HUNGRY, };

    @Inject(
        method = "createRandomNodeAt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getBiomeGenForCoords(II)Lnet/minecraft/world/biome/BiomeGenBase;"))
    private static void mixinCreateRandomNodeAt(World world, int x, int y, int z, Random random, boolean silverwood,
        boolean eerie, boolean small, CallbackInfo ci, @Local LocalRef<NodeType> type,
        @Local LocalRef<NodeModifier> modifier) {
        int index;

        if (SalisConfig.features.nodeModifierWeights.isEnabled()) {
            index = RandomHelper.weightedRandom(random, SalisConfig.features.nodeModifierWeights.getValue());
            if (index == -1) {
                modifier.set(null);
            } else {
                modifier.set(sa$modifiers[index]);
            }
        }
        if (SalisConfig.features.nodeTypeWeights.isEnabled()) {
            if (!silverwood && !eerie) {
                index = RandomHelper.weightedRandom(random, SalisConfig.features.nodeTypeWeights.getValue());
                if (index == -1) {
                    type.set(NodeType.NORMAL);
                } else {
                    type.set(sa$types[index]);
                }
            }
        }
    }

}
