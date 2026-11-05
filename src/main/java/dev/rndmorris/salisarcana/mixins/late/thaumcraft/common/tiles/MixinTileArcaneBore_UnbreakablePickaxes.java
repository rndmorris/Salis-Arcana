package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.inventory.IInventory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.wands.IWandable;
import thaumcraft.common.tiles.TileArcaneBore;

@Mixin(value = TileArcaneBore.class, remap = false)
abstract class MixinTileArcaneBore_UnbreakablePickaxes extends TileThaumcraft implements IInventory, IWandable {

    @Shadow
    public boolean hasFocus;

    @Shadow
    public boolean hasPickaxe;

    @WrapMethod(method = "areItemsValid")
    private boolean allowUnbreakablePickaxe(Operation<Boolean> original) {
        if (this.hasPickaxe && this.getStackInSlot(1)
            .getMaxDamage() <= 0) { // Some unbreakable tools like those in Thaumic Tinkerer use negative values
            return this.hasFocus;
        }
        return original.call();
    }

}
