package dev.rndmorris.salisarcana.common;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizons.tcwands.api.TCWandAPI;
import com.gtnewhorizons.tcwands.api.wandinfo.WandDetails;
import com.gtnewhorizons.tcwands.api.wrappers.AbstractWandWrapper;

import dev.rndmorris.salisarcana.common.compat.GTNHTCWandsCompat;
import dev.rndmorris.salisarcana.common.item.PlaceholderItem;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.config.settings.CustomResearchSetting;
import dev.rndmorris.salisarcana.lib.ArrayHelper;
import dev.rndmorris.salisarcana.lib.AspectHelper;
import dev.rndmorris.salisarcana.lib.WandHelper;
import dev.rndmorris.salisarcana.lib.WandType;
import thaumcraft.api.ThaumcraftApi;
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
    public static ResearchItem containerScanResearch;

    public static void init() {
        final var wandItem = (ItemWandCasting) ConfigItems.itemWandCasting;
        final var wand = new ItemStack(wandItem);

        wandItem.setRod(wand, ConfigItems.WAND_ROD_WOOD);
        wandItem.setCap(wand, ConfigItems.WAND_CAP_GOLD);
        replaceCapsResearch = maybeRegister(
            SalisConfig.features.replaceWandCapsSettings,
            PlaceholderItem.capPlaceholder,
            exampleCapRecipes());

        wandItem.setRod(wand, ConfigItems.WAND_ROD_GREATWOOD);
        wandItem.setCap(wand, ConfigItems.WAND_CAP_IRON);
        replaceCoreResearch = maybeRegister(
            SalisConfig.features.replaceWandCoreSettings,
            PlaceholderItem.rodPlaceholder,
            exampleRodRecipes());

        if (SalisConfig.features.enableFocusDisenchanting.isEnabled()) {
            DisenchantFocusUpgrade.registerResearch();
        }

        if (SalisConfig.features.nomiconDuplicateResearch.isEnabled()) {
            final var dupeResearch = ResearchCategories.getResearch("RESEARCHDUPE");
            final var oldPages = dupeResearch.getPages();
            final var newPages = new ResearchPage[oldPages.length + 1];
            System.arraycopy(oldPages, 0, newPages, 0, oldPages.length);
            newPages[oldPages.length] = new ResearchPage("salisarcana:duplicate_research.extra_page");
            dupeResearch.setPages(newPages);
        }

        containerScanResearch = maybeRegister(
            SalisConfig.features.thaumometerScanContainersResearch,
            Item.getItemFromBlock(Blocks.chest));
    }

    private static IArcaneRecipe[][] exampleCapRecipes() {
        if (!SalisConfig.features.replaceWandCapsSettings.isEnabled()) {
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

        WandHelper.allVanillaCaps()
            .stream()
            .filter(
                wandCap -> wandCap != null && wandCap.getItem() != null
                    && wandCap.getItem()
                        .getItem() != null)
            .forEach(wandCap -> {
                if (baseCap == wandCap) {
                    return;
                }

                final ItemStack wandCapItem = wandCap.getItem();

                final var outputWand = wandItem.copy();
                final var outputStaff = staffItem.copy();
                final var outputScepter = scepterItem.copy();
                wand.setCap(outputWand, wandCap);
                wand.setCap(outputStaff, wandCap);
                wand.setCap(outputScepter, wandCap);

                final var wandCost = WandType.WAND.getCraftingVisCost(wandCap, baseWandRod);
                wandList.add(
                    new ShapelessArcaneRecipe(
                        null,
                        outputWand,
                        AspectHelper.primalList(wandCost),
                        wandItem,
                        wandCapItem,
                        wandCapItem));

                final var staffCost = WandType.STAFF.getCraftingVisCost(wandCap, baseStaffRod);
                staffList.add(
                    new ShapelessArcaneRecipe(
                        null,
                        outputStaff,
                        AspectHelper.primalList(staffCost),
                        staffItem.copy(),
                        wandCapItem,
                        wandCapItem));

                final var scepterCost = WandType.SCEPTER.getCraftingVisCost(wandCap, baseWandRod);
                scepterList.add(
                    new ShapelessArcaneRecipe(
                        null,
                        outputScepter,
                        AspectHelper.primalList(scepterCost),
                        scepterItem.copy(),
                        wandCapItem,
                        wandCapItem,
                        wandCapItem));
            });

        return new IArcaneRecipe[][] { wandList.toArray(new IArcaneRecipe[0]), staffList.toArray(new IArcaneRecipe[0]),
            scepterList.toArray(new IArcaneRecipe[0]), };
    }

    private static IArcaneRecipe[][] exampleRodRecipes() {
        if (!SalisConfig.features.replaceWandCoreSettings.isEnabled()) {
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

        final ItemStack[] screw = { null }; // Needs to be like this to work in the lambda
        final ItemStack[] conductor = { null };

        WandHelper.allVanillaRods()
            .stream()
            .filter(
                wandRod -> wandRod != null && wandRod.getItem() != null
                    && wandRod.getItem()
                        .getItem() != null)
            .forEach(wandRod -> {
                final ItemStack rodItem = wandRod.getItem();

                if (wandRod instanceof StaffRod) {
                    if (wandRod == baseStaffRod) {
                        return;
                    }
                    final var outputStaff = staffItem.copy();
                    wand.setRod(outputStaff, wandRod);

                    final var staffCost = WandType.STAFF.getCraftingVisCost(baseCap, wandRod);

                    if (SalisConfig.modCompat.gtnhWands.coreSwapMaterials.isEnabled()) {
                        AbstractWandWrapper wrapper = GTNHTCWandsCompat
                            .getWandWrapper(wandRod, WandType.getWandType(wandItem));
                        if (wrapper == null) wrapper = TCWandAPI.getWandWrappers()
                            .get(0);
                        WandDetails props = wrapper.getDetails();
                        screw[0] = OreDictionary.getOres(props.getScrew())
                            .get(0);
                        conductor[0] = props.getConductor();

                        staffList.add(
                            new ShapelessArcaneRecipe(
                                null,
                                outputStaff,
                                AspectHelper.primalList(staffCost),
                                staffItem,
                                rodItem,
                                screw[0],
                                screw[0],
                                screw[0],
                                screw[0],
                                conductor[0],
                                conductor[0]));
                    } else {
                        staffList.add(
                            new ShapelessArcaneRecipe(
                                null,
                                outputStaff,
                                AspectHelper.primalList(staffCost),
                                staffItem,
                                rodItem));
                    }
                } else {
                    if (wandRod == baseWandRod) {
                        return;
                    }
                    final var outputWand = wandItem.copy();
                    final var outputScepter = scepterItem.copy();
                    wand.setRod(outputWand, wandRod);
                    wand.setRod(outputScepter, wandRod);
                    final var wandCost = WandType.WAND.getCraftingVisCost(baseCap, wandRod);
                    final var scepterCost = WandType.SCEPTER.getCraftingVisCost(baseCap, wandRod);

                    if (SalisConfig.modCompat.gtnhWands.coreSwapMaterials.isEnabled()) {
                        AbstractWandWrapper wrapper = GTNHTCWandsCompat
                            .getWandWrapper(wandRod, WandType.getWandType(wandItem));
                        if (wrapper == null) wrapper = TCWandAPI.getWandWrappers()
                            .get(0);
                        WandDetails props = wrapper.getDetails();
                        screw[0] = OreDictionary.getOres(props.getScrew())
                            .get(0);
                        conductor[0] = props.getConductor();

                        wandList.add(
                            new ShapelessArcaneRecipe(
                                null,
                                outputWand,
                                AspectHelper.primalList(wandCost),
                                wandItem,
                                rodItem,
                                screw[0],
                                screw[0],
                                screw[0],
                                screw[0],
                                conductor[0],
                                conductor[0]));

                        scepterList.add(
                            new ShapelessArcaneRecipe(
                                null,
                                outputScepter,
                                AspectHelper.primalList(scepterCost),
                                scepterItem,
                                rodItem,
                                screw[0],
                                screw[0],
                                conductor[0],
                                conductor[0]));
                    } else {
                        wandList.add(
                            new ShapelessArcaneRecipe(
                                null,
                                outputWand,
                                AspectHelper.primalList(wandCost),
                                wandItem,
                                rodItem));

                        scepterList.add(
                            new ShapelessArcaneRecipe(
                                null,
                                outputScepter,
                                AspectHelper.primalList(scepterCost),
                                scepterItem,
                                rodItem));
                    }
                }
            });

        return new IArcaneRecipe[][] { wandList.toArray(new IArcaneRecipe[0]),
            scepterList.toArray(new IArcaneRecipe[0]), staffList.toArray(new IArcaneRecipe[0]), };
    }

    private static ResearchItem maybeRegister(CustomResearchSetting settings, Item placeholderItem,
        IArcaneRecipe[]... recipeSets) {
        if (!settings.isEnabled()) {
            return null;
        }
        final var fullKey = settings.getInternalName();
        final var category = settings.researchCategory;
        final var col = settings.researchCol;
        final var row = settings.researchRow;

        final var research = new ResearchItem(
            fullKey,
            category,
            settings.getAspects(),
            col,
            row,
            settings.difficulty,
            new ItemStack(placeholderItem, 0, OreDictionary.WILDCARD_VALUE)).setConcealed()
                .setParents(settings.parentResearches)
                .setSpecial();
        if (settings.autoUnlock) {
            research.setStub();
            for (String parentResearch : settings.parentResearches) {
                final var sibling = ResearchCategories.getResearch(parentResearch);
                if (sibling == null) {
                    LOG.error("Could not locate research {} for {}.", parentResearch, fullKey);
                    continue;
                }
                sibling.siblings = ArrayHelper.appendToArray(sibling.siblings, fullKey);
            }
        } else if (research.tags.size() == 0) {
            // The research isn't free, so we need some aspect cost for it to be researchable
            LOG.error(
                "Research {} does not have any aspects set but is not auto-unlockable, making it impossible to research.",
                fullKey);
        }

        if (settings.warp > 0) {
            ThaumcraftApi.addWarpToResearch(fullKey, settings.warp);
        }
        final var pages = new ArrayList<ResearchPage>();
        pages.add(new ResearchPage("tc.research_page." + fullKey + ".0"));

        for (var recipeSet : recipeSets) {
            if (recipeSet.length < 1) {
                continue;
            }
            pages.add(new ResearchPage(recipeSet));
        }

        research.setPages(pages.toArray(new ResearchPage[0]));
        if (settings.purchasable) {
            research.setSecondary();
        }
        research.registerResearchItem();

        return research;
    }
}
