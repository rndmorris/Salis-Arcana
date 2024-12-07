package dev.rndmorris.tfixins.config.bugfixes;

import java.lang.ref.WeakReference;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.IConfigModule;
import dev.rndmorris.tfixins.config.Setting;

public class BlockCosmeticSolidBeaconFix extends Setting {

    private final boolean[] blockCosmeticSolidBeaconIds = new boolean[16];

    public BlockCosmeticSolidBeaconFix(WeakReference<IConfigModule> getModule) {
        super(getModule);
    }

    public boolean isBeaconMetadata(int id) {
        return !isEnabled() || blockCosmeticSolidBeaconIds[id % blockCosmeticSolidBeaconIds.length];
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        IConfigModule module;
        if ((module = moduleRef.get()) == null) {
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
