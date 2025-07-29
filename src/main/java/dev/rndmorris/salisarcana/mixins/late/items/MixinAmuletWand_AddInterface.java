package dev.rndmorris.salisarcana.mixins.late.items;

import org.spongepowered.asm.mixin.Mixin;

import dev.rndmorris.salisarcana.api.IVisContainer;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(value = { ItemAmuletVis.class, ItemWandCasting.class }, remap = false)
public abstract class MixinAmuletWand_AddInterface implements IVisContainer {
}
