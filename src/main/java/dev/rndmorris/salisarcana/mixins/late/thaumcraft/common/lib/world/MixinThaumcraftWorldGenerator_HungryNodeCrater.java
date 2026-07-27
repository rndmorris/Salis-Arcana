package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.world;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

@Mixin(value = ThaumcraftWorldGenerator.class, remap = false)
abstract class MixinThaumcraftWorldGenerator_HungryNodeCrater {

    /**
     * @author koolkrafter5
     * @reason Create a small crater under hungry nodes when they generate to make them more obvious.
     */
    @Inject(method = "createNodeAt", at = @At(value = "TAIL"))
    private static void mixinCreateRandomNodeAt(World world, int x, int y, int z, NodeType nt, NodeModifier nm,
        AspectList al, CallbackInfo ci) {
        if (nt != NodeType.HUNGRY) return;
        int max = 100;
        if (nm != null && SalisConfig.thaum.hungryModifierSpeed.isEnabled()) {
            switch (nm) {
                case BRIGHT -> max = 120;
                case PALE -> max = 80;
                case FADING -> max = 50;
            }
        }
        Vec3 src = Vec3.createVectorHelper(x + 0.5D, y + 0.5D, z + 0.5D);
        for (int i = 0; i < max; i++) {
            int tx = x + world.rand.nextInt(16) - world.rand.nextInt(16);
            int ty = y + world.rand.nextInt(16) - world.rand.nextInt(16);
            int tz = z + world.rand.nextInt(16) - world.rand.nextInt(16);
            if (ty > world.getHeightValue(tx, tz)) {
                ty = world.getHeightValue(tx, tz);
            }
            Vec3 dest = Vec3.createVectorHelper(tx + 0.5D, ty + 0.5D, tz + 0.5D);
            MovingObjectPosition mop = ThaumcraftApiHelper.rayTraceIgnoringSource(world, src, dest, true, false, false);
            if (mop == null) continue;
            double distance = salisarcana$getSquaredDistance(tx, ty, tz, mop.blockX, mop.blockY, mop.blockZ);
            if (distance == 0 || distance >= 256.0) continue;
            tx = mop.blockX;
            ty = mop.blockY;
            tz = mop.blockZ;
            Block block = world.getBlock(tx, ty, tz);
            if (block.isAir(world, tx, ty, tz) || (x == tx && y == ty && z == tz)) continue;
            float h = block.getBlockHardness(world, tx, ty, tz);
            if (h >= 0.0F && h < 5.0F) {
                world.setBlock(tx, ty, tz, Blocks.air);
            }
        }
    }

    @Unique
    private static double salisarcana$getSquaredDistance(double x1, double y1, double z1, double x2, double y2,
        double z2) {
        double dx = x1 + 0.5D - x2;
        double dy = y1 + 0.5D - y2;
        double dz = z1 + 0.5D - z2;
        return dx * dx + dy * dy + dz * dz;
    }

}
