package dev.rndmorris.salisarcana.client.handlers;

import static dev.rndmorris.salisarcana.lib.ThaumonomiconGuiHelper.RightClickClose$ScreenStack;

import net.glease.tc4tweak.modules.researchBrowser.ThaumonomiconIndexSearcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.R;
import dev.rndmorris.salisarcana.lib.ifaces.IReconnectableContainer;
import dev.rndmorris.salisarcana.mixins.TargetedMod;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.client.gui.GuiResearchRecipe;

public class GuiHandler {

    private static final boolean tc4tweaksLoaded = TargetedMod.TC4_TWEAKS.isLoaded();

    public GuiHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        // Used to clear the stack when going back to the research browser, mitigates #209
        if (SalisConfig.features.nomiconRightClickClose.isEnabled()
            && !SalisConfig.features.nomiconSavePage.isEnabled()) {
            if (event.gui instanceof GuiResearchBrowser
                && Minecraft.getMinecraft().currentScreen instanceof GuiResearchRecipe) {
                RightClickClose$ScreenStack.clear();
            }
        }

        // Re-connects GUIs to their tile entities on the client after NEI closes & returns focus, mitigates #337
        if (event.gui instanceof GuiContainer gui && gui.inventorySlots instanceof IReconnectableContainer container) {
            container.salisArcana$reconnect();
        }
    }

    // This checks to see if you are opening directly to a research page (like if the save page setting is turned on)
    // Without this, the tc4tweaks search bar gets drawn to the top of the research page instead of thaumonomicon.
    @SubscribeEvent
    public void onGuiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!tc4tweaksLoaded || !SalisConfig.features.nomiconSavePage.isEnabled()
            || RightClickClose$ScreenStack.isEmpty()) {
            return;
        }

        // At this point, when opening directly to a page, the current screen is the GuiResearchRecipe,
        // and the current event is for the GuiResearchBrowser
        if (event.gui instanceof GuiResearchBrowser
            && Minecraft.getMinecraft().currentScreen instanceof GuiResearchRecipe) {
            // If this is null, all attempts to draw it will be skipped.
            R.of(ThaumonomiconIndexSearcher.instance)
                .set("thaumSearchField", null);
        }
    }
}
