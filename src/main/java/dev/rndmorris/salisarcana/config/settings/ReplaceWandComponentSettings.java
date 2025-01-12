package dev.rndmorris.salisarcana.config.settings;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import org.apache.commons.lang3.text.WordUtils;

import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.IEnabler;

public class ReplaceWandComponentSettings extends Setting {

    private final @Nonnull Component component;
    private String researchCategory = "THAUMATURGY";
    private int researchCol;
    private int researchRow;

    public ReplaceWandComponentSettings(IEnabler dependency, ConfigPhase phase, @Nonnull Component component,
        int defaultCol, int defaultRow) {
        super(dependency, phase);
        this.component = component;
        researchCol = defaultCol;
        researchRow = defaultRow;
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        enabled = configuration.getBoolean(
            "enableReplaceWand" + component.nameProper() + "Recipe",
            getCategory(),
            enabled,
            "Enable a recipe to swap an existing existing wand, scepter, or staff's  " + component.nameLower() + ".");
        researchCategory = configuration.getString(
            "replace" + component.nameProper() + "ResearchCategory",
            getCategory(),
            researchCategory,
            "Which tab of the Thaumonomicon on which the research to replace a wand's " + component.nameLower()
                + " should appear.");
        researchCol = configuration.getInt(
            "replace" + component.nameProper() + "ResearchColumn",
            getCategory(),
            researchCol,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            "Which column in the Thaumonomicon at which the research to replace a wand's " + component.nameLower()
                + " should appear.");
        researchRow = configuration.getInt(
            "replace" + component.nameProper() + "ResearchRow",
            getCategory(),
            researchRow,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            "Which row in the Thaumonomicon at which the research to replace a wand's " + component.nameLower()
                + " should appear.");
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

    public enum Component {

        CAPS,
        CORE;

        public String nameLower() {
            return toString().toLowerCase();
        }

        public String nameProper() {
            return WordUtils.capitalizeFully(toString());
        }
    }
}
