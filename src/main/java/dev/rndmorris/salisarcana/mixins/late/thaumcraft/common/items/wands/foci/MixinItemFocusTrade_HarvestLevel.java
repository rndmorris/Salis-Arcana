package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands.foci;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.api.IArchitect;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.foci.ItemFocusTrade;

@Mixin(value = ItemFocusTrade.class, remap = false)
public abstract class MixinItemFocusTrade_HarvestLevel extends ItemFocusBasic implements IArchitect {

    @Unique
    private final int sa$harvestLevel = SalisConfig.features.equalTradeFocusHarvestLevel.getValue();

    @Unique
    private final boolean sa$potencyEnabled = SalisConfig.features.potencyModifiesHarvestLevel.isEnabled();

    @Unique
    private long sa$lastPlayedSound;

    @Unique
    private boolean sa$rightClick = false;

    @Shadow
    abstract protected MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World,
        EntityPlayer par2EntityPlayer);

    @Shadow
    public abstract boolean onEntitySwing(EntityLivingBase player, ItemStack stack);

    @WrapMethod(method = "onFocusRightClick")
    public ItemStack wrapOnFocusRightClick(ItemStack itemstack, World world, EntityPlayer player,
        MovingObjectPosition mop, Operation<ItemStack> original) {
        sa$rightClick = true;
        return original.call(itemstack, world, player, mop);
    }

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
        this.onEntitySwing(player, stack);
        return null;
    }

    @WrapOperation(
        method = "onEntitySwing",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/items/wands/foci/ItemFocusTrade;getPickedBlock(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"))
    public ItemStack wrapOnEntitySwing(ItemFocusTrade instance, ItemStack stack, Operation<ItemStack> original,
        @Local(name = "player") EntityLivingBase player) {
        if (this.sa$shouldBreak(stack, (EntityPlayer) player) || (sa$rightClick && player.isSneaking())
        // The itemstack here can be the ItemFocusBasic itself for some unholy reason. Believe it or not, we don't
        // want that.
            || !(stack.getItem() instanceof ItemWandCasting)) {
            sa$rightClick = false;
            return original.call(instance, stack);
        }
        // The sound would get played every tick the button is being held, so instead
        // we play it two times/second
        if (player.isClientWorld()) {
            if (System.currentTimeMillis() - this.sa$lastPlayedSound > 500) {
                this.sa$lastPlayedSound = System.currentTimeMillis();
                player.worldObj
                    .playSoundEffect(player.posX, player.posY, player.posZ, "thaumcraft:craftfail", 1.0F, 1.0F);
            }
        }
        sa$rightClick = false;
        return null;
    }

    /**
     * @param stack  the itemstack
     * @param player the player
     * @return true if the block should be broken (handled by the parent class), false if it should be handled by the
     *         mixin
     */
    @Unique
    public boolean sa$shouldBreak(ItemStack stack, EntityPlayer player) {
        World world = player.worldObj;
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player);
        if (mop == null) {
            return true;
        }
        final int x = mop.blockX, y = mop.blockY, z = mop.blockZ;
        final var block = world.getBlock(x, y, z);
        if (block == null) {
            return true;
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
        return block.getHarvestLevel(metadata) <= harvestLevel;
    }

}
