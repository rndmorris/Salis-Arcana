package dev.rndmorris.salisarcana.mixins.late.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.blocks.BlockEldritch;

@Mixin(BlockEldritch.class)
public abstract class MixinBlockEldritch_PlaceFullObelisk extends BlockContainer {
    protected MixinBlockEldritch_PlaceFullObelisk(Material p_i45386_1_) {
        super(p_i45386_1_);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z) {
        return super.canPlaceBlockAt(worldIn, x, y, z)
            && super.canPlaceBlockAt(worldIn, x, y + 1, z)
            && super.canPlaceBlockAt(worldIn, x, y + 2, z)
            && super.canPlaceBlockAt(worldIn, x, y + 3, z)
            && super.canPlaceBlockAt(worldIn, x, y + 4, z);
    }

    @Override
    public void onPostBlockPlaced(World worldIn, int x, int y, int z, int meta) {
        worldIn.setBlock(x, y + 1, z, this, 2, 3);
        worldIn.setBlock(x, y + 2, z, this, 2, 3);
        worldIn.setBlock(x, y + 3, z, this, 2, 3);
        worldIn.setBlock(x, y + 4, z, this, 2, 3);
    }
}
