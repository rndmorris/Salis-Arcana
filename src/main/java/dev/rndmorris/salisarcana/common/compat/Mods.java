package dev.rndmorris.salisarcana.common.compat;

import java.util.function.Supplier;

import cpw.mods.fml.common.Loader;

public enum Mods {

    TC4Tweak("tc4tweak"),;

    public final String modid;
    private final Supplier<Boolean> supplier;
    private Boolean loaded; // class version so it is nullable

    Mods(String modid) {
        this.modid = modid;
        this.supplier = null;
    }

    Mods(Supplier<Boolean> supplier) {
        this.supplier = supplier;
        this.modid = null;
    }

    public boolean isLoaded() {
        if (loaded == null) {
            if (supplier != null) {
                loaded = supplier.get();
            } else if (modid != null) {
                loaded = Loader.isModLoaded(modid);
            } else loaded = false;
        }
        return loaded;
    }
}
