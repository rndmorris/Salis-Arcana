package dev.rndmorris.salisarcana.lib.customresearch.pages;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import dev.rndmorris.salisarcana.lib.TranslationManager;
import thaumcraft.api.research.ResearchPage;

public class PictureResearchPageEntry extends ResearchPageEntry {

    String text;
    String resource;

    public PictureResearchPageEntry() {}

    public PictureResearchPageEntry(ResearchPage page, Integer index) {
        this.type = "picture";
        this.text = StatCollector.translateToLocal(page.text);
        this.resource = page.image.toString();
        this.number = index;
    }

    public String getText() {
        return text;
    }

    public ResourceLocation getResource() {
        return new ResourceLocation(resource);
    }

    @Override
    public ResearchPage getPage() {
        return new ResearchPage(this.getResource(), this.getText());
    }

    @Override
    public void createLangEntries(String key) {
        TranslationManager.setLangEntry("tc_research_page." + key + "." + this.getNumber(), this.getText());
    }
}
