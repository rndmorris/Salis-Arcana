package dev.rndmorris.salisarcana.common.compat;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import com.mitchej123.hodgepodge.config.TweaksConfig;

import cpw.mods.fml.common.Loader;
import dev.rndmorris.salisarcana.config.SalisConfig;
import jss.bugtorch.config.BugTorchConfig;

public class MixinModCompat {

    public static boolean disableWandCV = false;
    public static boolean disableBlockCandleFixes = false;

    public static void init() {
        // Both Hodgepodge and BugTorch register their configs in a coremod, so they are already loaded by the time
        // we get here.

        // For other mods, we may have to fetch config values manually.
        if (Loader.isModLoaded("hodgepodge")) {
            // Hodgepodge's cv support is buggy and won't charge amulets, so we force-disable it if either
            // is enabled, and we enable ours instead.
            if (TweaksConfig.addCVSupportToWandPedestal || SalisConfig.features.wandPedestalUseCV.isEnabled()) {
                TweaksConfig.addCVSupportToWandPedestal = false;
                SalisConfig.features.wandPedestalUseCV.setEnabled(true);
            }
        }
        if (Loader.isModLoaded("bugtorch")) {
            disableBlockCandleFixes = BugTorchConfig.fixThaumcraftCandleColorArrayOutOfBounds;
            if (disableBlockCandleFixes) {
                LOG.info("Salis Arcana: Disabling Thaumcraft candle color array out of bounds fix -- BugTorch Enabled");
            }
        }
    }
}
