package dev.rndmorris.salisarcana.common;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;
import static dev.rndmorris.salisarcana.SalisArcana.MODID;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import dev.rndmorris.salisarcana.common.item.PlaceholderItem;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.config.settings.ReplaceWandComponentSettings;
import dev.rndmorris.salisarcana.lib.ArrayHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;

public class CustomResearch {

    public static ResearchItem replaceCapsResearch;
    public static ResearchItem replaceCoreResearch;

    public static void init() {
        final var wandItem = (ItemWandCasting) ConfigItems.itemWandCasting;
        final var wand = new ItemStack(wandItem);

        wandItem.setRod(wand, ConfigItems.WAND_ROD_WOOD);
        wandItem.setCap(wand, ConfigItems.WAND_CAP_GOLD);
        replaceCapsResearch = maybeRegister(
            ConfigModuleRoot.enhancements.replaceWandCapsSettings,
            "REPLACEWANDCAPS",
            "CAP_gold",
            PlaceholderItem.capPlaceholder);

        wandItem.setRod(wand, ConfigItems.WAND_ROD_GREATWOOD);
        wandItem.setCap(wand, ConfigItems.WAND_CAP_IRON);
        replaceCoreResearch = maybeRegister(
            ConfigModuleRoot.enhancements.replaceWandCoreSettings,
            "REPLACEWANDCORE",
            "ROD_greatwood",
            PlaceholderItem.rodPlaceholder);
    }

    private static ResearchItem maybeRegister(ReplaceWandComponentSettings settings, String key, String siblingKey,
        PlaceholderItem placeholderItem) {
        if (!settings.isEnabled()) {
            return null;
        }
        final var fullKey = MODID + ":" + key;
        final var category = settings.getResearchCategory();
        final var col = settings.getResearchCol();
        final var row = settings.getResearchRow();

        final var research = new ResearchItem(
            fullKey,
            category,
            new AspectList(),
            col,
            row,
            1,
            new ItemStack(placeholderItem, 0, OreDictionary.WILDCARD_VALUE)).setConcealed()
                .setSpecial()
                .registerResearchItem();

        final var sibling = ResearchCategories.getResearch(siblingKey);
        if (sibling == null) {
            LOG.error("Could not locate research {} for {}.", siblingKey, fullKey);
            return research;
        }

        sibling.siblings = ArrayHelper.appendToArray(sibling.siblings, fullKey);
        return research;
    }
}
