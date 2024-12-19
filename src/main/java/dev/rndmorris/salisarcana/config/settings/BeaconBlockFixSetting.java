package dev.rndmorris.salisarcana.config.settings;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.IEnabler;

public class BeaconBlockFixSetting extends IntArraySetting {

    private static final String configName = "beaconBlockMetadataValues";
    private static final String comment = "Which metadata values of BlockCosmeticSolid are considered beacon base blocks. Default: 4 (Thaumium Block).";

    private final boolean[] isBeaconMetadata = new boolean[16];

    public BeaconBlockFixSetting(IEnabler dependency, ConfigPhase phase) {
        super(dependency, phase, configName, comment, new int[] { 4 }, 0, 15);
    }

    public boolean isBeaconMetadata(int metadata) {
        return isBeaconMetadata[metadata % isBeaconMetadata.length];
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        super.loadFromConfiguration(configuration);
        for (var metadata : value) {
            isBeaconMetadata[metadata % isBeaconMetadata.length] = true;
        }
    }
}
