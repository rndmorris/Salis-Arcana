package dev.rndmorris.salisarcana.mixins.late.tiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.api.NbtUtilities;
import thaumcraft.common.tiles.TileNode;

@Mixin(value = TileNode.class, remap = false)
public abstract class MixinTileNode_SuppressAspectsTag {

    @ModifyExpressionValue(
        method = "handleHungryNodeFirst",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isEntityInvulnerable()Z"))
    private boolean protectItems(boolean original, @Local(name = "eo") Entity entity) {
        if (!(entity instanceof EntityItem item)) {
            return original;
        }
        // if the entity is already invulnerable or has the tag, it should be immune to the hungry node's damage
        return original || NbtUtilities.hasSuppressAspectsTag(item.getEntityItem());
    }
}
