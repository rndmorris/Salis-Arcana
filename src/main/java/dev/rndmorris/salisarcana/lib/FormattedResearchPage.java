package dev.rndmorris.salisarcana.lib;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import thaumcraft.api.research.ResearchPage;

public class FormattedResearchPage extends ResearchPage {

    public final Object[] formattingData;

    public FormattedResearchPage(String text, Object[] formattingData) {
        super(text);
        this.formattingData = formattingData;
    }

    public FormattedResearchPage(String research, String text, Object[] formattingData) {
        super(research, text);
        this.formattingData = formattingData;
    }

    public FormattedResearchPage(ResourceLocation image, String caption, Object[] formattingData) {
        super(image, caption);
        this.formattingData = formattingData;
    }

    @Override
    public String getTranslatedText() {
        return StatCollector.translateToLocalFormatted(this.text, formattingData);
    }
}
