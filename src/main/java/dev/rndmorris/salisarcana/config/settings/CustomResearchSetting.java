package dev.rndmorris.salisarcana.config.settings;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;
import static dev.rndmorris.salisarcana.SalisArcana.MODID;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.IEnabler;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class CustomResearchSetting extends Setting {

    // Comment to be used in the paired togglesetting, will be at the top as a general explanation for what the research
    // does. Paired setting is optional.
    public final String configComment;

    // The base name of the config entry, will be used to generate the config entries for the research.
    public final String configName;

    // Research Info
    public String researchName;
    public String researchCategory;
    public int researchCol;
    public int researchRow;
    public int difficulty;
    public String[] parentResearches;
    public boolean purchasable;
    public boolean autoUnlock;

    public String[] aspectStrings; // formatted aspect:amount

    public CustomResearchSetting(IEnabler dependency, String configName, String configComment,
        ResearchInfo researchInfo) {
        super(dependency);

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

    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        this.enabled = configuration
            .getBoolean("_enabled" + this.configName, this.getCategory(), this.enabled, this.configComment);
        researchName = MODID + ":"
            + configuration.getString(configName + "Name", this.getCategory(), researchName, "The research entry ID");

        researchCategory = configuration.getString(
            configName + "Category",
            this.getCategory(),
            researchCategory,
            "The tab in the Thaumonomicon in which the research should appear");

        researchCol = configuration.getInt(
            configName + "Col",
            this.getCategory(),
            researchCol,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            "The column in the given category at which the research should appear");

        researchRow = configuration.getInt(
            configName + "Row",
            this.getCategory(),
            researchRow,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            "The row in the given category at which the research should appear");

        parentResearches = configuration.getStringList(
            configName + "Parents",
            this.getCategory(),
            parentResearches,
            "The research entry IDs of the parent research entries");

        purchasable = configuration.getBoolean(
            configName + "Purchasable",
            this.getCategory(),
            purchasable,
            "Whether the research should be purchasable with aspects instead of the normal minigame");

        this.aspectStrings = configuration.getStringList(
            configName + "Aspects",
            this.getCategory(),
            this.aspectStrings,
            "The aspects required for the research entry");

    }

    public AspectList getAspects() {
        AspectList researchAspects = new AspectList();
        for (String aspect : aspectStrings) {
            String[] aspectParts = aspect.split(":");
            if (aspectParts.length == 2) {
                if (Aspect.aspects.get(aspectParts[0]) == null) {
                    LOG.error(
                        "Error: Aspect {} in custom research {} does not exist!",
                        aspectParts[0],
                        this.configName);
                    continue;
                }
                researchAspects.add(Aspect.getAspect(aspectParts[0]), Integer.parseInt(aspectParts[1]));
            }
        }
        return researchAspects;
    }

    // Helper class for research info to be used in the constructor, avoids ugly constructor calls
    @SuppressWarnings("unused")
    public static class ResearchInfo {

        private int researchRow;
        private int researchCol;
        private int difficulty;
        private String researchCategory;
        private String researchName;
        private String[] researchParents = new String[0];
        private boolean purchasable = false;
        private String[] researchAspects = new String[0];
        private boolean autoUnlock = false;

        public ResearchInfo(String researchName, String researchCategory, int researchCol, int researchRow) {
            this.researchName = MODID + ":" + researchName;
            this.researchCategory = researchCategory;
            this.researchCol = researchCol;
            this.researchRow = researchRow;
        }

        public ResearchInfo setResearchName(String researchName) {
            this.researchName = MODID + ":" + researchName;
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
