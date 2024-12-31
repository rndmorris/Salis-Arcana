package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import thaumcraft.common.blocks.BlockCustomOreItem;
import thaumcraft.common.items.ItemShard;

@Mixin(value = ItemShard.class, priority = 1001)
public class MixinItemShard extends Item {

    /**
     * @author Midnight
     * @reason Fixes ArrayIndexOutOfBoundsException with invalid shard meta
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int par2) {
        return stack.getItemDamage() > 6 ? 0
            : stack.getItemDamage() == 6 ? super.getColorFromItemStack(stack, par2)
                : BlockCustomOreItem.colors[stack.getItemDamage() + 1];
    }
}
