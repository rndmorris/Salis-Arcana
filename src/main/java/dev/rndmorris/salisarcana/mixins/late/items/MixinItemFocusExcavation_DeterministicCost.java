package dev.rndmorris.salisarcana.mixins.late.items;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.foci.ItemFocusExcavation;

@Mixin(value = ItemFocusExcavation.class, remap = false)
public class MixinItemFocusExcavation_DeterministicCost extends ItemFocusBasic {

    @Unique
    private static final AspectList sa$silkTouchCost = new AspectList().add(Aspect.AIR, 1)
        .add(Aspect.FIRE, 1)
        .add(Aspect.WATER, 1)
        .add(Aspect.ORDER, 1)
        .add(Aspect.ENTROPY, 1)
        .add(Aspect.EARTH, 16);

    @Unique
    private static final AspectList sa$dowsingCost = new AspectList().add(Aspect.FIRE, 2)
        .add(Aspect.ORDER, 2)
        .add(Aspect.EARTH, 15);

    @Shadow
    public static FocusUpgradeType dowsing;

    @Shadow
    @Final
    private static AspectList cost;

    @ModifyExpressionValue(
        method = "getVisCost",
        at = @At(
            value = "FIELD",
            target = "Lthaumcraft/common/items/wands/foci/ItemFocusExcavation;cost2:Lthaumcraft/api/aspects/AspectList;",
            opcode = Opcodes.GETSTATIC),
        slice = @Slice(
            to = @At(
                value = "FIELD",
                target = "Lthaumcraft/common/items/wands/foci/ItemFocusExcavation;dowsing:Lthaumcraft/api/wands/FocusUpgradeType;")))
    public AspectList silkTouchCost(AspectList original) {
        return sa$silkTouchCost;
    }

    @ModifyExpressionValue(
        method = "getVisCost",
        at = @At(
            value = "FIELD",
            target = "Lthaumcraft/common/items/wands/foci/ItemFocusExcavation;cost2:Lthaumcraft/api/aspects/AspectList;",
            opcode = Opcodes.GETSTATIC),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lthaumcraft/common/items/wands/foci/ItemFocusExcavation;dowsing:Lthaumcraft/api/wands/FocusUpgradeType;")))
    public AspectList dowsingCost(AspectList original) {
        return sa$dowsingCost;
    }
}
