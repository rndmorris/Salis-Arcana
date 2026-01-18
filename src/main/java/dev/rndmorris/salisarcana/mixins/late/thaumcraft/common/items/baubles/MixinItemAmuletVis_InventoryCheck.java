package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.baubles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.PlayerHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(value = ItemAmuletVis.class, remap = false)
public abstract class MixinItemAmuletVis_InventoryCheck {

    @Shadow
    public abstract int getVis(ItemStack is, Aspect aspect);

    @Shadow
    public abstract void storeVis(ItemStack is, Aspect aspect, int amount);

    @Unique
    private final int sa$transferRate = SalisConfig.features.visAmuletTransferRate.getValue();

    @Unique
    private final int sa$tickRate = SalisConfig.features.visAmuletTickRate.getValue();

    public MixinItemAmuletVis_InventoryCheck() {}

    @WrapMethod(method = "onWornTick")
    private void wrapOnWornTick(ItemStack itemstack, EntityLivingBase entity, Operation<Void> original) {
        if (!entity.worldObj.isRemote && entity.ticksExisted % sa$tickRate == 0
            && entity instanceof EntityPlayer player) {
            ItemStack[] wands = PlayerHelper.getItemsInInventory(player, ItemWandCasting.class);
            for (ItemStack wandStack : wands) {
                // if it's the held item, it'll be taken care of when we call the original
                // we want true equivalence here, so we can't use .equals()
                if (wandStack == player.getHeldItem()) {
                    continue;
                }
                ItemWandCasting wand = (ItemWandCasting) wandStack.getItem();
                // noinspection DataFlowIssue (wand can't actually be null here)
                AspectList al = wand.getAspectsWithRoom(wandStack);
                for (Aspect aspect : al.getAspects()) {
                    if (aspect != null && this.getVis(itemstack, aspect) > 0) {
                        int amt = Math.min(sa$transferRate, wand.getMaxVis(wandStack) - wand.getVis(wandStack, aspect));
                        amt = Math.min(amt, this.getVis(itemstack, aspect));
                        this.storeVis(itemstack, aspect, this.getVis(itemstack, aspect) - amt);
                        wand.storeVis(wandStack, aspect, this.getVis(wandStack, aspect) + amt);
                    }
                }
            }
        }
        original.call(itemstack, entity);
    }
}
