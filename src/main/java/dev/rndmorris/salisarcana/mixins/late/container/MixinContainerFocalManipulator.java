package dev.rndmorris.salisarcana.mixins.late.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import dev.rndmorris.salisarcana.lib.IFocalManipulatorWithXP;
import thaumcraft.common.container.ContainerFocalManipulator;
import thaumcraft.common.tiles.TileFocalManipulator;

@Mixin(ContainerFocalManipulator.class)
public abstract class MixinContainerFocalManipulator extends Container {

    @Shadow(remap = false)
    private TileFocalManipulator table;

    @ModifyExpressionValue(
        method = "enchantItem",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/tiles/TileFocalManipulator;startCraft(ILnet/minecraft/entity/player/EntityPlayer;)Z",
            remap = false))
    public boolean attachPlayer(boolean craftStarted, EntityPlayer player) {
        if (craftStarted) {
            ((IFocalManipulatorWithXP) this.table).salisArcana$setCraftingOriginator(player);
        }
        return craftStarted;
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
