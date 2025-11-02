package dev.rndmorris.salisarcana.mixins.late.lib.events;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import thaumcraft.common.lib.events.ServerTickEventsFML;

@Mixin(ServerTickEventsFML.class)
public class MixinServerTickEventsFML_EqualTrade {
    @WrapOperation(method = "tickBlockSwap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z"))
    public boolean callOnBlockPlaced(World world, int x, int y, int z, Block block, int meta, int flags, Operation<Boolean> original, @Share("meta") LocalIntRef metaRef) {
        boolean upSolid = world.isSideSolid(x, y + 1, z, ForgeDirection.DOWN);
        boolean downSolid = world.isSideSolid(x, y - 1, z, ForgeDirection.UP);
        boolean southSolid = world.isSideSolid(x, y, z + 1, ForgeDirection.NORTH);
        boolean northSolid = world.isSideSolid(x, y, z - 1, ForgeDirection.SOUTH);
        boolean eastSolid = world.isSideSolid(x + 1, y, z, ForgeDirection.WEST);
        boolean westSolid = world.isSideSolid(x - 1, y, z, ForgeDirection.EAST);

        // Prioritize sides with

        meta = block.onBlockPlaced(world, x, y, z, -1 /* TODO SIDE */, 0f, 0f, 0f, meta);

        metaRef.set(meta);
        return original.call(world, x, y, z, block, meta, flags);
    }

    @WrapOperation(method = "tickBlockSwap", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBlockPlacedBy(Lnet/minecraft/world/World;IIILnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;)V"))
    public void callOnPostBlockPlaced(Block block, World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn, Operation<Void> original, @Share("meta") LocalIntRef metaRef) {
        original.call(block, worldIn, x, y, z, placer, itemIn);
        block.onPostBlockPlaced(worldIn, x, y, z, metaRef.get());
    }

    @Unique
    private int sa$getSidePriority(World world, int x, int y, int z, ForgeDirection dir) {
        final int dx = x - dir.offsetX;
        final int dy = y - dir.offsetY;
        final int dz = z - dir.offsetZ;

        if (world.isSideSolid(dx, dy, dz, dir)) {
            return 4;
        } else if (!world.isAirBlock(dx, dy, dz)) {
            return 2;
        } else {
            return 0;
        }
    }
}
