package dev.rndmorris.tfixins;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

import dev.rndmorris.tfixins.config.FixinsConfig;
import dev.rndmorris.tfixins.mixins.Mixins;

@LateMixin
public class ThaumicFixinsMixinLoader implements ILateMixinLoader {

    public ThaumicFixinsMixinLoader() {
        String path = Paths.get("config", ThaumicFixins.MODID + ".cfg")
            .toString();
        FixinsConfig.synchronizeConfiguration(new File(path));
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
