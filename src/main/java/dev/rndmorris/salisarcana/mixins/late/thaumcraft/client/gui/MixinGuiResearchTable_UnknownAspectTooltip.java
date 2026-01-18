package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.client.gui.GuiResearchTable;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.ResearchNoteData;
import thaumcraft.common.lib.utils.HexUtils;

@Mixin(value = GuiResearchTable.class, remap = false)
public abstract class MixinGuiResearchTable_UnknownAspectTooltip extends GuiContainer {

    @Shadow
    public ResearchNoteData note;

    @Shadow
    private String username;

    private MixinGuiResearchTable_UnknownAspectTooltip(Container p_i1072_1_) {
        super(p_i1072_1_);
    }

    @Inject(
        method = "drawSheet",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPopMatrix()V", ordinal = 1))
    private void drawUnknownAspectTooltip(int x, int y, int mx, int my, CallbackInfo ci, @Local HexUtils.Hex mouseHex) {
        final var hexData = this.note.hexEntries.get(mouseHex.toString());

        if (hexData != null && hexData.aspect != null
            && !Thaumcraft.proxy.getPlayerKnowledge()
                .hasDiscoveredAspect(this.username, hexData.aspect)) {
            final String aspect = hexData.aspect.getTag();
            final String aspectHintKey = "tc.aspect.help." + aspect;
            String hint = StatCollector.translateToLocal(aspectHintKey);

            // If there's no hint translation present, fall back to aspect description
            if (hint == aspectHintKey) {
                hint = StatCollector.translateToLocal("tc.aspect." + aspect);
            }

            // Calculate position of textbox
            final HexUtils.Pixel pixel = mouseHex.toPixel(9);
            int xPos = (int) pixel.x + x + 161 + 8;
            int yPos = (int) pixel.y + y + 75 + 20;

            // Prevent textbox from overflowing out of the right edge of the GUI
            // because it gets covered up by the NEI menu
            xPos = Math.min(xPos, x + 94 + 85);

            // -12 & +12 correct for the inherent shift in UtilsFX.drawCustomTooltip
            GL11.glDisable(GL11.GL_LIGHTING);
            UtilsFX.drawCustomTooltip(
                this,
                itemRender,
                this.fontRendererObj,
                this.fontRendererObj.listFormattedStringToWidth(
                    StatCollector.translateToLocalFormatted("tc.discoveryerror", hint),
                    150),
                xPos - 75 - 12,
                yPos + 12,
                7);
        }
    }
}
