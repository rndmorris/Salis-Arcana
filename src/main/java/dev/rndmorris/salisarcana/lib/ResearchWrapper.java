package dev.rndmorris.salisarcana.lib;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public final class ResearchWrapper extends ResearchItem {
    public final ResearchItem original;
    public boolean overrideName = false;
    public boolean overrideText = false;
    private ResearchWrapper(ResearchItem original, ResourceLocation icon) {
        super(original.key, original.category, original.tags, original.displayColumn, original.displayRow, original.getComplexity(), icon);
        this.original = original;
    }

    private ResearchWrapper(ResearchItem original, ItemStack icon) {
        super(original.key, original.category, original.tags, original.displayColumn, original.displayRow, original.getComplexity(), icon);
        this.original = original;
    }

    public static ResearchWrapper wrap(ResearchItem original) {
        if(original instanceof ResearchWrapper wrapper) {
            return wrapper;
        }

        ResearchWrapper wrapper;
        if(original.icon_resource != null) {
            wrapper = new ResearchWrapper(original, original.icon_resource);
        } else {
            wrapper = new ResearchWrapper(original, original.icon_item);
        }

        ResearchCategories.researchCategories.get(original.category).research.put(original.key, wrapper);

        return wrapper;
    }

    @Override
    public ResearchItem setSpecial() {
        return this.original.setSpecial();
    }

    @Override
    public ResearchItem setStub() {
        return this.original.setStub();
    }

    @Override
    public ResearchItem setLost() {
        return this.original.setLost();
    }

    @Override
    public ResearchItem setConcealed() {
        return this.original.setConcealed();
    }

    @Override
    public ResearchItem setHidden() {
        return this.original.setHidden();
    }

    @Override
    public ResearchItem setVirtual() {
        return this.original.setVirtual();
    }

    @Override
    public ResearchItem setParents(String... par) {
        return this.original.setParents(par);
    }

    @Override
    public ResearchItem setParentsHidden(String... par) {
        return this.original.setParentsHidden(par);
    }

    @Override
    public ResearchItem setSiblings(String... sib) {
        return this.original.setSiblings(sib);
    }

    @Override
    public ResearchItem setPages(ResearchPage... par) {
        return this.original.setPages(par);
    }

    @Override
    public ResearchPage[] getPages() {
        return this.original.getPages();
    }

    @Override
    public ResearchItem setItemTriggers(ItemStack... par) {
        return this.original.setItemTriggers(par);
    }

    @Override
    public ResearchItem setEntityTriggers(String... par) {
        return this.original.setEntityTriggers(par);
    }

    @Override
    public ResearchItem setAspectTriggers(Aspect... par) {
        return this.original.setAspectTriggers(par);
    }

    @Override
    public ItemStack[] getItemTriggers() {
        return this.original.getItemTriggers();
    }

    @Override
    public String[] getEntityTriggers() {
        return this.original.getEntityTriggers();
    }

    @Override
    public Aspect[] getAspectTriggers() {
        return this.original.getAspectTriggers();
    }

    @Override
    public ResearchItem registerResearchItem() {
        return this.original.registerResearchItem();
    }

    @Override
    public String getName() {
        if(overrideName) {
            return StatCollector.translateToLocal("tc.research_name." + this.key);
        } else {
            return this.original.getName();
        }
    }

    @Override
    public String getText() {
        if(overrideText) {
            return StatCollector.translateToLocal("tc.research_text." + this.key);
        } else {
            return this.original.getText();
        }
    }

    @Override
    public boolean isSpecial() {
        return this.original.isSpecial();
    }

    @Override
    public boolean isStub() {
        return this.original.isStub();
    }

    @Override
    public boolean isLost() {
        return this.original.isLost();
    }

    @Override
    public boolean isConcealed() {
        return this.original.isConcealed();
    }

    @Override
    public boolean isHidden() {
        return this.original.isHidden();
    }

    @Override
    public boolean isVirtual() {
        return this.original.isVirtual();
    }

    @Override
    public boolean isAutoUnlock() {
        return this.original.isAutoUnlock();
    }

    @Override
    public ResearchItem setAutoUnlock() {
        return this.original.setAutoUnlock();
    }

    @Override
    public boolean isRound() {
        return this.original.isRound();
    }

    @Override
    public ResearchItem setRound() {
        return this.original.setRound();
    }

    @Override
    public boolean isSecondary() {
        return this.original.isSecondary();
    }

    @Override
    public ResearchItem setSecondary() {
        return this.original.setSecondary();
    }

    @Override
    public int getComplexity() {
        return this.original.getComplexity();
    }

    @Override
    public ResearchItem setComplexity(int complexity) {
        return this.original.setComplexity(complexity);
    }

    @Override
    public Aspect getResearchPrimaryTag() {
        return this.original.getResearchPrimaryTag();
    }
}
