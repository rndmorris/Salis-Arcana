package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.*;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.foci.ItemFocusExcavation;

@Mixin(ItemFocusExcavation.class)
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

    /**
     * @author nicksitnikov
     * @reason The original method poorly implements lazy loading, resulting in nondeterministic results. There's no
     *         good way to transform the function otherwise.
     */
    @Overwrite
    public AspectList getVisCost(ItemStack itemstack) {
        if (this.isUpgradedWith(itemstack, FocusUpgradeType.silktouch)) {
            return sa$silkTouchCost;
        } else if (this.isUpgradedWith(itemstack, dowsing)) {
            return sa$dowsingCost;
        } else {
            return cost;
        }
    }
}
