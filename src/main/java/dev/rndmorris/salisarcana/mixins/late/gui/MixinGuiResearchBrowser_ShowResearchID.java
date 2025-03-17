package dev.rndmorris.salisarcana.mixins.late.gui;

import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.client.gui.GuiResearchBrowser;

@Mixin(GuiResearchBrowser.class)
public class MixinGuiResearchBrowser_ShowResearchID extends GuiScreen {

    @Unique
    private boolean sa$isControlHeld;

    @Shadow(remap = false)
    private ResearchItem currentHighlight = null;

    @Override
    public void handleKeyboardInput() {
        super.handleKeyboardInput();
        if (ConfigModuleRoot.enhancements.nomiconShowResearchId.isEnabled()) {
            sa$ShowResearchId_handleKeyboardInput();
        }
    }

    @Unique
    private void sa$ShowResearchId_handleKeyboardInput() {
        this.sa$isControlHeld = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
    }

    @Unique
    private void sa$ShowResearchId_drawScreen(int mouseX, int mouseY) {
        if (this.sa$isControlHeld && this.currentHighlight != null) {
            sa$drawPopup(mouseX, mouseY, this.currentHighlight.key);
        }
    }

    @Inject(method = "drawScreen", at = @At(value = "TAIL"))
    private void mixinDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (ConfigModuleRoot.enhancements.nomiconShowResearchId.isEnabled()) {
            sa$ShowResearchId_drawScreen(mouseX, mouseY);
        }
    }

    @Unique
    private void sa$drawPopup(int x, int y, String str) {
        GL11.glPushMatrix();

        int textWidth = this.fontRendererObj.getStringWidth(str);
        int lineCount = this.fontRendererObj.listFormattedStringToWidth(str, 150)
            .size();
        int textHeight = lineCount * this.fontRendererObj.FONT_HEIGHT;
        int rectangleColor = 0xC0000000; // RGBA
        int purple = 0xFF9090FF;
        x += 6;
        y += 4;

        this.drawGradientRect(
            x - 3,
            y - 3 - (textHeight * 2),
            x + (int) (textWidth * .5) + 3,
            y - textHeight - 3,
            rectangleColor,
            rectangleColor);

        GL11.glTranslatef((float) x, (float) y - (textHeight * 2), 0);
        GL11.glScalef(0.5F, 0.5F, 1.0F);

        this.fontRendererObj.drawSplitString(str, 0, 0, 150, purple);

        GL11.glPopMatrix();
    }

}
