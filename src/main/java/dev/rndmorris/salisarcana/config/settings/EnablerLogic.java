package dev.rndmorris.salisarcana.config.settings;

import dev.rndmorris.salisarcana.config.IEnabler;

public abstract class EnablerLogic implements IEnabler {

    public static Not not(IEnabler inputSetting) {
        return new Not(inputSetting);
    }

    public static And and(IEnabler... inputSettings) {
        return new And(inputSettings);
    }

    public static class Not extends EnablerLogic {

        private final IEnabler inputSetting;

        public Not(IEnabler inputSetting) {
            this.inputSetting = inputSetting;
        }

        @Override
        public boolean isEnabled() {
            return !inputSetting.isEnabled();
        }
    }

    public static class And extends EnablerLogic {

        private final IEnabler[] inputSettings;

        public And(IEnabler... inputSettings) {
            this.inputSettings = inputSettings;
        }

        @Override
        public boolean isEnabled() {
            for (var input : inputSettings) {
                if (input != null && !input.isEnabled()) {
                    return false;
                }
            }
            return true;
        }
    }

}
