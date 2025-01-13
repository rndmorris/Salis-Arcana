package dev.rndmorris.salisarcana.common;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;
import static dev.rndmorris.salisarcana.SalisArcana.MODID;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraftforge.oredict.OreDictionary;

import dev.rndmorris.salisarcana.common.item.PlaceholderItem;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.config.settings.ReplaceWandComponentSettings;
import dev.rndmorris.salisarcana.lib.ArrayHelper;
import dev.rndmorris.salisarcana.lib.AspectHelper;
import dev.rndmorris.salisarcana.lib.WandHelper;
import dev.rndmorris.salisarcana.lib.WandType;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.wands.StaffRod;
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
            PlaceholderItem.capPlaceholder,
            exampleCapRecipes());

        wandItem.setRod(wand, ConfigItems.WAND_ROD_GREATWOOD);
        wandItem.setCap(wand, ConfigItems.WAND_CAP_IRON);
        replaceCoreResearch = maybeRegister(
            ConfigModuleRoot.enhancements.replaceWandCoreSettings,
            "REPLACEWANDCORE",
            "ROD_greatwood",
            PlaceholderItem.rodPlaceholder,
            exampleRodRecipes());
    }

    private static IArcaneRecipe[][] exampleCapRecipes() {
        if (!ConfigModuleRoot.enhancements.replaceWandCapsSettings.isEnabled()) {
            return new ShapelessArcaneRecipe[][] {};
        }
        final var wand = (ItemWandCasting) ConfigItems.itemWandCasting;
        final var baseCap = ConfigItems.WAND_CAP_GOLD;
        final var baseWandRod = ConfigItems.WAND_ROD_GREATWOOD;
        final var baseStaffRod = ConfigItems.STAFF_ROD_GREATWOOD;

        final var wandItem = new ItemStack(wand);
        wand.setCap(wandItem, baseCap);
        wand.setRod(wandItem, baseWandRod);

        final var staffItem = wandItem.copy();
        wand.setRod(staffItem, baseStaffRod);

        final var scepterItem = wandItem.copy();
        scepterItem.setTagInfo("sceptre", new NBTTagByte((byte) 1));

        final var wandList = new ArrayList<IArcaneRecipe>();
        final var staffList = new ArrayList<IArcaneRecipe>();
        final var scepterList = new ArrayList<IArcaneRecipe>();

        for (var capItem : PlaceholderItem.capPlaceholder.getAllBaseItems()) {
            if (baseCap.getItem()
                .isItemEqual(capItem)) {
                continue;
            }
            final var cap = WandHelper.getWandCapFromItem(capItem);
            if (cap == null) {
                continue;
            }
            final var outputWand = wandItem.copy();
            final var outputStaff = staffItem.copy();
            final var outputscepter = scepterItem.copy();
            wand.setCap(outputWand, cap);
            wand.setCap(outputStaff, cap);
            wand.setCap(outputscepter, cap);

            final var wandCost = WandType.WAND.getCraftingVisCost(cap, baseWandRod);
            wandList.add(
                new ShapelessArcaneRecipe(
                    null,
                    outputWand,
                    AspectHelper.primalList(wandCost),
                    wandItem.copy(),
                    capItem,
                    capItem));

            final var staffCost = WandType.STAFF.getCraftingVisCost(cap, baseStaffRod);
            staffList.add(
                new ShapelessArcaneRecipe(
                    null,
                    outputStaff,
                    AspectHelper.primalList(staffCost),
                    staffItem.copy(),
                    capItem,
                    capItem));

            final var scepterCost = WandType.SCEPTER.getCraftingVisCost(cap, baseWandRod);
            scepterList.add(
                new ShapelessArcaneRecipe(
                    null,
                    outputscepter,
                    AspectHelper.primalList(scepterCost),
                    scepterItem.copy(),
                    capItem,
                    capItem,
                    capItem));
        }

        return new IArcaneRecipe[][] { wandList.toArray(new IArcaneRecipe[0]), staffList.toArray(new IArcaneRecipe[0]),
            scepterList.toArray(new IArcaneRecipe[0]), };
    }

    private static IArcaneRecipe[][] exampleRodRecipes() {
        if (!ConfigModuleRoot.enhancements.replaceWandCoreSettings.isEnabled()) {
            return new ShapelessArcaneRecipe[][] {};
        }
        final var wand = (ItemWandCasting) ConfigItems.itemWandCasting;
        final var baseCap = ConfigItems.WAND_CAP_GOLD;
        final var baseWandRod = ConfigItems.WAND_ROD_GREATWOOD;
        final var baseStaffRod = ConfigItems.STAFF_ROD_GREATWOOD;

        final var wandItem = new ItemStack(wand);
        wand.setCap(wandItem, baseCap);
        wand.setRod(wandItem, baseWandRod);

        final var staffItem = wandItem.copy();
        wand.setRod(staffItem, baseStaffRod);

        final var scepterItem = wandItem.copy();
        scepterItem.setTagInfo("sceptre", new NBTTagByte((byte) 1));

        final var wandList = new ArrayList<IArcaneRecipe>();
        final var scepterList = new ArrayList<IArcaneRecipe>();
        final var staffList = new ArrayList<IArcaneRecipe>();

        for (var rodItem : PlaceholderItem.rodPlaceholder.getAllBaseItems()) {
            final var rod = WandHelper.getWandRodFromItem(rodItem);
            if (rod == null) {
                continue;
            }
            if (rod instanceof StaffRod) {
                if (rod == baseStaffRod) {
                    continue;
                }
                final var outputStaff = staffItem.copy();
                wand.setRod(outputStaff, rod);

                final var staffCost = WandType.STAFF.getCraftingVisCost(baseCap, rod);
                staffList.add(
                    new ShapelessArcaneRecipe(
                        null,
                        outputStaff,
                        AspectHelper.primalList(staffCost),
                        staffItem,
                        rodItem));
            } else {
                if (rod == baseWandRod) {
                    continue;
                }
                final var outputWand = wandItem.copy();
                final var outputScepter = scepterItem.copy();
                wand.setRod(outputWand, rod);
                wand.setRod(outputScepter, rod);

                final var wandCost = WandType.WAND.getCraftingVisCost(baseCap, rod);
                wandList.add(
                    new ShapelessArcaneRecipe(null, outputWand, AspectHelper.primalList(wandCost), wandItem, rodItem));

                final var scepterCost = WandType.SCEPTER.getCraftingVisCost(baseCap, rod);
                scepterList.add(
                    new ShapelessArcaneRecipe(
                        null,
                        outputScepter,
                        AspectHelper.primalList(scepterCost),
                        scepterItem,
                        rodItem));
            }
        }

        return new IArcaneRecipe[][] { wandList.toArray(new IArcaneRecipe[0]),
            scepterList.toArray(new IArcaneRecipe[0]), staffList.toArray(new IArcaneRecipe[0]), };
    }

    private static ResearchItem maybeRegister(ReplaceWandComponentSettings settings, String key, String siblingKey,
        PlaceholderItem placeholderItem, IArcaneRecipe[]... recipeSets) {
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
                .setSpecial();
        final var pages = new ArrayList<ResearchPage>();
        pages.add(new ResearchPage("tc.research_page." + fullKey + ".0"));

        for (var recipeSet : recipeSets) {
            if (recipeSet.length < 1) {
                continue;
            }
            pages.add(new ResearchPage(recipeSet));
        }

        research.setPages(pages.toArray(new ResearchPage[0]));
        research.registerResearchItem();

        final var sibling = ResearchCategories.getResearch(siblingKey);
        if (sibling == null) {
            LOG.error("Could not locate research {} for {}.", siblingKey, fullKey);
            return research;
        }

        sibling.siblings = ArrayHelper.appendToArray(sibling.siblings, fullKey);
        return research;
    }
}
