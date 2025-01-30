package dev.rndmorris.salisarcana.config.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.google.gson.annotations.SerializedName;

import cpw.mods.fml.common.registry.GameRegistry;
import dev.rndmorris.salisarcana.lib.AssetHelper;
import dev.rndmorris.salisarcana.lib.R;
import dev.rndmorris.salisarcana.lib.StringHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.common.config.ConfigResearch;

public class ResearchEntry {

    private String key = "";
    private String category = "";
    private String name = "";
    private String tooltip = "";
    private AspectEntry[] aspects = new AspectEntry[0];
    private String[] parents = new String[0];
    private String[] parentsHidden = new String[0];
    private String[] siblings = new String[0];
    private int displayColumn = 0;
    private int displayRow = 0;
    private String icon_item = "";
    private String icon_resource = "";
    private int complexity = 0;
    private boolean isSpecial = false;
    private boolean isSecondary = false;
    private boolean isRound = false;
    private boolean isStub = false;
    private boolean isVirtual = false;
    private boolean isConcealed = false;
    private boolean isHidden = false;
    private boolean isLost = false;
    private boolean isAutoUnlock = false;
    private String[] itemTriggers = new String[0];
    private String[] entityTriggers = new String[0];
    private AspectEntry[] aspectTriggers = new AspectEntry[0];
    private ResearchPageEntry[] pages = new ResearchPageEntry[0];

    public ResearchEntry() {

    }

    public ResearchEntry(ResearchItem research) {
        this.key = research.key;
        this.category = research.category;
        this.name = research.getName();
        this.tooltip = research.getText();
        this.aspects = research.tags.aspects.entrySet()
            .stream()
            .map(entry -> {
                var aspectEntry = new AspectEntry();
                aspectEntry.aspect = entry.getKey()
                    .getTag();
                aspectEntry.amount = entry.getValue();
                return aspectEntry;
            })
            .toArray(AspectEntry[]::new);
        this.parents = research.parents;
        this.parentsHidden = research.parentsHidden;
        this.siblings = research.siblings;
        this.displayColumn = research.displayColumn;
        this.displayRow = research.displayRow;
        if (research.icon_item == null || GameRegistry.findUniqueIdentifierFor(research.icon_item.getItem()) == null) {
            this.icon_item = "";
        } else {
            this.icon_item = String.format(
                "%s:%d",
                GameRegistry.findUniqueIdentifierFor(research.icon_item.getItem()),
                research.icon_item.getItemDamage());
        }
        this.icon_resource = research.icon_resource != null ? research.icon_resource.toString() : "";
        this.complexity = research.getComplexity();
        this.isSpecial = research.isSpecial();
        this.isSecondary = research.isSecondary();
        this.isRound = research.isRound();
        this.isStub = research.isStub();
        this.isVirtual = research.isVirtual();
        this.isConcealed = research.isConcealed();
        this.isHidden = research.isHidden();
        this.isLost = research.isLost();
        this.isAutoUnlock = research.isAutoUnlock();
        if (research.getItemTriggers() == null) {
            this.itemTriggers = new String[0];
        } else {
            this.itemTriggers = new String[research.getItemTriggers().length];
            for (int j = 0; j < research.getItemTriggers().length; j++) {
                this.itemTriggers[j] = String.format(
                    "%s:%d",
                    GameRegistry.findUniqueIdentifierFor(research.icon_item.getItem()),
                    research.icon_item.getItemDamage());
            }
        }
        if (research.getEntityTriggers() == null) {
            this.entityTriggers = new String[0];
        } else {
            this.entityTriggers = research.getEntityTriggers();
        }
        if (research.getAspectTriggers() == null) {
            this.aspectTriggers = new AspectEntry[0];
        } else {
            this.aspectTriggers = new AspectEntry[research.getAspectTriggers().length];
            for (int j = 0; j < research.getAspectTriggers().length; j++) {
                AspectEntry aspectEntry = new AspectEntry();
                aspectEntry.aspect = research.getAspectTriggers()[j].getTag();
                aspectEntry.amount = 0;
                this.aspectTriggers[j] = aspectEntry;
            }
        }
        this.pages = new ResearchPageEntry[research.getPages().length];
        for (int j = 0; j < research.getPages().length; j++) {
            this.pages[j] = new ResearchPageEntry(research.getPages()[j], j);
        }

    }

    public String getKey() {
        return key;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getTooltip() {
        return tooltip;
    }

    public AspectList getAspects() {
        AspectList aspectList = new AspectList();
        for (AspectEntry entry : aspects) {
            aspectList.add(Aspect.getAspect(entry.getAspect()), entry.getAmount());
        }
        return aspectList;
    }

    public ItemStack getIconItem() {
        if (icon_item == null || icon_item.isEmpty()) {
            return null;
        }
        String[] split = icon_item.split(":");
        ItemStack stack = GameRegistry.findItemStack(split[0], split[1], 1);
        if (split.length > 2 && stack != null) {
            stack.setItemDamage(Integer.parseInt(split[2]));
        }
        return stack;
    }

    public ResourceLocation getIconResource() {
        return icon_resource != null ? new ResourceLocation(icon_resource) : null;
    }

    public ItemStack[] getItemTriggers() {
        if (itemTriggers == null) {
            return new ItemStack[0];
        }
        ItemStack[] ret = new ItemStack[itemTriggers.length];
        for (int i = 0; i < itemTriggers.length; i++) {
            ItemStack stack = StringHelper.parseItemFromString(itemTriggers[i]);
            if (stack != null) {
                ret[i] = stack;
            }
        }
        return ret;
    }

    public Aspect[] getAspectTriggers() {
        if (aspectTriggers == null) {
            return new Aspect[0];
        }
        Aspect[] ret = new Aspect[aspectTriggers.length];
        for (int i = 0; i < aspectTriggers.length; i++) {
            ret[i] = Aspect.getAspect(aspectTriggers[i].getAspect());
        }
        return ret;
    }

    public void updateResearchItem(ResearchItem research) {
        R r = new R(research);
        if (!research.category.equals(this.getCategory())) {
            r.set("category", this.getCategory());
            ResearchCategories.researchCategories.get(research.category).research.remove(research.key);
            ResearchCategories.researchCategories.get(research.category).research.put(research.key, research);
        }
        String key = AssetHelper.lookupLangEntryByValue(research.getName());
        if (key == null) {
            key = research.getName();
        }
        AssetHelper.addLangEntry(key, this.getName());
        key = AssetHelper.lookupLangEntryByValue(research.getText());
        if (key == null) {
            key = research.getText();
        }
        AssetHelper.addLangEntry(key, this.getTooltip());

        research.setParents(parents);
        research.setParentsHidden(parentsHidden);
        research.setSiblings(siblings);
        research.setComplexity(complexity);
        // These fields are all final, so we have to use reflection
        r.set("tags", this.getAspects());
        r.set("icon_item", this.getIconItem());
        r.set("icon_resource", this.getIconResource());
        r.set("displayColumn", displayColumn);
        r.set("displayRow", displayRow);

        // Always using reflection here saves us from having to write if (isSpecial) { research.setSpecial(); }
        // else { r.set("isSpecial", false); }
        // There's no method "unset" any of these fields, so we can't just do that
        r.set("isSpecial", isSpecial);
        r.set("isSecondary", isSecondary);
        r.set("isRound", isRound);
        r.set("isStub", isStub);
        r.set("isVirtual", isVirtual);
        r.set("isConcealed", isConcealed);
        r.set("isHidden", isHidden);
        r.set("isLost", isLost);
        r.set("isAutoUnlock", isAutoUnlock);

        research.setItemTriggers(this.getItemTriggers());
        research.setEntityTriggers(entityTriggers);
        research.setAspectTriggers(this.getAspectTriggers());
        List<ResearchPage> pages = new ArrayList<>();
        for (ResearchPageEntry entry : this.pages) {
            if (entry.getType()
                .equals("text")) {
                AssetHelper.addLangEntry(AssetHelper.lookupLangEntryByValue(entry.getText()), entry.getText());
                AssetHelper
                    .addLangEntry("tc_research_page." + this.getKey() + "." + entry.getNumber(), entry.getText());
                pages.add(new ResearchPage(entry.getText()));
            } else if (entry.getType()
                .equals("picture")) {
                    AssetHelper
                        .addLangEntry("tc_research_page." + this.getKey() + "." + entry.getNumber(), entry.getText());
                    pages.add(new ResearchPage(entry.getResource(), entry.getText()));
                } else {
                    pages.add(entry.getPage());
                }
        }
        research.setPages(pages.toArray(new ResearchPage[0]));
    }

}

class AspectEntry {

    public String aspect;
    public int amount;

    public String getAspect() {
        return aspect;
    }

    public int getAmount() {
        return amount;
    }
}

class ResearchPageEntry {

    @SerializedName("pageType")
    public String type;
    public int number;
    public String text = "";
    public ItemEntry item = null;
    public String resource = "";
    public AspectEntry[] aspects;

    public ResearchPageEntry() {

    }

    public ResearchPageEntry(ResearchPage page, int index) {
        switch (page.type) {
            case TEXT: {
                type = "text";
                number = index;
                text = StatCollector.translateToLocal(page.text);
                return;
            }
            case IMAGE: {
                type = "picture";
                number = index;
                text = StatCollector.translateToLocal(page.text);
                resource = page.image.toString();
                return;
            }
            case ARCANE_CRAFTING:
            case CRUCIBLE_CRAFTING:
            case INFUSION_CRAFTING:
            case NORMAL_CRAFTING:
            case SMELTING: {

                type = switch (page.type) {
                    case ARCANE_CRAFTING -> "arcane";
                    case CRUCIBLE_CRAFTING -> "crucible";
                    case INFUSION_CRAFTING -> "infusion";
                    case SMELTING -> "smelting";
                    default -> "crafting";
                };
                number = index;
                item = new ItemEntry();
                GameRegistry.UniqueIdentifier identifier = GameRegistry
                    .findUniqueIdentifierFor(page.recipeOutput.getItem());
                if (identifier == null) {
                    return;
                }
                item.item = identifier.toString();
                item.meta = page.recipeOutput.getItemDamage();
                item.amount = page.recipeOutput.stackSize;
                return;
            }
            case ASPECTS:
                type = "aspect";
                number = index;
                aspects = page.aspects.aspects.entrySet()
                    .stream()
                    .map(entry -> {
                        var aspectEntry = new AspectEntry();
                        aspectEntry.aspect = entry.getKey()
                            .getTag();
                        aspectEntry.amount = entry.getValue();
                        return aspectEntry;
                    })
                    .toArray(AspectEntry[]::new);
                return;
        }
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public ResourceLocation getResource() {
        return new ResourceLocation(resource);
    }

    public ResearchPage getPage() {
        ItemStack stack;
        switch (type) {
            case "arcane":
                stack = StringHelper.parseItemFromString(item.getItem());
                if (stack == null) {
                    return null;
                }
                stack.stackSize = item.getAmount();
                stack.setItemDamage(item.getMeta());
                for (Map.Entry<String, Object> entry : ConfigResearch.recipes.entrySet()) {
                    if (entry.getValue() instanceof ShapedArcaneRecipe
                        || entry.getValue() instanceof ShapelessArcaneRecipe) {
                        IArcaneRecipe recipe = (IArcaneRecipe) entry.getValue();
                        if (recipe.getRecipeOutput()
                            .isItemEqual(stack)) {
                            return new ResearchPage(recipe);
                        }
                    } else if (entry.getValue() instanceof ShapedArcaneRecipe[]
                        || entry.getValue() instanceof ShapelessArcaneRecipe[]) {
                            IArcaneRecipe[] recipe = (IArcaneRecipe[]) entry.getValue();
                            for (IArcaneRecipe recipe_ : recipe) {
                                if (recipe_.getRecipeOutput()
                                    .isItemEqual(stack)) {
                                    return new ResearchPage(recipe);
                                }
                            }
                        }
                }
                break;
            case "aspect":
                return new ResearchPage(this.getAspects());
            case "crafting":
                for (Object obj : CraftingManager.getInstance()
                    .getRecipeList()) {
                    if (obj instanceof ShapedRecipes || obj instanceof ShapelessRecipes) {
                        IRecipe recipe = (IRecipe) obj;
                        if (recipe.getRecipeOutput()
                            .isItemEqual(StringHelper.parseItemFromString(item.getItem()))) {
                            return new ResearchPage(recipe);
                        }
                    } else if (obj instanceof ShapedRecipes[] || obj instanceof ShapelessRecipes[]) {
                        IRecipe[] recipe = (IRecipe[]) obj;
                        for (IRecipe recipe_ : recipe) {
                            if (recipe_.getRecipeOutput()
                                .isItemEqual(StringHelper.parseItemFromString(item.getItem()))) {
                                return new ResearchPage(recipe_);
                            }
                        }
                    }
                }
            case "crucible":
                stack = StringHelper.parseItemFromString(item.getItem());
                if (stack == null) {
                    return null;
                }
                stack.stackSize = item.getAmount();
                stack.setItemDamage(item.getMeta());
                for (Map.Entry<String, Object> entry : ConfigResearch.recipes.entrySet()) {
                    if (entry.getValue() instanceof CrucibleRecipe recipe) {
                        if (recipe.getRecipeOutput()
                            .isItemEqual(stack)) {
                            return new ResearchPage(entry.getKey());
                        }
                    }
                }
                break;
            case "infusion":
                stack = StringHelper.parseItemFromString(item.getItem());
                if (stack == null) {
                    return null;
                }
                stack.stackSize = item.getAmount();
                stack.setItemDamage(item.getMeta());
                for (Map.Entry<String, Object> entry : ConfigResearch.recipes.entrySet()) {
                    if (entry.getValue() instanceof InfusionRecipe recipe) {
                        if (((ItemStack) recipe.getRecipeOutput()).isItemEqual(stack)) {
                            return new ResearchPage(entry.getKey());
                        }
                    }
                }
                break;
            case "smelting":
                stack = StringHelper.parseItemFromString(item.getItem());
                if (stack == null) {
                    return null;
                }
                stack.stackSize = item.getAmount();
                stack.setItemDamage(item.getMeta());
                for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.smelting()
                    .getSmeltingList()
                    .entrySet()) {
                    if (entry.getValue()
                        .isItemEqual(stack)) {
                        return new ResearchPage(entry.getKey());
                    }
                }
                break;
        }
        return null;
    }

    public int getNumber() {
        return number;
    }

    public AspectList getAspects() {
        AspectList aspectList = new AspectList();
        for (AspectEntry entry : aspects) {
            aspectList.add(Aspect.getAspect(entry.getAspect()), entry.getAmount());
        }
        return aspectList;
    }

}

class ItemEntry {

    public String item;
    public int meta;
    public int amount;

    public String getItem() {
        return item;
    }

    public int getMeta() {
        return meta;
    }

    public int getAmount() {
        return amount;
    }
}
