package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.lib.PlayerHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(ItemAmuletVis.class)
public abstract class MixinItemAmuletVis_InventoryCheck {

    @Shadow
    public abstract int getVis(ItemStack is, Aspect aspect);

    @Shadow
    public abstract void storeVis(ItemStack is, Aspect aspect, int amount);

    @Unique
    private final int sa$transferRate = ConfigModuleRoot.enhancements.visAmuletTransferRate.getValueOrDefault();

    @WrapMethod(method = "onWornTick")
    private void wrapOnWornTick(ItemStack itemstack, EntityLivingBase entity, Operation<Void> original) {
        if (entity instanceof EntityPlayer player) {
            Slot[] slots = PlayerHelper.getItemsInInventory(player, ItemWandCasting.class);
            // only time this will be null is if the player has no wands in their inventory, so we don't need to do a
            // null check below
            ItemWandCasting wand = slots.length > 0 ? (ItemWandCasting) slots[0].getStack()
                .getItem() : null;
            for (Slot slot : slots) {
                // if it's the held item, it'll be taken care of when we call the original
                if (player.inventoryContainer.getSlot(player.inventory.currentItem)
                    .equals(slot)) {
                    continue;
                }
                ItemStack wandStack = slot.getStack();
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
