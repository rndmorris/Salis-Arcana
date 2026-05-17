package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands.foci;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
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

    // spotless:off
    /*
        The problematic code in question looks like this:
            if(cost2 == null) {
                cost2 = (new AspectList()).add(...).add(...);
                cost2.add(cost);
            }
            return cost2;
        The problem is that both instances of this code segment use the same static variable,
        so only one secondary cost can exist for the focus.
        We solve this problem by replacing all gets of cost2 with our own variable for each upgrade:
            if(sa$silkTouchCost == null) {
                cost2 = (new AspectList()).add(...).add(...);
                sa$silkTouchCost.add(cost);
            }
            return sa$silkTouchCost;
        Since our variables are never null, the if-statement never triggers.
        The @Slices allow us to target each upgrade separately, so the other upgrade instead looks like this:
            if(sa$dowsingCost == null) {
                cost2 = (new AspectList()).add(...).add(...);
                sa$dowsingCost.add(cost);
            }
            return sa$dowsingCost;
        Thus the upgrades no longer interfere with each other, and there's no initialization order to worry about.
     */
    // spotless:on

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
