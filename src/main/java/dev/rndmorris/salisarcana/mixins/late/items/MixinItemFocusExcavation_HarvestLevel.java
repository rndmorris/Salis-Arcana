package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.common.items.wands.foci.ItemFocusExcavation;

@Mixin(ItemFocusExcavation.class)
public class MixinItemFocusExcavation_HarvestLevel {

    @WrapMethod(method = "onFocusRightClick", remap = false)
    public ItemStack wrapOnFocusRightClick(ItemStack itemstack, World world, EntityPlayer p, MovingObjectPosition mop,
        Operation<ItemStack> original) {
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            Block block = p.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ);
            int metadata = p.worldObj.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
            int requiredLevel = block.getHarvestLevel(metadata);
            int harvestLevel = ConfigModuleRoot.enhancements.excavationFocusHarvestLevel.getValue();
            if (harvestLevel < 0 || harvestLevel >= requiredLevel) {
                return original.call(itemstack, world, p, mop);
            }
        }
        return itemstack;
    }
}
