package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import thaumcraft.common.items.ItemLootBag;

@Mixin(value = ItemLootBag.class, remap = false)
public class MixinItemLootBag extends Item {

    @Shadow
    public IIcon[] icon;

    /**
     * @author rndmorris
     * @reason Range check to prevent crashes
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return icon[Math.min(Math.max(damage, 0), icon.length - 1)];
    }
}
