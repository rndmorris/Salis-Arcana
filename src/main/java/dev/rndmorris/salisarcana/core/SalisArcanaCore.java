package dev.rndmorris.salisarcana.core;

import java.util.ArrayList;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.core.asm.IAsmEditor;
import dev.rndmorris.salisarcana.core.asm.compat.ModCompatEditor;

@IFMLLoadingPlugin.MCVersion("1.7.10")
public class SalisArcanaCore implements IFMLLoadingPlugin {

    public SalisArcanaCore() {
        ConfigModuleRoot.synchronizeConfiguration();
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
                    .addConflict("MixinBlockCosmeticSolid", ConfigModuleRoot.bugfixes.beaconBlockFixSetting)
                    .addConflict("MixinBlockCandleRenderer", ConfigModuleRoot.bugfixes.candleRendererCrashes)
                    .addConflict("MixinBlockCandle", ConfigModuleRoot.bugfixes.candleRendererCrashes)
                    .addConflict("MixinItemShard", ConfigModuleRoot.bugfixes.itemShardColor)
                    .addConflict("MixinWandManager", ConfigModuleRoot.enhancements.useAllBaublesSlots)
                    .addConflict("MixinEventHandlerRunic", ConfigModuleRoot.enhancements.useAllBaublesSlots)
                    .addConflict("MixinWarpEvents", ConfigModuleRoot.enhancements.useAllBaublesSlots));
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
