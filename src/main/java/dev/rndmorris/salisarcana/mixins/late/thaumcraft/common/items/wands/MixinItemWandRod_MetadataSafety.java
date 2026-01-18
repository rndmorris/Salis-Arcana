package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands;

import net.minecraft.util.IIcon;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.common.items.wands.ItemWandRod;

@Mixin(value = ItemWandRod.class, remap = false)
public abstract class MixinItemWandRod_MetadataSafety {

    @Shadow
    public IIcon[] iconWand;

    @Shadow
    public IIcon[] iconStaff;

    @WrapMethod(method = "getIconFromDamage", remap = true)
    private IIcon mixinGetIconFromDamage(int meta, Operation<IIcon> original) {
        if ((meta >= 0 && meta < iconWand.length) || (meta >= 50 && (meta - 50) < iconStaff.length) || meta >= 100) {
            return original.call(meta);
        } else {
            return null;
        }
    }
}
