package dev.rndmorris.salisarcana.lib;

import java.text.NumberFormat;
import java.util.List;

import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.common.config.ConfigItems;

public class WandPartTooltipEventHandler {

    private static final NumberFormat PERCENT_FORMAT = NumberFormat.getPercentInstance();

    private static final Aspect[] PRIMALS = new Aspect[] { Aspect.AIR, Aspect.EARTH, Aspect.FIRE, Aspect.WATER,
        Aspect.ORDER, Aspect.ENTROPY };

    @SubscribeEvent
    public void renderTooltip(ItemTooltipEvent event) {
        final boolean expandText = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);

        final var wandCap = WandHelper.getWandCapFromItem(event.itemStack);
        final var wandRod = WandHelper.getWandRodFromItem(event.itemStack);

        if (wandCap != null && ResearchCategories.getResearch(wandCap.getResearch()) == null) return;
        if (wandRod != null && ResearchCategories.getResearch(wandRod.getResearch()) == null) return;

        if (!expandText && (wandCap != null || wandRod != null)) {
            event.toolTip.add(StatCollector.translateToLocal("salisarcana:wand_part.show_details"));
            return;
        }

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
                    final String primalName = "§" + primal.getChatcolor() + primal.getName();

                    event.toolTip.add(
                        StatCollector.translateToLocalFormatted(
                            "salisarcana:wand_cap.vis_discount.aspect",
                            primalName,
                            specialAspects.contains(primal) ? specialCostString : baseCostString));
                }
            }

            addMultilineTranslation(event.toolTip, "salisarcana:wand_cap.special." + wandCap.getTag());

            if (wandCap == ConfigItems.WAND_CAP_IRON) {
                event.toolTip.add(StatCollector.translateToLocal("salisarcana:wand_part.cannot_preserve_node"));
            }
        } else if (wandRod != null) {
            event.toolTip.add(
                StatCollector.translateToLocalFormatted("salisarcana:wand_rod.vis_capacity", wandRod.getCapacity()));

            if (wandRod instanceof StaffRod staff && staff.hasRunes()) {
                event.toolTip.add(StatCollector.translateToLocal("salisarcana:wand_rod.runes"));
            }

            addMultilineTranslation(event.toolTip, "salisarcana:wand_rod.special." + wandRod.getTag());

            if (wandRod == ConfigItems.WAND_ROD_WOOD) {
                event.toolTip.add(StatCollector.translateToLocal("salisarcana:wand_part.cannot_preserve_node"));
            }
        }
    }

    private static void addMultilineTranslation(final List<String> tooltip, final String langKey) {
        String translation = StatCollector.translateToLocal(langKey);
        if (translation != langKey) {
            tooltip.add(translation);
            return;
        }

        int keyNum = 1;
        while (true) {
            final String langKeyLine = langKey + "." + keyNum;
            final String line = StatCollector.translateToLocal(langKeyLine);

            if (langKeyLine == line) return;

            tooltip.add(line);
            keyNum++;
        }
    }
}
