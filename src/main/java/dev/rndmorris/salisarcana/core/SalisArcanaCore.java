package dev.rndmorris.salisarcana.core;

import java.util.ArrayList;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.core.asm.IAsmEditor;
import dev.rndmorris.salisarcana.core.asm.compat.ModCompatEditor;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions("dev.rndmorris.salisarcana.core")
public class SalisArcanaCore implements IFMLLoadingPlugin {

    private static Boolean isObf;
    public static final String MODID = "salisarcana";
    public static final Logger LOG = LogManager.getLogger("salisarcana-core");
    public static ArrayList<IAsmEditor> editors = new ArrayList<>();

    public SalisArcanaCore() {
        SalisConfig.synchronizeConfiguration();
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "dev.rndmorris.salisarcana.core.SalisArcanaClassTransformer" };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        isObf = (boolean) data.get("runtimeDeobfuscationEnabled");

        editors.add(
            new ModCompatEditor(
                "xyz.uniblood.thaumicmixins.mixinplugin.ThaumicMixinsLateMixins",
                "getMixins",
                "(Ljava/util/Set;)Ljava/util/List;")
                    .addConflict("MixinBlockCosmeticSolid", SalisConfig.bugfixes.beaconBlockFixSetting)
                    .addConflict("MixinBlockCandleRenderer", SalisConfig.bugfixes.candleRendererCrashes)
                    .addConflict("MixinBlockCandle", SalisConfig.bugfixes.candleRendererCrashes)
                    .addConflict("MixinItemShard", SalisConfig.bugfixes.itemShardColor)
                    .addConflict("MixinWandManager", SalisConfig.features.useAllBaublesSlots)
                    .addConflict("MixinEventHandlerRunic", SalisConfig.features.useAllBaublesSlots)
                    .addConflict("MixinWarpEvents_BaubleSlots", SalisConfig.features.useAllBaublesSlots));
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    public static boolean isObf() {
        if (isObf == null) {
            throw new IllegalStateException("Obfuscation state has been accessed too early!");
        }
        return isObf;
    }
}
