package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.lib;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import dev.rndmorris.salisarcana.lib.ObfuscationInfo;
import dev.rndmorris.salisarcana.mixins.early.accessor.AccessorGuiContainer;
import dev.rndmorris.salisarcana.mixins.early.accessor.AccessorMinecraft;
import thaumcraft.client.lib.UtilsFX;

@Mixin(value = UtilsFX.class, remap = false)
public class MixinUtilsFX_ReflectionToAccessors {
    // Replace reflection with AT/accessors

    /**
     * @author Sisyphussy
     * @reason inefficient reflection
     */
    @Overwrite
    public static float getEquippedProgress(ItemRenderer ir) {
        return ir.equippedProgress;
    }

    /**
     * @author Sisyphussy
     * @reason inefficient reflection
     */
    @Overwrite
    public static float getPrevEquippedProgress(ItemRenderer ir) {
        return ir.prevEquippedProgress;
    }

    /**
     * @author Sisyphussy
     * @reason inefficient reflection
     */
    @Overwrite
    public static Timer getTimer(Minecraft mc) {
        return ((AccessorMinecraft) mc).getTimer();
    }

    /**
     * @author Sisyphussy
     * @reason inefficient reflection
     */
    @Overwrite
    public static int getGuiXSize(GuiContainer gui) {
        return ((AccessorGuiContainer) gui).getXSize();
    }

    /**
     * @author Sisyphussy
     * @reason inefficient reflection
     */
    @Overwrite
    public static int getGuiYSize(GuiContainer gui) {
        return ((AccessorGuiContainer) gui).getYSize();
    }

    /**
     * @author Sisyphussy
     * @reason inefficient reflection
     */
    @Overwrite
    public static float getGuiZLevel(Gui gui) {
        return gui.zLevel;
    }

    /**
     * @author Sisyphussy
     * @reason inefficient reflection
     *         Note: This specific method gets overwritten by TC4Tweaks as well.
     */
    @Overwrite
    public static ResourceLocation getParticleTexture() {
        return sa$particleTextures;
    }

    @Unique
    private static final ResourceLocation sa$particleTextures;

    static {
        // this can also be done via accessors, makes no difference though.
        ResourceLocation location;
        try {
            Field f = EffectRenderer.class.getDeclaredField(ObfuscationInfo.PARTICLE_TEXTURES.getName());
            f.setAccessible(true);
            location = (ResourceLocation) f.get(null);
        } catch (Throwable t) {
            location = new ResourceLocation("textures/particle/particles.png");
        }
        sa$particleTextures = location;
    }
}
