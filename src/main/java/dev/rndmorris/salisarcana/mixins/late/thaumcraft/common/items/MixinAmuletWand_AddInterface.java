package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items;

import org.spongepowered.asm.mixin.Mixin;

import dev.rndmorris.salisarcana.api.IVisContainer;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import thaumcraft.common.items.wands.ItemWandCasting;

// todo: break me into multiple files to fit package structure

@Mixin(value = { ItemAmuletVis.class, ItemWandCasting.class }, remap = false)
public abstract class MixinAmuletWand_AddInterface implements IVisContainer {
}
