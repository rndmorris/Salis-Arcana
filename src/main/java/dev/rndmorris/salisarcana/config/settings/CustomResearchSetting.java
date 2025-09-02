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
    public int warp;
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
        this.warp = researchInfo.getWarp();

        this.configName = configName + "Research";
        this.configComment = configComment;

    }

    public String getInternalName() {
        return MODID + ":" + this.researchName;
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        this.enabled = configuration
            .getBoolean("_enabled" + this.configName, this.getCategory(), this.enabled, this.configComment);

        this.researchCategory = configuration.getString(
            this.configName + "Category",
            this.getCategory(),
            this.researchCategory,
            "The tab in the Thaumonomicon in which the research should appear");

        this.researchCol = configuration.getInt(
            this.configName + "Col",
            this.getCategory(),
            this.researchCol,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            "The column in the given category at which the research should appear");

        this.researchRow = configuration.getInt(
            this.configName + "Row",
            this.getCategory(),
            this.researchRow,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            "The row in the given category at which the research should appear");

        this.parentResearches = configuration.getStringList(
            this.configName + "Parents",
            this.getCategory(),
            this.parentResearches,
            "The research entry IDs of the parent research entries");

        this.purchasable = configuration.getBoolean(
            this.configName + "Purchasable",
            this.getCategory(),
            this.purchasable,
            "Whether the research should be purchasable with aspects instead of the normal minigame");

        this.autoUnlock = configuration.getBoolean(
            this.configName + "AutoUnlock",
            this.getCategory(),
            this.autoUnlock,
            "Whether the research should automatically unlock as soon as its parents are researched.");

        this.warp = configuration.getInt(
            this.configName + "Warp",
            this.getCategory(),
            this.warp,
            0,
            100,
            "How much warp is applied upon researching this research.");

        this.aspectStrings = configuration.getStringList(
            this.configName + "Aspects",
            this.getCategory(),
            this.aspectStrings,
            "The aspects required for the research entry");

    }

    public AspectList getAspects() {
        AspectList researchAspects = new AspectList();
        for (String aspect : this.aspectStrings) {
            String[] aspectParts = aspect.split(":");
            if (aspectParts.length == 2) {
                if (Aspect.aspects.get(aspectParts[0]) == null) {
                    LOG.error(
                        "Error: Aspect {} in custom research {} does not exist!",
                        aspectParts[0],
                        this.configName);
                    continue;
                }

                int amount;
                try {
                    amount = Integer.parseInt(aspectParts[1]);
                } catch (NumberFormatException e) {
                    LOG.error(
                        "Cannot parse amount of aspects in value \"{}\" in config setting \"{}Aspects\".",
                        aspect,
                        this.configName);
                    continue;
                }

                if (amount > 0) {
                    researchAspects.add(Aspect.getAspect(aspectParts[0]), amount);
                } else {
                    LOG.error(
                        "Invalid amount of aspect {} in value \"{}\" in config setting \"{}Aspects\".",
                        aspectParts[0],
                        aspect,
                        this.configName);
                }
            } else {
                LOG.error(
                    "Invalid aspect string \"{}\" in config setting \"{}Aspects\". Each value must be formatted as \"aspect:amount\".",
                    aspect,
                    this.configName);
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
        private int warp = 0;

        public ResearchInfo(String researchName, String researchCategory, int researchCol, int researchRow) {
            this.researchName = researchName;
            this.researchCategory = researchCategory;
            this.researchCol = researchCol;
            this.researchRow = researchRow;
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

        public ResearchInfo setWarp(int warp) {
            this.warp = warp;
            return this;
        }

        public String getResearchName() {
            return this.researchName;
        }

        public String getResearchCategory() {
            return this.researchCategory;
        }

        public int getResearchCol() {
            return this.researchCol;
        }

        public int getResearchRow() {
            return this.researchRow;
        }

        public String[] getParents() {
            return this.researchParents;
        }

        public boolean isPurchasable() {
            return this.purchasable;
        }

        public String[] getResearchAspects() {
            return this.researchAspects;
        }

        public int getDifficulty() {
            return this.difficulty;
        }

        public boolean getAutoUnlock() {
            return this.autoUnlock;
        }

        public int getWarp() {
            return this.warp;
        }
    }
}
