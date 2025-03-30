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
        final int x = mop.blockX, y = mop.blockY, z = mop.blockZ;
        final var block = world.getBlock(x, y, z);
        final var metadata = world.getBlockMetadata(x, y, z);
        int harvestLevel = ConfigModuleRoot.enhancements.equalTradeFocusHarvestLevel.getValue();
        int modifiedHarvestLevel = harvestLevel;
        if (ConfigModuleRoot.enhancements.potencyModifiesHarvestLevel.isEnabled()) {
            ItemWandCasting wandCasting = (ItemWandCasting) stack.getItem();
            @SuppressWarnings("DataFlowIssue") // idea doesn't know that wandCasting.getFocusItem(stack) can't be null
            ItemStack focus = wandCasting.getFocusItem(stack);
            modifiedHarvestLevel += this.getUpgradeLevel(focus, FocusUpgradeType.potency);
        }
        return (harvestLevel < 0 || block.getHarvestLevel(metadata) <= modifiedHarvestLevel);
    }
}
