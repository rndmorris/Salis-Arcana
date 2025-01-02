package dev.rndmorris.salisarcana.core;

import java.util.ArrayList;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.core.asm.IAsmEditor;
import dev.rndmorris.salisarcana.core.asm.TMixinsCompat;

public class SalisArcanaCore implements IFMLLoadingPlugin {

    public SalisArcanaCore() {
        ConfigModuleRoot.synchronizeConfiguration(ConfigPhase.EARLY);
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
        editors.add(new TMixinsCompat());
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
