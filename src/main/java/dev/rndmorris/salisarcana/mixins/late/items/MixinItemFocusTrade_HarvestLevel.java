package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.api.IArchitect;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.foci.ItemFocusTrade;

@Mixin(value = ItemFocusTrade.class, remap = false)
public abstract class MixinItemFocusTrade_HarvestLevel extends ItemFocusBasic implements IArchitect {

    @Shadow
    abstract protected MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World,
        EntityPlayer par2EntityPlayer);

    @WrapOperation(
        method = "onFocusRightClick",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/items/wands/foci/ItemFocusTrade;getPickedBlock(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack wrapGetPickedBlock(ItemFocusTrade instance, ItemStack stack, Operation<ItemStack> original,
        @Local(name = "world") World world, @Local(name = "player") EntityPlayer player) {
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player);
        final int x = mop.blockX, y = mop.blockY, z = mop.blockZ;
        final var block = world.getBlock(x, y, z);
        final var metadata = world.getBlockMetadata(x, y, z);
        if (block.getHarvestLevel(metadata) <= ConfigModuleRoot.enhancements.equalTradeFocusHarvestLevel.getValue()) {
            return original.call(instance, stack);
        }
        return null;
    }
}
