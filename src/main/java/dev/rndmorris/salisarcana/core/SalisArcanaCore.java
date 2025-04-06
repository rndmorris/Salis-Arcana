package dev.rndmorris.salisarcana.core;

import java.util.ArrayList;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import dev.rndmorris.salisarcana.config.Config;
import dev.rndmorris.salisarcana.core.asm.IAsmEditor;
import dev.rndmorris.salisarcana.core.asm.compat.ModCompatEditor;

@IFMLLoadingPlugin.MCVersion("1.7.10")
public class SalisArcanaCore implements IFMLLoadingPlugin {

    public SalisArcanaCore() {
        Config.synchronizeConfiguration();
    }

    public static ArrayList<IAsmEditor> editors = new ArrayList<>();

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

        editors.add(
            new ModCompatEditor(
                "xyz.uniblood.thaumicmixins.mixinplugin.ThaumicMixinsLateMixins",
                "getMixins",
                "(Ljava/util/Set;)Ljava/util/List;")
                    .addConflict("MixinBlockCosmeticSolid", Config.bugfixes.beaconBlockFixSetting)
                    .addConflict("MixinBlockCandleRenderer", Config.bugfixes.candleRendererCrashes)
                    .addConflict("MixinBlockCandle", Config.bugfixes.candleRendererCrashes)
                    .addConflict("MixinItemShard", Config.bugfixes.itemShardColor)
                    .addConflict("MixinWandManager", Config.features.useAllBaublesSlots)
                    .addConflict("MixinEventHandlerRunic", Config.features.useAllBaublesSlots)
                    .addConflict("MixinWarpEvents", Config.features.useAllBaublesSlots));
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
