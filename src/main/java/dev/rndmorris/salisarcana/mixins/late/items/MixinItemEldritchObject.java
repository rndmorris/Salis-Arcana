package dev.rndmorris.salisarcana.mixins.late.items;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.common.items.ItemEldritchObject;

@Mixin(ItemEldritchObject.class)
public class MixinItemEldritchObject {

    @ModifyConstant(method = "<init>", constant = { @Constant(intValue = 1, ordinal = 0) })
    private int modifyEldritchObject(int original) {
        return ConfigModuleRoot.enhancements.itemEldritchObjectStackSize.getValue();
    }

}
