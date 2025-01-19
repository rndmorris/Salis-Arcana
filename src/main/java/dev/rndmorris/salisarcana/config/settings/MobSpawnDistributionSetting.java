package dev.rndmorris.salisarcana.config.settings;

import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.IEnabler;

public class MobSpawnDistributionSetting extends EnumSetting<MobSpawnDistributionSetting.Options> {

    public MobSpawnDistributionSetting(IEnabler dependency, ConfigPhase phase, String name, String comment) {
        super(dependency, phase, name, comment, Options.DEFAULT);
    }

    public boolean isEvenSpread() {
        return this.value == Options.EVEN_SPREAD;
    }

    public boolean isCenterWeighted() {
        return this.value == Options.CENTER_WEIGHTED;
    }

    public enum Options {
        DEFAULT,
        EVEN_SPREAD,
        CENTER_WEIGHTED;
    }
}
