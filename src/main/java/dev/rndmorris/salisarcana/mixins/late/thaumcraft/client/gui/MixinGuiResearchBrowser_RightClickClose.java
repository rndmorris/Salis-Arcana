package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.gui;

import static dev.rndmorris.salisarcana.lib.ThaumonomiconGuiHelper.RightClickClose$ScreenStack;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Tuple;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.client.gui.GuiResearchRecipe;

@Mixin(value = GuiResearchBrowser.class)
public class MixinGuiResearchBrowser_RightClickClose extends GuiScreen {

    @Shadow(remap = false)
    protected double guiMapX;

    @Shadow(remap = false)
    protected double guiMapY;

    @WrapMethod(method = "initGui")
    public void wrapInitGui(Operation<Void> original) {
        original.call();
        if (!RightClickClose$ScreenStack.isEmpty() && SalisConfig.features.nomiconSavePage.isEnabled()) {
            Tuple currentPage = RightClickClose$ScreenStack.pop();
            this.mc.displayGuiScreen(
                new GuiResearchRecipe(
                    (ResearchItem) currentPage.getFirst(),
                    (int) currentPage.getSecond(),
                    this.guiMapX,
                    this.guiMapY));
        }
    }

    @WrapMethod(method = "mouseClicked")
    private void onMouseClicked(int mouseX, int mouseY, int button, Operation<Void> operation) {
        if (button == 1) {
            // simulate pressing inv key
            // par1 is the char typed, par2 is the keycode. par1 is not used in super hierarchy, so we can use any char
            // Thaumcraft checks against keyBindInventory, so we use that for par2
            this.keyTyped(' ', this.mc.gameSettings.keyBindInventory.getKeyCode());
            return;
        }
        operation.call(mouseX, mouseY, button);
    }
}
