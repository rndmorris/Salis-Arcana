package dev.rndmorris.salisarcana.lib.customresearch.pages;

import net.minecraft.util.StatCollector;

import dev.rndmorris.salisarcana.lib.AssetHelper;
import thaumcraft.api.research.ResearchPage;

public class TextResearchPageEntry extends ResearchPageEntry {

    String text;

    public TextResearchPageEntry() {}

    public TextResearchPageEntry(ResearchPage page, Integer index) {
        this.number = index;
        this.type = "text";
        this.text = StatCollector.translateToLocal(page.text);
    }

    public String getText() {
        return text;
    }

    @Override
    public ResearchPage getPage() {
        return new ResearchPage(this.getText());
    }

    @Override
    public void createLangEntries(String key) {
        AssetHelper.addLangEntry(AssetHelper.lookupLangEntryByValue(this.getText()), this.getText());
        AssetHelper.addLangEntry("tc_research_page." + key + "." + this.getNumber(), this.getText());
    }
}
