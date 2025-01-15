package dev.rndmorris.salisarcana.config.settings;

import net.minecraftforge.common.config.Configuration;

import org.apache.commons.lang3.text.WordUtils;

import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.IEnabler;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class CustomResearchSetting extends Setting {

    public final String configComment;
    public final String configName;
    public String researchName;
    public String researchCategory;
    public int researchCol;
    public int researchRow;
    public int difficulty;
    public String[] parentResearches;
    public ToggleSetting pairedSetting;
    public boolean purchasable;
    public String[] aspectStrings;
    public AspectList researchAspects;
    public boolean autoUnlock;

    public CustomResearchSetting(IEnabler dependency, ConfigPhase phase, String configName, String configComment,
        ResearchInfo researchInfo) {
        super(dependency, phase);

        this.researchName = researchInfo.getResearchName();
        this.researchCategory = researchInfo.getResearchCategory();
        this.researchCol = researchInfo.getResearchCol();
        this.researchRow = researchInfo.getResearchRow();
        this.parentResearches = researchInfo.getParents();
        this.difficulty = researchInfo.getDifficulty();
        this.autoUnlock = researchInfo.getAutoUnlock();
        this.aspectStrings = researchInfo.getResearchAspects();

        this.configName = configName + "Research";
        this.configComment = configComment;

        this.pairedSetting = new ToggleSetting(
            dependency,
            phase,
            "_enable" + WordUtils.capitalize(this.configName),
            configComment);
        this.category = configName + "Research";
        this.pairedSetting.setCategory(this.category);

        this.enabled = this.pairedSetting.isEnabled();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Setting> T setCategory(String category) {
        this.category = category;
        if (this.pairedSetting != null) {
            this.pairedSetting.setCategory(category);
        }
        return (T) this;
    }

    @SuppressWarnings({ "unchecked", "unused" })
    public <T extends CustomResearchSetting> T setPairedSetting(ToggleSetting pairedSetting) {
        this.pairedSetting = pairedSetting;
        return (T) this;
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        if (this.pairedSetting != null) {
            this.pairedSetting.loadFromConfiguration(configuration);
            this.enabled = this.pairedSetting.isEnabled();
        }

        researchName = configuration
            .getString(configName + "Name", this.category, researchName, "The research entry ID");

        researchCategory = configuration.getString(
            configName + "Category",
            this.category,
            researchCategory,
            "The tab in the Thaumonomicon in which the research should appear");

        researchCol = configuration.getInt(
            configName + "Col",
            this.category,
            researchCol,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            "The column in the given category at which the research should appear");

        researchRow = configuration.getInt(
            configName + "Row",
            this.category,
            researchRow,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            "The row in the given category at which the research should appear");

        parentResearches = configuration.getStringList(
            configName + "Parents",
            this.category,
            parentResearches,
            "The research entry IDs of the parent research entries");

        purchasable = configuration.getBoolean(
            configName + "Purchasable",
            this.category,
            purchasable,
            "Whether the research should be purchasable with aspects instead of the normal minigame");

        this.aspectStrings = configuration.getStringList(
            configName + "Aspects",
            this.category,
            this.aspectStrings,
            "The aspects required for the research entry");
        this.researchAspects = new AspectList();
        for (String aspect : aspectStrings) {
            String[] aspectParts = aspect.split(":");
            if (aspectParts.length == 2) {
                if (Aspect.aspects.get(aspectParts[0]) == null) {
                    throw new IllegalArgumentException("Invalid aspect: " + aspectParts[0]);
                }
                researchAspects.add(Aspect.getAspect(aspectParts[0]), Integer.parseInt(aspectParts[1]));
            }
        }
    }

    @Override
    public boolean isEnabled() {
        if (pairedSetting != null) {
            return super.isEnabled() && pairedSetting.isEnabled();
        }
        return super.isEnabled() && this.enabled;
    }

    @SuppressWarnings("unused")
    public static class ResearchInfo {

        public int researchRow;
        public int researchCol;
        public int difficulty;
        public String researchCategory;
        public String researchName;
        public String[] researchParents = new String[0];
        public boolean purchasable = false;
        public String[] researchAspects = new String[0];
        public boolean autoUnlock = false;

        public ResearchInfo() {

        }

        public ResearchInfo(String researchName, String researchCategory, int researchCol, int researchRow,
            int difficulty) {
            this.researchName = researchName;
            this.researchCategory = researchCategory;
            this.researchCol = researchCol;
            this.researchRow = researchRow;
        }

        public ResearchInfo(String researchName, String researchCategory, int researchCol, int researchRow,
            int difficulty, boolean purchasable) {
            this.researchName = researchName;
            this.researchCategory = researchCategory;
            this.researchCol = researchCol;
            this.researchRow = researchRow;
            this.purchasable = purchasable;
        }

        public ResearchInfo setResearchName(String researchName) {
            this.researchName = researchName;
            return this;
        }

        public ResearchInfo setResearchCategory(String researchCategory) {
            this.researchCategory = researchCategory;
            return this;
        }

        public ResearchInfo setResearchCol(int researchCol) {
            this.researchCol = researchCol;
            return this;
        }

        public ResearchInfo setResearchRow(int researchRow) {
            this.researchRow = researchRow;
            return this;
        }

        public ResearchInfo setParents(String... parentResearch) {
            this.researchParents = parentResearch;
            return this;
        }

        public ResearchInfo setPurchasable(boolean purchasable) {
            this.purchasable = purchasable;
            return this;
        }

        public ResearchInfo setAspects(String... aspects) {
            this.researchAspects = aspects;
            return this;
        }

        public ResearchInfo setDifficulty(int difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public ResearchInfo setAutoUnlock() {
            this.autoUnlock = true;
            return this;
        }

        public String getResearchName() {
            return researchName;
        }

        public String getResearchCategory() {
            return researchCategory;
        }

        public int getResearchCol() {
            return researchCol;
        }

        public int getResearchRow() {
            return researchRow;
        }

        public String[] getParents() {
            return researchParents;
        }

        public boolean isPurchasable() {
            return purchasable;
        }

        public String[] getResearchAspects() {
            return researchAspects;
        }

        public int getDifficulty() {
            return difficulty;
        }

        public boolean getAutoUnlock() {
            return autoUnlock;
        }

    }
}
