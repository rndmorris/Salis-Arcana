package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.ResearchHelper;
import dev.rndmorris.salisarcana.network.MessageDuplicateResearch;
import dev.rndmorris.salisarcana.network.NetworkHandler;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.ResearchManager;

@Mixin(GuiResearchBrowser.class)
public class MixinGuiResearchBrowser_DuplicateButton extends GuiScreen {

    @Unique
    private static final ResourceLocation sa$duplicationButtonTexture = new ResourceLocation(
        SalisArcana.MODID,
        "textures/gui/duplicate_button.png");

    @Unique
    private static final ResourceLocation sa$paperTexture = new ResourceLocation(
        "minecraft",
        "textures/items/paper.png");

    @Unique
    private static final ResourceLocation sa$inkwellTexture = new ResourceLocation(
        "thaumcraft",
        "textures/items/inkwell.png");

    @Shadow(remap = false)
    private ResearchItem currentHighlight;

    @Shadow(remap = false)
    public boolean hasScribestuff;

    @Shadow(remap = false)
    private String player;

    @Unique
    private int sa$buttonX;

    @Unique
    private int sa$buttonY;

    @Unique
    private ResearchItem sa$selectedResearch;

    @Unique
    private long sa$duplicateCooldown;

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

    @Inject(
        method = "genResearchBackground",
        at = @At(value = "INVOKE", target = "Ljava/util/LinkedHashMap;keySet()Ljava/util/Set;", remap = false),
        remap = false)
    public void drawDuplicateButton(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (!ResearchManager.isResearchComplete(this.player, "RESEARCHDUPE")) return;

        final boolean mouseOnButton = this.sa$mouseOnButton(mouseX, mouseY);
        if (!mouseOnButton || this.sa$selectedResearch == null) {
            this.sa$selectedResearch = this.currentHighlight;
        }

        if (this.sa$selectedResearch != null) {
            if (this.sa$selectedResearch.getResearchPrimaryTag() == null
                || !ResearchManager.isResearchComplete(this.player, this.sa$selectedResearch.key)) {
                this.sa$selectedResearch = null;
                return;
            }

            if (mouseOnButton) {
                this.currentHighlight = null; // Don't draw the title/description text box.
            } else {
                GL11.glColor3f(0.8f, 0.8f, 0.8f);
            }

            Minecraft.getMinecraft().renderEngine.bindTexture(sa$duplicationButtonTexture);

            // Draw the "duplicate research" button
            final var t = Tessellator.instance;
            t.startDrawingQuads();
            t.addVertexWithUV(this.sa$buttonX, this.sa$buttonY + 10, zLevel, 0d, 1d);
            t.addVertexWithUV(this.sa$buttonX + 10, this.sa$buttonY + 10, zLevel, 1d, 1d);
            t.addVertexWithUV(this.sa$buttonX + 10, this.sa$buttonY, zLevel, 1d, 0d);
            t.addVertexWithUV(this.sa$buttonX, this.sa$buttonY, zLevel, 0d, 0d);
            t.draw();
        } else {
            this.sa$duplicateCooldown = 0L;
        }
    }

    @WrapMethod(method = "mouseClicked")
    public void buttonClicked(int mouseX, int mouseY, int mouseButton, Operation<Void> original) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && this.sa$selectedResearch != null
            && this.sa$mouseOnButton(mouseX, mouseY)) {
            final long currentTime = System.currentTimeMillis();
            if (this.hasScribestuff && this.sa$duplicateCooldown < currentTime) {
                this.sa$duplicateCooldown = currentTime + 2000L;
                NetworkHandler.instance.sendToServer(new MessageDuplicateResearch(this.sa$selectedResearch.key));
            }
        } else {
            original.call(mouseX, mouseY, mouseButton);
        }
    }

    @Unique
    private boolean sa$mouseOnButton(int mouseX, int mouseY) {
        return mouseX >= this.sa$buttonX && mouseX < (this.sa$buttonX + 10)
            && mouseY >= this.sa$buttonY
            && mouseY < (this.sa$buttonY + 10);
    }

    @Inject(
        method = "genResearchBackground",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;drawScreen(IIF)V", remap = true),
        remap = false)
    private void drawHoverTextbox(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (this.currentHighlight != null || this.sa$selectedResearch == null) return;

        final var playerEntity = Minecraft.getMinecraft().thePlayer;
        final String titleText = StatCollector
            .translateToLocalFormatted("salisarcana:duplicate_research.text", this.sa$selectedResearch.getName());
        final boolean opFree = SalisConfig.features.creativeOpThaumonomicon.isEnabled()
            && playerEntity.capabilities.isCreativeMode;
        final boolean aspectFree = opFree || SalisConfig.features.researchDuplicationFree.isEnabled();
        final boolean sufficientAspects = aspectFree
            || ResearchHelper.hasResearchAspects(this.player, this.sa$selectedResearch.tags);

        final int leftX = mouseX + 6;
        final int topY = mouseY - 4;
        int messageY = this.fontRendererObj.FONT_HEIGHT + 9; // Below the titleText
        AspectList playerAspects = Thaumcraft.proxy.playerKnowledge.getAspectsDiscovered(this.player);

        int width = (this.fontRendererObj.getStringWidth(titleText) * 3) / 4;

        if (!opFree) {
            // 24 is the width of the paper & inkwell icons
            width = Math.max(width, 24 + (aspectFree ? 0 : 16 * this.sa$selectedResearch.tags.size()));
            messageY += 34; // Leave space for the price list
        }

        int color;
        List<String> message;
        if (System.currentTimeMillis() < this.sa$duplicateCooldown) {
            message = this.fontRendererObj.listFormattedStringToWidth(
                StatCollector.translateToLocal("salisarcana:duplicate_research.success"),
                width * 2);
            color = 0xFFA500;
        } else if (opFree || (this.hasScribestuff && sufficientAspects)) {
            message = this.fontRendererObj.listFormattedStringToWidth(
                StatCollector.translateToLocal("salisarcana:duplicate_research.prompt"),
                width * 2);
            color = 0x87CEEB;
        } else {
            message = new ArrayList<>();
            color = 0xDC143C;

            if (!this.hasScribestuff) {
                message.addAll(
                    this.fontRendererObj.listFormattedStringToWidth(
                        StatCollector.translateToLocal("tc.research.shortprim"),
                        width * 2));
            }

            if (!sufficientAspects) {
                message.addAll(
                    this.fontRendererObj
                        .listFormattedStringToWidth(StatCollector.translateToLocal("tc.research.short"), width * 2));
            }
        }

        final int bottomY = topY + (messageY + ((this.fontRendererObj.FONT_HEIGHT + 2) * message.size())) / 2;
        this.drawGradientRect(leftX - 3, topY - 3, leftX + width + 3, bottomY + 3, -1073741824, -1073741824);

        GL11.glPushMatrix();
        GL11.glTranslatef(leftX, topY, 0f);
        GL11.glScalef(0.75f, 0.75f, 1f);
        this.fontRendererObj.drawStringWithShadow(titleText, 0, 0, 0xFFFFFF);
        GL11.glScalef(2f / 3f, 2f / 3f, 1f);
        for (final String line : message) {
            this.fontRendererObj.drawStringWithShadow(line, 0, messageY, color);
            messageY += this.fontRendererObj.FONT_HEIGHT + 2;
        }
        GL11.glPopMatrix();

        if (!opFree) {
            final float opacity = (float) Math.sin(((Minecraft.getSystemTime() % 600L) / 600d) * Math.PI * 2.0D) * 0.25F
                + 0.75F;
            final var engine = Minecraft.getMinecraft().renderEngine;
            int xPos = leftX;
            final int yPos = topY + 8;

            GL11.glPushAttrib(1048575);
            GL11.glEnable(GL11.GL_BLEND);

            final boolean hasPaper = playerEntity.inventory.hasItem(Items.paper);
            engine.bindTexture(sa$paperTexture);
            GL11.glColor4f(1f, 1f, 1f, hasPaper ? 1f : opacity);
            UtilsFX.drawTexturedQuadFull(xPos, yPos, 0d);

            final boolean hasInkwell = ResearchManager.consumeInkFromPlayer(playerEntity, false);
            engine.bindTexture(sa$inkwellTexture);
            GL11.glColor4f(1f, 1f, 1f, hasInkwell ? 1f : opacity);
            UtilsFX.drawTexturedQuadFull(xPos + 8, yPos, 0d);

            xPos += 24;
            if (!aspectFree) {
                for (final var aspect : this.sa$selectedResearch.tags.aspects.entrySet()) {
                    float alpha = 1f;
                    if (playerAspects.getAmount(aspect.getKey()) < aspect.getValue()) {
                        alpha = opacity;
                    }

                    UtilsFX.drawTag(xPos, yPos, aspect.getKey(), aspect.getValue(), 0, 0d, 771, alpha, false);
                    xPos += 16;
                }
            }
            GL11.glPopAttrib();
        }
    }
}
