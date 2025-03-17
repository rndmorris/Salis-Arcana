package dev.rndmorris.salisarcana.mixins.late.gui;

import net.minecraft.client.gui.GuiScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.client.gui.GuiResearchBrowser;

@Mixin(value = GuiResearchBrowser.class)
public class MixinGuiResearchBrowser_RightClickClose extends GuiScreen {

    @Inject(method = "mouseClicked", at = @At(value = "HEAD"), cancellable = true)
    private void onMouseClicked(int mouseX, int mouseY, int button, CallbackInfo ci) {
        if (ConfigModuleRoot.enhancements.nomiconRightClickClose.isEnabled()) {
            sa$RightClickClose_mouseClicked(button, ci);
        }
    }

    @Unique
    private void sa$RightClickClose_mouseClicked(int button, CallbackInfo ci) {
        if (button == 1) {
            this.mc.displayGuiScreen(null);
            ci.cancel();
        }
    }
}
