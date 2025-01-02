package dev.rndmorris.salisarcana.common.compat;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import com.mitchej123.hodgepodge.config.TweaksConfig;

import cpw.mods.fml.common.Loader;
import jss.bugtorch.config.BugTorchConfig;

public class ModCompat {

    public static boolean disableWandCV = false;
    public static boolean disableBlockCandleFixes = false;

    public static void init() {
        if (Loader.isModLoaded("hodgepodge")) {
            disableWandCV = TweaksConfig.addCVSupportToWandPedestal;
            if (disableWandCV) {
                LOG.info("Salis Arcana: Disabling Wand Pedestal CV support");
            }
        }
        if (Loader.isModLoaded("bugtorch")) {
            disableBlockCandleFixes = BugTorchConfig.fixThaumcraftCandleColorArrayOutOfBounds;
            if (disableBlockCandleFixes) {
                LOG.info("Salis Arcana: Disabling Thaumcraft candle color array out of bounds fix");
            }
        }
    }
}
