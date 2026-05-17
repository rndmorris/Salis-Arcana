package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.rndmorris.salisarcana.lib.ifaces.IFocalManipulatorWithXP;
import thaumcraft.common.container.ContainerFocalManipulator;
import thaumcraft.common.tiles.TileFocalManipulator;

@Mixin(ContainerFocalManipulator.class)
public abstract class MixinContainerFocalManipulator_StoreXP extends Container {

    @Shadow(remap = false)
    private TileFocalManipulator table;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    public void attachPlayer(InventoryPlayer par1InventoryPlayer, TileFocalManipulator tileEntity, CallbackInfo ci) {
        ((IFocalManipulatorWithXP) tileEntity).salisArcana$connectPlayer(par1InventoryPlayer.player);
    }

    @Override
    public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer player) {
        if (slotId == 0) {
            ((IFocalManipulatorWithXP) this.table).salisArcana$transferXpToPlayer(player);
        }
        return super.slotClick(slotId, clickedButton, mode, player);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        ((IFocalManipulatorWithXP) this.table).salisArcana$disconnectPlayer(player);
    }
}
