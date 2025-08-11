package dev.rndmorris.salisarcana.lib;

import java.text.NumberFormat;

import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.StaffRod;

public class WandPartTooltipEventHandler {

    private static final NumberFormat PERCENT_FORMAT = NumberFormat.getPercentInstance();

    private static final Aspect[] PRIMALS = new Aspect[] { Aspect.AIR, Aspect.EARTH, Aspect.FIRE, Aspect.WATER,
        Aspect.ORDER, Aspect.ENTROPY };

    @SubscribeEvent
    public void renderTooltip(ItemTooltipEvent event) {
        final var wandCap = WandHelper.getWandCapFromItem(event.itemStack);
        if (wandCap != null) {
            final float baseCost = wandCap.getBaseCostModifier();
            final float specialCost = wandCap.getSpecialCostModifier();
            final var specialAspects = wandCap.getSpecialCostModifierAspects();

            final String baseCostString = (baseCost > 1f ? "§4" : "§2") + PERCENT_FORMAT.format(1f - baseCost);
            if (specialCost == 0 || specialAspects == null || specialAspects.isEmpty()) {
                // The vis price is the same for all aspects
                event.toolTip.add(
                    StatCollector
                        .translateToLocalFormatted("salisarcana:wand_cap.vis_discount.generic", baseCostString));
            } else {
                final String specialCostString = (specialCost > 1f ? "§4" : "§2")
                    + PERCENT_FORMAT.format(1f - specialCost);

                for (final Aspect primal : PRIMALS) {
                    final String primalName = "§" + primal.getChatcolor() + primal.getName() + "§5";

                    event.toolTip.add(
                        StatCollector.translateToLocalFormatted(
                            "salisarcana:wand_cap.vis_discount.aspect",
                            primalName,
                            specialAspects.contains(primal) ? specialCostString : baseCostString));
                }
            }
            return;
        }

        final var wandRod = WandHelper.getWandRodFromItem(event.itemStack);
        if (wandRod != null) {
            event.toolTip.add(
                StatCollector.translateToLocalFormatted("salisarcana:wand_rod.vis_capacity", wandRod.getCapacity()));

            if (wandRod instanceof StaffRod staff && staff.hasRunes()) {
                event.toolTip.add(StatCollector.translateToLocal("salisarcana:wand_rod.runes"));
            }
        }
    }
}
