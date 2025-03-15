package dev.rndmorris.salisarcana.config.settings;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.text.WordUtils;

import dev.rndmorris.salisarcana.config.IEnabler;

public class ReplaceWandComponentSettings extends CustomResearchSetting {

    public ReplaceWandComponentSettings(IEnabler dependency, @Nonnull Component component, ResearchInfo info) {
        super(
            dependency,
            "replaceWand" + component.nameProper(),
            "Enable a recipe to swap an existing existing wand, scepter, or staff's " + component.nameLower() + ".",
            info);
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
