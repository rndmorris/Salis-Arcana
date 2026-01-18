package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import thaumcraft.common.blocks.BlockWoodenDevice;

@Mixin(BlockWoodenDevice.class)
public class MixinBlockWoodenDevice_LocalizableText {

    @ModifyConstant(
        method = "onBlockActivated",
        constant = @Constant(stringValue = "It will now trigger on everything."))
    public String replaceKeyEveryone(String original) {
        return "salisarcana:misc.arcane_pressure_plate.everyone";
    }

    @ModifyConstant(
        method = "onBlockActivated",
        constant = @Constant(stringValue = "It will now trigger on everything except you."))
    public String replaceKeyNotYou(String original) {
        return "salisarcana:misc.arcane_pressure_plate.not_you";
    }

    @ModifyConstant(method = "onBlockActivated", constant = @Constant(stringValue = "It will now trigger on just you."))
    public String replaceKeyOnlyYou(String original) {
        return "salisarcana:misc.arcane_pressure_plate.only_you";
    }
}
