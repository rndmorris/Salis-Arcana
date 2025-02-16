package dev.rndmorris.salisarcana.lib.customresearch.pages;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

import thaumcraft.api.research.ResearchPage;

public abstract class ResearchPageEntry {

    static HashMap<ResearchPage.PageType, Class<? extends ResearchPageEntry>> types = new HashMap<>() {

        {
            put(ResearchPage.PageType.TEXT, TextResearchPageEntry.class);
            put(ResearchPage.PageType.IMAGE, PictureResearchPageEntry.class);
            put(ResearchPage.PageType.ASPECTS, AspectResearchPageEntry.class);
            put(ResearchPage.PageType.ARCANE_CRAFTING, ArcaneResearchPageEntry.class);
            put(ResearchPage.PageType.CRUCIBLE_CRAFTING, CrucibleResearchPageEntry.class);
            put(ResearchPage.PageType.INFUSION_CRAFTING, InfusionResearchPageEntry.class);
            put(ResearchPage.PageType.NORMAL_CRAFTING, CraftingResearchPageEntry.class);
            put(ResearchPage.PageType.SMELTING, SmeltingResearchPageEntry.class);
        }
    };

    @SerializedName("pageType")
    public transient String type;
    public int number;

    public ResearchPageEntry() {

    }

    public static ResearchPageEntry create(ResearchPage page, int j) {
        try {
            return types.get(page.type)
                .getDeclaredConstructor(ResearchPage.class, Integer.class)
                .newInstance(page, j);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
            | InvocationTargetException e) {
            LOG.error("Error creating research page entry for page type {}", page.type, e);
        }
        return null;
    }

    public String getType() {
        return type;
    }

    abstract public ResearchPage getPage();

    public int getNumber() {
        return number;
    }

    public void createLangEntries(String key) {}
}
