package dev.rndmorris.salisarcana;

import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

import dev.rndmorris.salisarcana.common.compat.ModCompat;
import dev.rndmorris.salisarcana.mixins.Mixins;

@LateMixin
public class SalisArcanaMixinLoader implements ILateMixinLoader {

    public SalisArcanaMixinLoader() {
        ModCompat.init();
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
