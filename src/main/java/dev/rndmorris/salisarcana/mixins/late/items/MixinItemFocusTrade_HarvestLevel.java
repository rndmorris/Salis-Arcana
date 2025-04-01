package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.api.IArchitect;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.foci.ItemFocusTrade;

@Mixin(value = ItemFocusTrade.class, remap = false)
public abstract class MixinItemFocusTrade_HarvestLevel extends ItemFocusBasic implements IArchitect {

    @Unique
    private final int sa$harvestLevel = ConfigModuleRoot.enhancements.equalTradeFocusHarvestLevel.getValue();

    @Unique
    private final boolean sa$potencyEnabled = ConfigModuleRoot.enhancements.potencyModifiesHarvestLevel.isEnabled();

    @Shadow
    abstract protected MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World,
        EntityPlayer par2EntityPlayer);

    @WrapOperation(
        method = "onFocusRightClick",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/items/wands/foci/ItemFocusTrade;getPickedBlock(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack wrapGetPickedBlock(ItemFocusTrade instance, ItemStack stack, Operation<ItemStack> original,
        @Local(name = "player") EntityPlayer player) {
        if (this.sa$shouldBreak(stack, player)) {
            return original.call(instance, stack);
        }
        player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "thaumcraft:craftfail", 1.0F, 1.0F);
        return null;
    }

    @WrapOperation(
        method = "onEntitySwing",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/items/wands/foci/ItemFocusTrade;getPickedBlock(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"))
    public ItemStack wrapOnEntitySwing(ItemFocusTrade instance, ItemStack stack, Operation<ItemStack> original,
        @Local(name = "player") EntityLivingBase player) {
        if (this.sa$shouldBreak(stack, (EntityPlayer) player)) {
            return original.call(instance, stack);
        }
        player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "thaumcraft:craftfail", 1.0F, 1.0F);
        return null;
    }

    @Unique
    public boolean sa$shouldBreak(ItemStack stack, EntityPlayer player) {
        World world = player.worldObj;
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player);
        if (mop == null) {
            return false;
        }
        final int x = mop.blockX, y = mop.blockY, z = mop.blockZ;
        final var block = world.getBlock(x, y, z);
        if (block == null) {
            return false;
        }
        final var metadata = world.getBlockMetadata(x, y, z);
        if (block.hasTileEntity(metadata)) {
            return false;
        }
        int harvestLevel = this.sa$harvestLevel;
        if (this.sa$potencyEnabled) {
            if (stack.getItem() instanceof ItemWandCasting wand) {
                ItemStack focus = wand.getFocusItem(stack);
                harvestLevel += this.getUpgradeLevel(focus, FocusUpgradeType.potency);
            } else {
                return false;
            }
        }
        return (block.getHarvestLevel(metadata) <= harvestLevel);
    }

}
