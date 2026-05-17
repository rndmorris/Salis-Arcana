package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.research;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.common.lib.research.ResearchManager;

@Mixin(ResearchManager.class)
public class MixinResearchManager_CreativeOPThaumonomicon {

    @WrapMethod(method = "consumeInkFromPlayer", remap = false)
    private static boolean creativeThaumonomiconInkCheck(EntityPlayer player, boolean doit,
        Operation<Boolean> original) {
        return player.capabilities.isCreativeMode || original.call(player, doit);
    }

    @WrapOperation(
        method = "createResearchNoteForPlayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/InventoryPlayer;consumeInventoryItem(Lnet/minecraft/item/Item;)Z"))
    private static boolean creativeThaumonomiconPaperCheck(InventoryPlayer instance, Item p_146026_1_,
        Operation<Boolean> original, @Local(argsOnly = true) EntityPlayer player) {
        return player.capabilities.isCreativeMode || original.call(instance, p_146026_1_);
    }
}
