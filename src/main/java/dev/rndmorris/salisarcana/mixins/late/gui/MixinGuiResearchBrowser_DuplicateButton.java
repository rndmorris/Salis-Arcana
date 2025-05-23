package dev.rndmorris.salisarcana.mixins.late.gui;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.network.MessageDuplicateResearch;
import dev.rndmorris.salisarcana.network.NetworkHandler;
import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.api.research.ResearchItem;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.common.lib.research.ResearchManager;

@Mixin(GuiResearchBrowser.class)
public class MixinGuiResearchBrowser_DuplicateButton extends GuiScreen {
    @Unique
    private static final ResourceLocation sa$duplicationButtonTexture = new ResourceLocation(SalisArcana.MODID, "textures/gui/duplicate_button.png");

    @Shadow(remap = false)
    private ResearchItem currentHighlight;

    @Shadow(remap = false)
    public boolean hasScribestuff;

    @Unique
    private int sa$buttonX;

    @Unique
    private int sa$buttonY;

    @Unique
    private ResearchItem sa$selectedResearch;

    @Inject(
        method = "genResearchBackground",
        at = @At(
            value = "FIELD",
            target = "Lthaumcraft/client/gui/GuiResearchBrowser;currentHighlight:Lthaumcraft/api/research/ResearchItem;",
            opcode = Opcodes.PUTFIELD,
            ordinal = 1,
            remap = false),
        remap = false)
    public void saveDuplicateButton(int mouseX, int mouseY, float partialTicks, CallbackInfo ci,
        @Local(name = "var42") int researchLeftX, @Local(name = "var41") int researchTopY) {
        this.sa$buttonX = researchLeftX + 15;
        this.sa$buttonY = researchTopY + 15;
    }

    @Inject(method = "genResearchBackground", at = @At(value = "FIELD", target = "Lthaumcraft/api/research/ResearchCategories;researchCategories:Ljava/util/LinkedHashMap;", remap = false), remap = false)
    public void drawDuplicateButton(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        final var playerName = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
        if(!this.hasScribestuff || !ResearchManager.isResearchComplete(playerName, "RESEARCHDUPE")) return;

        final boolean mouseOnButton = this.sa$mouseOnButton(mouseX, mouseY);
        if(!mouseOnButton || this.sa$selectedResearch == null) {
            this.sa$selectedResearch = this.currentHighlight;
        }

        if(this.sa$selectedResearch != null) {
            if(this.sa$selectedResearch.getResearchPrimaryTag() == null || !ResearchManager.isResearchComplete(playerName, this.sa$selectedResearch.key)) {
                this.sa$selectedResearch = null;
                return;
            }

            Minecraft.getMinecraft().renderEngine.bindTexture(sa$duplicationButtonTexture);

            if (mouseOnButton) {
                this.currentHighlight = null; // Don't draw the title/description text box.
            } else {
                GL11.glColor3f(0.8f, 0.8f, 0.8f);
            }

            final var t = Tessellator.instance;
            t.startDrawingQuads();
            t.addVertexWithUV(this.sa$buttonX, this.sa$buttonY + 10, zLevel, 0.0D, 1.0D);
            t.addVertexWithUV(this.sa$buttonX + 10, this.sa$buttonY + 10, zLevel, 1.0D, 1.0D);
            t.addVertexWithUV(this.sa$buttonX + 10, this.sa$buttonY, zLevel, 1.0D, 0.0D);
            t.addVertexWithUV(this.sa$buttonX, this.sa$buttonY, zLevel, 0.0D, 0.0D);
            t.draw();
        }
    }

    @WrapMethod(method = "mouseClicked")
    public void buttonClicked(int mouseX, int mouseY, int mouseButton, Operation<Void> original) {
        if(mouseButton == 0 && this.sa$selectedResearch != null && this.sa$mouseOnButton(mouseX, mouseY)) {
            NetworkHandler.instance.sendToServer(new MessageDuplicateResearch(this.sa$selectedResearch.key));
        } else {
            original.call(mouseX, mouseY, mouseButton);
        }
    }

    @Unique
    private boolean sa$mouseOnButton(int mouseX, int mouseY) {
        return mouseX >= this.sa$buttonX && mouseX < (this.sa$buttonX + 10) && mouseY >= this.sa$buttonY && mouseY < (this.sa$buttonY + 10);
    }
}
