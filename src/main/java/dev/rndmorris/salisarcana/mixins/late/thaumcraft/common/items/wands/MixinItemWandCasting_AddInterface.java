package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands;

import org.spongepowered.asm.mixin.Mixin;

import dev.rndmorris.salisarcana.api.IVisContainer;
import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(value = { ItemWandCasting.class }, remap = false)
public abstract class MixinItemWandCasting_AddInterface implements IVisContainer {
}
