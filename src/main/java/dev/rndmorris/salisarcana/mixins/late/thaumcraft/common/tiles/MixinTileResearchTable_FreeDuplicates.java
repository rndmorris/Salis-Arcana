package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.tiles.TileResearchTable;

@Mixin(TileResearchTable.class)
public class MixinTileResearchTable_FreeDuplicates {

    @WrapOperation(
        method = "duplicate",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/api/aspects/AspectList;getAspects()[Lthaumcraft/api/aspects/Aspect;",
            remap = false),
        remap = false)
    public Aspect[] makeItFree(AspectList instance, Operation<Aspect[]> original) {
        return new Aspect[] {};
    }
}
