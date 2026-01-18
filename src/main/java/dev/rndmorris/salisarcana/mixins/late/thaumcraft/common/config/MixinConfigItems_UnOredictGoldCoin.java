package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.config;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.common.config.ConfigItems;

@Mixin(value = ConfigItems.class, remap = false)
public class MixinConfigItems_UnOredictGoldCoin {

    @WrapOperation(
        method = "init",
        at = @At(
            value = "INVOKE",
            ordinal = 24,
            target = "Lnet/minecraftforge/oredict/OreDictionary;registerOre(Ljava/lang/String;Lnet/minecraft/item/ItemStack;)V"))
    private static void onInit(String s, ItemStack itemStack, Operation<Void> original) {
        // intentionally do nothing
    }

}
