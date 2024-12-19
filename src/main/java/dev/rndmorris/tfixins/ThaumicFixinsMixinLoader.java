package dev.rndmorris.tfixins;

import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

import dev.rndmorris.tfixins.config.ConfigPhase;
import dev.rndmorris.tfixins.config.ConfigModuleRoot;
import dev.rndmorris.tfixins.mixins.Mixins;

@LateMixin
public class ThaumicFixinsMixinLoader implements ILateMixinLoader {

    public ThaumicFixinsMixinLoader() {
        ConfigModuleRoot.synchronizeConfiguration(ConfigPhase.EARLY);
    }

    @Override
    public String getMixinConfig() {
        return "mixins.tfixins.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        return Mixins.getMixins();
    }
}
