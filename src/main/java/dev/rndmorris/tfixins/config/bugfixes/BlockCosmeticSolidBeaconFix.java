package dev.rndmorris.tfixins.config.bugfixes;

import java.lang.ref.WeakReference;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.ConfigPhase;
import dev.rndmorris.tfixins.config.IConfigModule;
import dev.rndmorris.tfixins.config.settings.Setting;

public class BlockCosmeticSolidBeaconFix extends Setting {

    private final boolean[] blockCosmeticSolidBeaconIds = new boolean[16];
    private final WeakReference<IConfigModule> parentModule;

    public BlockCosmeticSolidBeaconFix(IConfigModule module, ConfigPhase phase) {
        super(module, phase);
        parentModule = new WeakReference<>(module);
    }

    public boolean isBeaconMetadata(int id) {
        return !isEnabled() || blockCosmeticSolidBeaconIds[id % blockCosmeticSolidBeaconIds.length];
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        final var module = parentModule.get();
        if (module == null) {
            return;
        }
        final var beaconIds = configuration.get(
            module.getModuleId(),
            "BlockCosmeticSolid Beacon Ids",
            new int[] { 4 },
            "Which metadata values of BlockCosmeticSolid are considered beacon base blocks. Default: 4 (Thaumium Block).",
            0,
            15)
            .getIntList();
        for (var id : beaconIds) {
            blockCosmeticSolidBeaconIds[id % blockCosmeticSolidBeaconIds.length] = true;
        }
    }
}
