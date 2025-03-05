package dev.rndmorris.salisarcana.lib.customresearch.pages;

import dev.rndmorris.salisarcana.lib.customresearch.AspectEntry;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchPage;

public class AspectResearchPageEntry extends ResearchPageEntry {

    public AspectEntry[] aspects;

    public AspectResearchPageEntry() {}

    public AspectResearchPageEntry(ResearchPage page, Integer index) {
        this.number = index;
        this.type = "aspect";
        this.aspects = page.aspects.aspects.entrySet()
            .stream()
            .map(entry -> {
                var aspectEntry = new AspectEntry();
                aspectEntry.aspect = entry.getKey()
                    .getTag();
                aspectEntry.amount = entry.getValue();
                return aspectEntry;
            })
            .toArray(AspectEntry[]::new);
    }

    public AspectList getAspects() {
        return AspectEntry.getAspects(aspects);
    }

    @Override
    public ResearchPage getPage() {
        return new ResearchPage(getAspects());
    }
}
