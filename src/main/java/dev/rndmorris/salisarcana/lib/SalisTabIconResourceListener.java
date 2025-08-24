package dev.rndmorris.salisarcana.lib;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;

public class SalisTabIconResourceListener implements IResourceManagerReloadListener {

    private static final ResourceLocation salisTabResource = new ResourceLocation(
        "salisarcana",
        "textures/items/dust.png");
    private DynamicTexture salisTabTexture;

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        // Thaumcraft's texture is an animation, so we can't just use it directly. We need to extract a single frame

        // Loads thaum texture
        ResourceLocation location = new ResourceLocation("thaumcraft", "textures/items/dust.png");
        BufferedImage full;
        BufferedImage newImage;
        try {
            IResource resource = resourceManager.getResource(location);
            full = ImageIO.read(resource.getInputStream());

            // we only use getWidth() here because the texture is an animation, so it consists of multiple "frames"
            newImage = new BufferedImage(full.getWidth(), full.getWidth(), BufferedImage.TYPE_INT_ARGB);
            // Grabs the first frame (width x width)
            for (int y = 0; y < full.getWidth(); y++) {
                for (int x = 0; x < full.getWidth(); x++) {
                    int rgb = full.getRGB(x, y);
                    newImage.setRGB(x, y, rgb);
                }
            }
            DynamicTexture newTexture = new DynamicTexture(newImage);
            if (this.salisTabTexture != null && this.salisTabTexture.equals(newTexture)) {
                // texture is the same, no need to reload
                return;
            }
            this.salisTabTexture = newTexture;
            TextureManager manager = Minecraft.getMinecraft()
                .getTextureManager();
            // manager will be null early on during startup when this first gets called, but it gets loaded later on
            if (manager == null) {
                return;
            }
            manager.loadTexture(SalisTabIconResourceListener.salisTabResource, this.salisTabTexture);
        } catch (IOException e) {
            LOG.error("Failed to load Salis Arcana tab image from {}", location, e);
        }
    }

    public static ResourceLocation getSalisTabResource() {

        return SalisTabIconResourceListener.salisTabResource;
    }
}
