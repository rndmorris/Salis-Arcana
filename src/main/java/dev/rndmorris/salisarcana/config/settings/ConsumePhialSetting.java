package dev.rndmorris.salisarcana.config.settings;

import javax.annotation.Nonnull;

import dev.rndmorris.salisarcana.config.IEnabler;

public class ConsumePhialSetting extends EnumSetting<ConsumePhialSetting.Option> {

    public ConsumePhialSetting(IEnabler dependency, String name, String comment, @Nonnull Option defaultValue) {
        super(dependency, name, comment, defaultValue);
    }

    @Override
    public boolean isEnabled() {
        return this.value != Option.PHIAL && super.isEnabled();
    }

    public boolean consumeEssentia() {
        return this.value == Option.ESSENTIA;
    }

    public enum Option {
        FREE,
        ESSENTIA,
        PHIAL
    }
}
