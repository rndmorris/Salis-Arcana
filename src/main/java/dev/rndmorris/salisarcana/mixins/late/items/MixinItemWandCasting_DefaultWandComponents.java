package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import dev.rndmorris.salisarcana.lib.WandHelper;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(value = ItemWandCasting.class, remap = false)
public class MixinItemWandCasting_DefaultWandComponents {

    @WrapMethod(method = "getCap")
    public WandCap defaultCap(ItemStack stack, Operation<WandCap> original) {
        final WandCap result = original.call(stack);
        return result != null ? result : WandHelper.CAP_UNKNOWN;
    }

    @WrapMethod(method = "getRod")
    public WandRod defaultRod(ItemStack stack, Operation<WandRod> original) {
        final WandRod result = original.call(stack);

        if (result != null) {
            return result;
        } else {
            if (stack.stackTagCompound.getString("rod")
                .endsWith("_staff")) {
                return WandHelper.STAFF_UNKNOWN;
            } else {
                return WandHelper.ROD_UNKNOWN;
            }
        }
    }
}
