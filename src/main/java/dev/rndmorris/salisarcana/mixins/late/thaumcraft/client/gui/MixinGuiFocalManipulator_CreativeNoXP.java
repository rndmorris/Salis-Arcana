package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.client.gui.GuiFocalManipulator;

@Mixin(GuiFocalManipulator.class)
public abstract class MixinGuiFocalManipulator_CreativeNoXP extends GuiContainer {

    public MixinGuiFocalManipulator_CreativeNoXP(Container p_i1072_1_) {
        super(p_i1072_1_);
    }

    @ModifyExpressionValue(
        method = { "drawScreen", "drawGuiContainerBackgroundLayer", "mouseClicked" },
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityClientPlayerMP;experienceLevel:I"))
    public int replaceXpLevel(int original) {
        return this.mc.thePlayer.capabilities.isCreativeMode ? Integer.MAX_VALUE : original;
    }
}
