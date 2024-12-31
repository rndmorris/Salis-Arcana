package dev.rndmorris.salisarcana;

import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.mixins.Mixins;

@LateMixin
public class SalisArcanaMixinLoader implements ILateMixinLoader {

    public SalisArcanaMixinLoader() {
        ConfigModuleRoot.synchronizeConfiguration(ConfigPhase.EARLY);
    }

    @Override
    public String getMixinConfig() {
        return "mixins.salisarcana.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        return Mixins.getLateMixins(loadedMods);
    }
}
