package dev.rndmorris.salisarcana.lib.customresearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.rndmorris.salisarcana.lib.TranslationManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.google.gson.annotations.SerializedName;

import cpw.mods.fml.common.registry.GameRegistry;
import dev.rndmorris.salisarcana.lib.AssetHelper;
import dev.rndmorris.salisarcana.lib.R;
import dev.rndmorris.salisarcana.lib.StringHelper;
import dev.rndmorris.salisarcana.lib.customresearch.pages.ResearchPageEntry;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class ResearchEntry {

    private boolean enabled;
    @SerializedName("action")
    private String type;
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
            this.pages[j] = ResearchPageEntry.create(research.getPages()[j], j);
        }

    }

    public boolean isEnabled() {
        return enabled;
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
        return AspectEntry.getAspects(aspects);
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
        switch (this.type) {
            case "replace", "create" -> this.doReplace(research);
            case "update" -> this.doUpdate(research);
        }
    }

    private void doReplace(ResearchItem research) {
        R r = new R(research);
        if (!research.category.equals(this.getCategory())) {
            r.set("category", this.getCategory());
            ResearchCategories.researchCategories.get(research.category).research.remove(research.key);
            ResearchCategories.researchCategories.get(research.category).research.put(research.key, research);
        }
        String key = TranslationManager.lookupLangEntryByValue(research.getName());
        if (key == null) {
            key = research.getName();
        }
        TranslationManager.setLangEntry(key, this.getName());
        key = TranslationManager.lookupLangEntryByValue(research.getText());
        if (key == null) {
            key = research.getText();
        }
        TranslationManager.setLangEntry(key, this.getTooltip());

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
            entry.createLangEntries(this.getKey());
            pages.add(entry.getPage());
        }
        research.setPages(pages.toArray(new ResearchPage[0]));
    }

    private void doUpdate(ResearchItem research) {
        R r = new R(research);
        if (this.category != null && !this.category.isEmpty() && !this.category.equals(research.category)) {
            ResearchCategories.researchCategories.get(research.category).research.remove(research.key);
            ResearchCategories.researchCategories.get(this.category).research.put(research.key, research);
            r.set("category", this.category);
        }
        if (this.name != null && !this.name.isEmpty() && !this.name.equals(research.getName())) {
            String key = TranslationManager.lookupLangEntryByValue(research.getName());
            if (key == null) {
                key = research.getName();
            }
            TranslationManager.setLangEntry(key, this.getName());
        }
        if (this.tooltip != null && !this.tooltip.isEmpty() && !this.tooltip.equals(research.getText())) {
            String key = TranslationManager.lookupLangEntryByValue(research.getText());
            if (key == null) {
                key = research.getText();
            }
            TranslationManager.setLangEntry(key, this.getTooltip());
        }
        if (this.aspects != null && this.aspects.length > 0) {
            r.set("tags", this.getAspects());
        }
        if (this.parents != null && this.parents.length > 0) {
            research.parents = this.parents;
        }
        if (this.parentsHidden != null && this.parentsHidden.length > 0) {
            research.parentsHidden = this.parentsHidden;
        }
        if (this.siblings != null && this.siblings.length > 0) {
            research.siblings = this.siblings;
        }
        if (this.displayColumn != research.displayColumn) {
            r.set("displayColumn", this.displayColumn);
        }
        if (this.displayRow != research.displayRow) {
            r.set("displayRow", this.displayRow);
        }
        if (this.icon_item != null && !this.icon_item.isEmpty()) {
            r.set("icon_item", this.getIconItem());
        }
        if (this.icon_resource != null && !this.icon_resource.isEmpty()) {
            r.set("icon_resource", this.getIconResource());
        }
        if (this.complexity != research.getComplexity()) {
            research.setComplexity(this.complexity);
        }
        if (this.isSpecial != research.isSpecial()) {
            r.set("isSpecial", this.isSpecial);
        }
        if (this.isSecondary != research.isSecondary()) {
            r.set("isSecondary", this.isSecondary);
        }
        if (this.isRound != research.isRound()) {
            r.set("isRound", this.isRound);
        }
        if (this.isStub != research.isStub()) {
            r.set("isStub", this.isStub);
        }
        if (this.isVirtual != research.isVirtual()) {
            r.set("isVirtual", this.isVirtual);
        }
        if (this.isConcealed != research.isConcealed()) {
            r.set("isConcealed", this.isConcealed);
        }
        if (this.isHidden != research.isHidden()) {
            r.set("isHidden", this.isHidden);
        }
        if (this.isLost != research.isLost()) {
            r.set("isLost", this.isLost);
        }
        if (this.isAutoUnlock != research.isAutoUnlock()) {
            r.set("isAutoUnlock", this.isAutoUnlock);
        }
        if (this.itemTriggers != null && !Arrays.equals(this.getItemTriggers(), research.getItemTriggers())) {
            research.setItemTriggers(this.getItemTriggers());
        }
        if (this.entityTriggers != null && !Arrays.equals(this.entityTriggers, research.getEntityTriggers())) {
            research.setEntityTriggers(this.entityTriggers);
        }
        if (this.aspectTriggers != null && !Arrays.equals(this.getAspectTriggers(), research.getAspectTriggers())) {
            r.set("aspectTriggers", this.getAspectTriggers());
        }
        ResearchPage[] originalPages = research.getPages();
        int max = Math.max(
            Arrays.stream(this.pages)
                .mapToInt(ResearchPageEntry::getNumber)
                .max()
                .orElse(0),
            originalPages.length);
        ResearchPage[] newPages = new ResearchPage[max];
        System.arraycopy(originalPages, 0, newPages, 0, originalPages.length);
        for (ResearchPageEntry page : this.pages) {
            page.createLangEntries(this.getKey());
            newPages[page.getNumber()] = page.getPage();
        }
        r.set("pages", newPages);
    }

    public String getType() {
        return type;
    }
}
