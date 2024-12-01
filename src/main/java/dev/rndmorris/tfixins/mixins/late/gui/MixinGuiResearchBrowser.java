package dev.rndmorris.tfixins.mixins.late.gui;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import dev.rndmorris.tfixins.config.FixinsConfig;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.common.lib.research.ResearchManager;

@Mixin(GuiResearchBrowser.class)
public class MixinGuiResearchBrowser extends GuiScreen {

    @Shadow(remap = false)
    private static String selectedCategory = null;

    @Shadow(remap = false)
    private String player;

    @Shadow(remap = false)
    private ResearchItem currentHighlight = null;

    @Unique
    private int tf$lastDir = 0;
    @Unique
    private boolean tf$isControlHeld;

    @Override
    public void handleInput() {
        super.handleInput();
        if (FixinsConfig.researchBrowserModule.showResearchId.isEnabled()) {
            this.tf$isControlHeld = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
        }

        if (FixinsConfig.researchBrowserModule.scrollwheelEnabled.isEnabled()) {
            // We need to run this every time to avoid buffering a scroll
            int dir = (int) Math.signum(Mouse.getDWheel()); // We want DWheel since last call, not last mouse event, as
                                                            // it's possible no new mouse events will have been sent
            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                if (dir != tf$lastDir) {
                    tf$lastDir = dir;
                    ArrayList<String> categories = new ArrayList<>();
                    for (String category : ResearchCategories.researchCategories.keySet()) {
                        if (category.equals("ELDRITCH")
                            && !ResearchManager.isResearchComplete(this.player, "ELDRITCHMINOR")) {
                            continue;
                        }
                        categories.add(category);
                    }

                    if (FixinsConfig.researchBrowserModule.invertedScrolling.isEnabled()) {
                        dir *= -1;
                    }

                    int new_index = (categories.indexOf(selectedCategory) + dir) % categories.size();
                    if (new_index < 0) {
                        new_index += categories.size();
                    }
                    selectedCategory = categories.get(new_index);
                }
            }
        }
    }

    @Inject(method="mouseClicked", at=@At(value = "HEAD"), cancellable = true)
    private void mixinMouseClicked(int mouseX, int mouseY, int button, CallbackInfo ci) {
        if (FixinsConfig.researchBrowserModule.showResearchId.isEnabled() && tf$isControlHeld) {
            ci.cancel();
        }
    }

    @Inject(method="drawScreen", at=@At(value="TAIL"))
    private void mixinDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (FixinsConfig.researchBrowserModule.showResearchId.isEnabled() && this.tf$isControlHeld && this.currentHighlight != null) {
            tf$drawPopup(mouseX, mouseY, this.currentHighlight.key);
        }
    }

    @Unique
    private void tf$drawPopup(int x, int y, String str) {
        GL11.glPushMatrix();

        int textWidth = this.fontRendererObj.getStringWidth(str);
        int lineCount = this.fontRendererObj.listFormattedStringToWidth(str, 150).size();
        int textHeight = lineCount * this.fontRendererObj.FONT_HEIGHT;
        int rectangleColor = 0xC0000000; // RGBA
        int purple = 0xFF9090FF;
        x += 6;
        y += 4;

        this.drawGradientRect(x - 3, y - 3 - (textHeight * 2),
            x + (int)(textWidth * .5) + 3, y - textHeight - 3,
            rectangleColor, rectangleColor);

        GL11.glTranslatef((float) x, (float) y - (textHeight * 2), 0);
        GL11.glScalef(0.5F, 0.5F, 1.0F);

        this.fontRendererObj.drawSplitString(str, 0, 0, 150, purple);

        GL11.glPopMatrix();
    }
}
