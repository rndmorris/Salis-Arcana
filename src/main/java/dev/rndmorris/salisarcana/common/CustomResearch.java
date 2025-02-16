package dev.rndmorris.salisarcana.common;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;
import static dev.rndmorris.salisarcana.SalisArcana.MODID;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraftforge.oredict.OreDictionary;

import org.spongepowered.libraries.com.google.gson.JsonSyntaxException;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import dev.rndmorris.salisarcana.common.item.PlaceholderItem;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.config.settings.CustomResearchSetting;
import dev.rndmorris.salisarcana.config.settings.ResearchEntry;
import dev.rndmorris.salisarcana.lib.ArrayHelper;
import dev.rndmorris.salisarcana.lib.AspectHelper;
import dev.rndmorris.salisarcana.lib.ResearchHelper;
import dev.rndmorris.salisarcana.lib.WandHelper;
import dev.rndmorris.salisarcana.lib.WandType;
import dev.rndmorris.salisarcana.network.MessageInvalidateCache;
import dev.rndmorris.salisarcana.network.MessageSendResearch;
import dev.rndmorris.salisarcana.network.NetworkHandler;
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

    public static void registerResearchFromFiles() {
        if (FMLCommonHandler.instance()
            .getEffectiveSide()
            .isClient()) {
            return;
        }
        List<ResearchEntry> researches = new ArrayList<>();

        File researchPath = Paths.get("config", "salisarcana", "research")
            .toFile();
        if (researchPath.exists() && researchPath.isDirectory()) {
            File[] files = researchPath.listFiles((dir, name) -> name.endsWith(".json"));
            if (files == null) {
                return;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    continue;
                }
                try {
                    ResearchEntry research = ResearchHelper.importResearchFromJson(file);
                    researches.add(research);
                } catch (IOException e) {
                    LOG.error("Could not read research file {}.", file.getName(), e);
                } catch (JsonSyntaxException e) {
                    LOG.error("Could not parse research file {}.", file.getName(), e);
                }
            }
        } else {
            if (!researchPath.mkdirs()) {
                LOG.warn("Could not create research config directory.");
            }
        }
        if (researches.isEmpty()) {
            return;
        }
        for (ResearchEntry research : researches) {
            if (!research.isEnabled()) {
                continue;
            }
            try {
                if (ResearchHelper.registerCustomResearch(research)) {
                    NetworkHandler.instance.sendToAll(new MessageSendResearch(research));
                } else {
                    LOG.error("Could not register research {}.", research.getKey());
                }
            } catch (Exception e) {
                LOG.error("Could not register research {}.", research.getKey(), e);
            }
        }
        if (Loader.isModLoaded("tc4tweak")) {
            NetworkHandler.instance.sendToAll(new MessageInvalidateCache());
        }
    }

    public static void init() {
        final var wandItem = (ItemWandCasting) ConfigItems.itemWandCasting;
        final var wand = new ItemStack(wandItem);

        wandItem.setRod(wand, ConfigItems.WAND_ROD_WOOD);
        wandItem.setCap(wand, ConfigItems.WAND_CAP_GOLD);
        replaceCapsResearch = maybeRegister(
            ConfigModuleRoot.enhancements.replaceWandCapsSettings,
            PlaceholderItem.capPlaceholder,
            exampleCapRecipes());

        wandItem.setRod(wand, ConfigItems.WAND_ROD_GREATWOOD);
        wandItem.setCap(wand, ConfigItems.WAND_CAP_IRON);
        replaceCoreResearch = maybeRegister(
            ConfigModuleRoot.enhancements.replaceWandCoreSettings,
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
                    staffList.add(
                        new ShapelessArcaneRecipe(
                            null,
                            outputStaff,
                            AspectHelper.primalList(staffCost),
                            staffItem,
                            rodItem));
                } else {
                    if (wandRod == baseWandRod) {
                        return;
                    }
                    final var outputWand = wandItem.copy();
                    final var outputScepter = scepterItem.copy();
                    wand.setRod(outputWand, wandRod);
                    wand.setRod(outputScepter, wandRod);

                    final var wandCost = WandType.WAND.getCraftingVisCost(baseCap, wandRod);
                    wandList.add(
                        new ShapelessArcaneRecipe(
                            null,
                            outputWand,
                            AspectHelper.primalList(wandCost),
                            wandItem,
                            rodItem));

                    final var scepterCost = WandType.SCEPTER.getCraftingVisCost(baseCap, wandRod);
                    scepterList.add(
                        new ShapelessArcaneRecipe(
                            null,
                            outputScepter,
                            AspectHelper.primalList(scepterCost),
                            scepterItem,
                            rodItem));
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
        final var fullKey = MODID + ":" + settings.researchName;
        final var category = settings.researchCategory;
        final var col = settings.researchCol;
        final var row = settings.researchRow;

        final var research = new ResearchItem(
            fullKey,
            category,
            settings.researchAspects,
            col,
            row,
            settings.difficulty,
            new ItemStack(placeholderItem, 0, OreDictionary.WILDCARD_VALUE)).setConcealed()
                .setParents(settings.parentResearches)
                .setSpecial();
        if (settings.autoUnlock) {
            research.setStub();
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

        for (String parentResearch : settings.parentResearches) {
            final var sibling = ResearchCategories.getResearch(parentResearch);
            if (sibling == null) {
                LOG.error("Could not locate research {} for {}.", parentResearch, fullKey);
                continue;
            }
            sibling.siblings = ArrayHelper.appendToArray(sibling.siblings, fullKey);
        }

        return research;
    }
}
