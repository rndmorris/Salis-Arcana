package dev.rndmorris.salisarcana;

import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;
import com.gtnewhorizon.gtnhmixins.builders.IMixins;

import dev.rndmorris.salisarcana.common.compat.MixinModCompat;
import dev.rndmorris.salisarcana.mixins.Mixins;

@LateMixin
public class SalisArcanaMixinLoader implements ILateMixinLoader {

    public SalisArcanaMixinLoader() {
        MixinModCompat.init();
    }

    @Override
    public String getMixinConfig() {
        return "mixins.salisarcana.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        return IMixins.getLateMixins(Mixins.class, loadedMods);
    }
}
