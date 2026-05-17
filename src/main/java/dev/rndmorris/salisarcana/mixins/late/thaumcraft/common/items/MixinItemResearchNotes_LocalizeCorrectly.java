package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.common.items.ItemResearchNotes;

@Mixin(ItemResearchNotes.class)
public class MixinItemResearchNotes_LocalizeCorrectly {

    @WrapOperation(
        method = "onItemRightClick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/StatCollector;translateToLocal(Ljava/lang/String;)Ljava/lang/String;"))
    public String doNotLocalizeOnServer(String langKey, Operation<String> original) {
        return langKey;
    }
}
