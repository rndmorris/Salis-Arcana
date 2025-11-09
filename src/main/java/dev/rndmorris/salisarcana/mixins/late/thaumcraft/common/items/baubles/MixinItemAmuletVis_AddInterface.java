package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.baubles;

import org.spongepowered.asm.mixin.Mixin;

import dev.rndmorris.salisarcana.api.IVisContainer;
import thaumcraft.common.items.baubles.ItemAmuletVis;

@Mixin(value = { ItemAmuletVis.class }, remap = false)
public abstract class MixinItemAmuletVis_AddInterface implements IVisContainer {
}
