package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.baubles;

import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.common.items.baubles.ItemAmuletRunic;
import thaumcraft.common.items.baubles.ItemGirdleRunic;
import thaumcraft.common.items.baubles.ItemRingRunic;

@Mixin(value = { ItemAmuletRunic.class, ItemGirdleRunic.class, ItemRingRunic.class })
public abstract class MixinBaubles_ItemIconFix extends Item {

    // explicitly remap = false otherwise we get a runtime InvalidMixinException
    @Shadow(remap = false)
    public IIcon[] icon;

    @WrapMethod(method = "getIconFromDamage")
    private IIcon mixinGetIconFromDamage(int meta, Operation<IIcon> original) {
        if (0 <= meta && meta < icon.length) {
            return original.call(meta);
        } else {
            return null;
        }
    }

}
