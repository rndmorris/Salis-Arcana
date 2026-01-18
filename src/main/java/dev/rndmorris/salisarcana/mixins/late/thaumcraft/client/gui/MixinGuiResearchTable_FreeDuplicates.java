package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.gui.GuiResearchTable;

@Mixin(GuiResearchTable.class)
public class MixinGuiResearchTable_FreeDuplicates {

    @Unique
    private static final AspectList sa$noAspects = new AspectList();

    @ModifyArg(
        method = "drawScreen",
        at = @At(value = "INVOKE", target = "Lthaumcraft/client/gui/GuiResearchTable;drawTexturedModalRect(IIIIII)V"),
        index = 4)
    public int changeWidth(int original) {
        return 40;
    }

    @WrapOperation(
        method = "drawScreen",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/api/aspects/AspectList;copy()Lthaumcraft/api/aspects/AspectList;",
            remap = false))
    public AspectList dontCopy(AspectList instance, Operation<AspectList> original) {
        return sa$noAspects;
    }
}
