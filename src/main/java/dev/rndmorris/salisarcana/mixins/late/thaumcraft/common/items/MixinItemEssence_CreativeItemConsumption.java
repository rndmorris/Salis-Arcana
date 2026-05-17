package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import thaumcraft.common.items.ItemEssence;

@Mixin(ItemEssence.class)
public class MixinItemEssence_CreativeItemConsumption {

    @Inject(
        method = "onItemUseFirst",
        at = @At(
            value = "FIELD",
            opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/item/ItemStack;stackSize:I",
            shift = At.Shift.AFTER),
        cancellable = true)
    private void onItemUseFirst(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side,
        float f1, float f2, float f3, CallbackInfoReturnable<Boolean> cir) {
        if (player.capabilities.isCreativeMode) {
            itemstack.stackSize += 1;
            world.playSoundAtEntity(player, "game.neutral.swim", 0.25F, 1.0F);
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
