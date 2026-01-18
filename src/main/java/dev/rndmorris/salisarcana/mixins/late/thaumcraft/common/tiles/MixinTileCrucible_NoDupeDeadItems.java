package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.entity.item.EntityItem;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.common.tiles.TileCrucible;

@Mixin(TileCrucible.class)
public abstract class MixinTileCrucible_NoDupeDeadItems {

    @WrapMethod(method = "attemptSmelt", remap = false)
    private void ignoreDeadItems(EntityItem entity, Operation<Void> original) {
        if (!entity.isDead) {
            original.call(entity);
        }
    }
}
