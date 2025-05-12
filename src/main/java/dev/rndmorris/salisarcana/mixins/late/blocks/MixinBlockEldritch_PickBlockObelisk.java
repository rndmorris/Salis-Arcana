package dev.rndmorris.salisarcana.mixins.late.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.blocks.BlockEldritch;

@Mixin(BlockEldritch.class)
public abstract class MixinBlockEldritch_PickBlockObelisk extends BlockContainer {
    protected MixinBlockEldritch_PickBlockObelisk(Material p_i45386_1_) {
        super(p_i45386_1_);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        final ItemStack itemStack = super.getPickBlock(target, world, x, y, z);

        if(itemStack.getItemDamage() == 2) itemStack.setItemDamage(1);

        return itemStack;
    }
}
