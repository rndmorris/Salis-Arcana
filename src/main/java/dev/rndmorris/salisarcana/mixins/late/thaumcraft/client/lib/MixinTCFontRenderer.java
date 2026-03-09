package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.lib;

import com.gtnewhorizons.angelica.client.font.BatchingFontRenderer;
import com.gtnewhorizons.angelica.mixins.interfaces.FontRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.client.lib.TCFontRenderer;

@Mixin(value = TCFontRenderer.class, remap = false)
public class MixinTCFontRenderer {

    @Unique
    private static FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

    @Unique
    private static BatchingFontRenderer bfr = ((FontRendererAccessor) fr).angelica$getBatcher();

    /**
     * @author DeathFuel
     * @reason Overwrite TC's copy-pasted font rendering code with Angelica's replacements
     */
    @Overwrite
    private int renderString(String text, int x, int y, int argb, boolean dropShadow) {
        bfr.setBookMode(true);
        int i = ((FontRendererAccessor) fr).angelica$drawStringBatched(text, x, y, argb, dropShadow);
        bfr.setBookMode(false);
        return i;
    }
}
