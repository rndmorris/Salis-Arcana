package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.common.items.ItemEldritchObject;

@Mixin(value = ItemEldritchObject.class, remap = false)
public class MixinItemEldritchObject_EldritchObjectStackSize {

    @ModifyConstant(method = "<init>", constant = { @Constant(intValue = 1, ordinal = 0) })
    private int modifyEldritchObject(int original) {
        return SalisConfig.features.itemEldritchObjectStackSize.getValue();
    }

}
