package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import dev.rndmorris.salisarcana.lib.WandHelper;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(value = ItemWandCasting.class, remap = false)
public abstract class MixinItemWandCasting_DefaultWandComponents {

    @Shadow
    public abstract WandCap getCap(ItemStack stack);

    @Shadow
    public abstract WandRod getRod(ItemStack stack);

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

    @Inject(method = "addInformation", at = @At("HEAD"), remap = true)
    private void addInvalidPartMessage(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced,
        CallbackInfo ci) {
        final var cap = this.getCap(stack);
        final var rod = this.getRod(stack);
        if (cap == WandHelper.CAP_UNKNOWN || rod == WandHelper.ROD_UNKNOWN || rod == WandHelper.STAFF_UNKNOWN) {
            tooltip.add(StatCollector.translateToLocal("salisarcana:unknown_wand.craft"));
        }
    }
}
