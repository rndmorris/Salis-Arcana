package dev.rndmorris.salisarcana.mixins.late.tc4tweaks;

import net.glease.tc4tweak.ClientProxy;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = ClientProxy.class, remap = false)
public class MixinClientProxy {

    @ModifyConstant(
        method = "handleMouseInput(Lthaumcraft/client/gui/GuiResearchRecipe;)V",
        constant = @Constant(intValue = 1, ordinal = 2))
    private static int handleMouseInput(int value) {
        return 0;
    }
}
