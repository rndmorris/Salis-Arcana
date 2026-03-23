package dev.rndmorris.salisarcana.lib;

import java.text.NumberFormat;
import java.util.List;
import java.util.StringJoiner;

import cpw.mods.fml.common.Optional;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import org.lwjgl.input.Keyboard;

import com.gtnewhorizons.tcwands.api.wandinfo.WandProps;
import com.gtnewhorizons.tcwands.api.wrappers.AbstractWandWrapper;
import com.gtnewhorizons.tcwands.api.wrappers.CapWrapper;
import com.gtnewhorizons.tcwands.api.wrappers.SceptreWrapper;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dev.rndmorris.salisarcana.common.compat.GTNHTCWandsCompat;
import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
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

                final StringJoiner specials = new StringJoiner(", ");
                final StringJoiner normals = new StringJoiner(", ");

                for (final Aspect primal : PRIMALS) {
                    final String primalName = "§" + primal.getChatcolor() + primal.getName();

                    if (specialAspects.contains(primal)) {
                        specials.add(primalName);
                    } else {
                        normals.add(primalName);
                    }
                }

                event.toolTip.add(
                    StatCollector.translateToLocalFormatted(
                        "salisarcana:wand_cap.vis_discount.aspect",
                        specials.toString(),
                        specialCostString));

                event.toolTip.add(
                    StatCollector.translateToLocalFormatted(
                        "salisarcana:wand_cap.vis_discount.aspect",
                        normals.toString(),
                        baseCostString));
            }

            int multiplier = wandCap.getCraftCost();

            if (SalisConfig.modCompat.gtnhWands.isEnabled()) {
                multiplier = getGTNHCapsMultiplier(wandCap, multiplier);
            }

            event.toolTip.add(StatCollector.translateToLocalFormatted("salisarcana:wand_cap.price_mult", multiplier));

            addMultilineTranslation(event.toolTip, "salisarcana:wand_cap.special." + wandCap.getTag());

            if (wandCap == ConfigItems.WAND_CAP_IRON) {
                event.toolTip.add(StatCollector.translateToLocal("salisarcana:wand_part.cannot_preserve_node"));
            }
        } else if (wandRod != null) {
            event.toolTip.add(
                StatCollector.translateToLocalFormatted("salisarcana:wand_rod.vis_capacity", wandRod.getCapacity()));

            boolean isStaff = wandRod instanceof StaffRod;
            if (SalisConfig.modCompat.gtnhWands.isEnabled()
                && getWrapper(wandRod, isStaff) instanceof SceptreWrapper wrapper) {
                addGTNHCoreStats(event, wrapper, isStaff);
            } else {
                event.toolTip.add(
                    StatCollector.translateToLocalFormatted("salisarcana:wand_rod.base_price", wandRod.getCraftCost()));
            }

            if (wandRod instanceof StaffRod staff && staff.hasRunes()) {
                event.toolTip.add(StatCollector.translateToLocal("salisarcana:wand_rod.runes"));
            }

            addMultilineTranslation(event.toolTip, "salisarcana:wand_rod.special." + wandRod.getTag());

            if (wandRod == ConfigItems.WAND_ROD_WOOD) {
                event.toolTip.add(StatCollector.translateToLocal("salisarcana:wand_part.cannot_preserve_node"));
            }
        }
    }

    @Optional.Method(modid = "gtnhtcwands")
    private static int getGTNHCapsMultiplier(WandCap wandCap, int multiplier) {
        CapWrapper capWrapper = GTNHTCWandsCompat.getCapWrapper(wandCap);
        if (capWrapper != null) {
            multiplier = capWrapper.getCostMultiplier();
        }
        return multiplier;
    }

    @Optional.Method(modid = "gtnhtcwands")
    private static void addGTNHCoreStats(ItemTooltipEvent event, SceptreWrapper wrapper, boolean isStaff) {
        WandProps props = wrapper.getProps();
        event.toolTip.add(
            StatCollector.translateToLocalFormatted(
                "salisarcana:wand_rod.gtnh_wands_price",
                props.getBaseCost(),
                props.getCapCost()));
        event.toolTip.add(
            StatCollector.translateToLocalFormatted(
                isStaff ? "salisarcana:wand_rod.gtnh_staffter_mult" : "salisarcana:wand_rod.gtnh_scepter_mult",
                wrapper.getSceptreCostMultiplier()));
    }

    @Optional.Method(modid = "gtnhtcwands")
    private static AbstractWandWrapper getWrapper(WandRod wandRod, boolean isStaff) {
        return GTNHTCWandsCompat.getWandWrapper(wandRod, isStaff ? WandType.STAFFTER : WandType.SCEPTER);
    }

    private static void addMultilineTranslation(final List<String> tooltip, final String langKey) {
        // StatCollector.translateToLocal will return the original string object if it cannot find a translation key.
        // In order to avoid a second hashmap lookup, we use a direct comparison to check if this happened.
        String translation = StatCollector.translateToLocal(langKey);
        if (translation != langKey) {
            tooltip.add(translation);
            return;
        }

        int keyNum = 1;
        while (true) {
            final String langKeyLine = langKey + "." + keyNum;
            final String line = StatCollector.translateToLocal(langKeyLine);

            // StatCollector.translateToLocal will return the original string object if it cannot find the key.
            if (langKeyLine == line) return;

            tooltip.add(line);
            keyNum++;
        }
    }
}
