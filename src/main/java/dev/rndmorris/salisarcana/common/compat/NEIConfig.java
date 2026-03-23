package dev.rndmorris.salisarcana.common.compat;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizons.aspectrecipeindex.nei.arcaneworkbench.ArcaneOverlayHandler;
import com.gtnewhorizons.aspectrecipeindex.nei.arcaneworkbench.ArcaneSlotPositioner;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.common.Optional;
import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.Tags;
import dev.rndmorris.salisarcana.common.compat.nei.WandCapSubstitutionHandler;
import dev.rndmorris.salisarcana.common.compat.nei.WandCoreSubstitutionHandler;
import dev.rndmorris.salisarcana.common.item.PlaceholderItem;
import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.client.gui.GuiArcaneWorkbench;

@Optional.Interface(iface = "codechicken.nei.api.API", modid = "NotEnoughItems")
public class NEIConfig implements IConfigureNEI {

    @Override
    @Optional.Method(modid = "NotEnoughItems")
    public void loadConfig() {
        hidePlaceholder(PlaceholderItem.capPlaceholder);
        hidePlaceholder(PlaceholderItem.rodPlaceholder);

        if (SalisConfig.modCompat.aspectRecipeIndex.isEnabled()) {
            ArcaneSlotPositioner positioner = new ArcaneSlotPositioner();
            ArcaneOverlayHandler handler = new ArcaneOverlayHandler();
            if (SalisConfig.features.replaceWandCoreSettings.isEnabled()
                && SalisConfig.modCompat.aspectRecipeIndex.coreReplacementNEIHandler.isEnabled()) {
                API.registerRecipeHandler(new WandCoreSubstitutionHandler());
                API.registerUsageHandler(new WandCoreSubstitutionHandler());
                API.registerGuiOverlay(GuiArcaneWorkbench.class, "salisarcana.substitution.core", positioner);
                API.registerGuiOverlayHandler(GuiArcaneWorkbench.class, handler, "salisarcana.substitution.core");
            }
            if (SalisConfig.features.replaceWandCapsSettings.isEnabled()
                && SalisConfig.modCompat.aspectRecipeIndex.capReplacementNEIHandler.isEnabled()) {
                API.registerRecipeHandler(new WandCapSubstitutionHandler());
                API.registerUsageHandler(new WandCapSubstitutionHandler());
                API.registerGuiOverlay(GuiArcaneWorkbench.class, "salisarcana.substitution.caps", positioner);
                API.registerGuiOverlayHandler(GuiArcaneWorkbench.class, handler, "salisarcana.substitution.caps");
            }
        }
    }

    @Optional.Method(modid = "NotEnoughItems")
    private void hidePlaceholder(PlaceholderItem placeholder) {
        if (placeholder != null) {
            API.hideItem(new ItemStack(placeholder, 0, OreDictionary.WILDCARD_VALUE));
        }
    }

    @Override
    @Optional.Method(modid = "NotEnoughItems")
    public String getName() {
        return SalisArcana.MODID;
    }

    @Override
    @Optional.Method(modid = "NotEnoughItems")
    public String getVersion() {
        return Tags.VERSION;
    }
}
