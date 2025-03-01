package dev.rndmorris.salisarcana.common.compat;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import com.mitchej123.hodgepodge.config.TweaksConfig;

import cpw.mods.fml.common.Loader;
import jss.bugtorch.config.BugTorchConfig;

public class MixinModCompat {

    public static boolean disableWandCV = false;
    public static boolean disableBlockCandleFixes = false;

    public static void init() {
        // Both Hodgepodge and BugTorch register their configs in a coremod, so they are already loaded by the time
        // we get here.

        // For other mods, we may have to fetch config values manually.
        if (Loader.isModLoaded("hodgepodge")) {
            disableWandCV = TweaksConfig.addCVSupportToWandPedestal;
            if (disableWandCV) {
                LOG.info("Salis Arcana: Disabling Wand Pedestal CV support -- Hodgepodge Enabled");
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
