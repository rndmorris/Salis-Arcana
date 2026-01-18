package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.equipment;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.common.items.equipment.ItemPrimalCrusher;

@Mixin(value = ItemPrimalCrusher.class, remap = false)
public abstract class PrimalCrusher_StoneOredictCompat {

    @Unique
    private int sa$stoneOreId = -1;
    @Unique
    private int sa$cobblestoneOreId = -1;
    @Unique
    private int sa$stoneBrickOreId = -1;

    @WrapOperation(
        method = "onBlockDestroyed",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/items/equipment/ItemPrimalCrusher;isEffectiveAgainst(Lnet/minecraft/block/Block;)Z",
            remap = false),
        remap = true)
    private boolean wrapIsEffectiveAgainst(ItemPrimalCrusher instance, Block block, Operation<Boolean> original,
        @Local(name = "md") int metadata) {
        if (original.call(instance, block)) {
            return true;
        }
        if (sa$stoneOreId == -1) {
            sa$stoneOreId = OreDictionary.getOreID("stone");
            sa$cobblestoneOreId = OreDictionary.getOreID("cobblestone");
            sa$stoneBrickOreId = OreDictionary.getOreID("stoneBricks");
        }
        final int[] oreIds = OreDictionary.getOreIDs(new ItemStack(block, 1, metadata));
        for (final var oreId : oreIds) {
            if (oreId == sa$stoneOreId || oreId == sa$cobblestoneOreId || oreId == sa$stoneBrickOreId) {
                return true;
            }
        }
        return false;
    }
}
