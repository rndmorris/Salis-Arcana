package dev.rndmorris.salisarcana.mixins.late.tiles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import thaumcraft.common.tiles.TileEldritchLock;

@Mixin(TileEldritchLock.class)
public class MixinTileEldritchLock_PreciseRemoval {
    @Shadow(remap = false)
    byte facing;

    @WrapOperation(method = "doBossSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlock(III)Lnet/minecraft/block/Block;"), remap = false)
    public Block requireCorrectMetadata(World world, int x, int y, int z, Operation<Block> original) {
        if(world.getBlockMetadata(x, y, z) == 12) {
            return original.call(world, x, y, z);
        } else {
            return null;
        }
    }

    @ModifyConstant(method = "doBossSpawn", constant = {@Constant(intValue = -2, ordinal = 2), @Constant(intValue = 2, ordinal = 4)}, remap = false)
    public int requireFaceZ(int original) {
        return this.facing > 3 ? 0 : original;
    }

    @ModifyConstant(method = "doBossSpawn", constant = {@Constant(intValue = -2, ordinal = 4), @Constant(intValue = 2, ordinal = 6)}, remap = false)
    public int requireFaceX(int original) {
        return this.facing > 3 ? original: 0;
    }
}
