package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import thaumcraft.common.items.ItemLootBag;
import thaumcraft.common.items.ItemNugget;
import thaumcraft.common.items.ItemResource;
import thaumcraft.common.items.baubles.ItemAmuletRunic;
import thaumcraft.common.items.baubles.ItemGirdleRunic;
import thaumcraft.common.items.baubles.ItemRingRunic;
import thaumcraft.common.items.equipment.ItemPrimalArrow;
import thaumcraft.common.items.wands.ItemWandCap;

@Mixin(
    value = { ItemLootBag.class, ItemNugget.class, ItemResource.class, ItemAmuletRunic.class, ItemGirdleRunic.class,
        ItemRingRunic.class, ItemPrimalArrow.class, ItemWandCap.class })
public abstract class Mixin_ItemIconFix extends Item {

    // explicitly remap = false otherwise we get a runtime InvalidMixinException
    @Shadow(remap = false)
    public IIcon[] icon;

    @SideOnly(Side.CLIENT)
    @Inject(method = "getIconFromDamage", at = @At("HEAD"), cancellable = true)
    private void mixinGetIconFromDamage(int meta, CallbackInfoReturnable<IIcon> cir) {
        if (0 <= meta && meta < icon.length) {
            cir.setReturnValue(icon[meta]);
        } else {
            cir.setReturnValue(null);
        }
        cir.cancel();
    }

}
