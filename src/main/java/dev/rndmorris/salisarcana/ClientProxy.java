package dev.rndmorris.salisarcana;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.IResource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import dev.rndmorris.salisarcana.client.ThaumicInventoryScanner;
import dev.rndmorris.salisarcana.client.handlers.GuiHandler;
import dev.rndmorris.salisarcana.config.SalisConfig;

public class ClientProxy extends CommonProxy {

    ThaumicInventoryScanner scanner;

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        if (SalisConfig.features.thaumicInventoryScanning.isEnabled()) {
            scanner = new ThaumicInventoryScanner();
            MinecraftForge.EVENT_BUS.register(scanner);
            FMLCommonHandler.instance()
                .bus()
                .register(scanner);

            scanner.init(event);
        }
        new GuiHandler();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        if (SalisConfig.features.thaumicInventoryScanning.isEnabled()) {
            scanner.postInit(event);
        }
    }

    @Override
    public boolean isSingleplayerClient() {
        return Minecraft.getMinecraft()
            .isSingleplayer();
    }

    @Override
    public World getFakePlayerWorld() {
        if (Minecraft.getMinecraft()
            .isSingleplayer()) {
            return MinecraftServer.getServer()
                .worldServerForDimension(0);
        } else {
            return Minecraft.getMinecraft().theWorld;
        }
    }

    @Override
    public ResourceLocation getSalisTabResource() {
        // Thaumcraft's texture is an animation, so we can't just use it directly. We need to extract a single frame

        // Loads thaum texture
        ResourceLocation location = new ResourceLocation("thaumcraft", "textures/items/dust.png");
        BufferedImage original;
        BufferedImage newImage;
        try {
            // Loads the texture from the resource manager into a BufferedImage
            IResource resource = Minecraft.getMinecraft()
                .getResourceManager()
                .getResource(location);
            original = ImageIO.read(resource.getInputStream());;
        } catch (IOException e) {
            LOG.error("Failed to load Salis Arcana tab image from {}", location, e);
            return null;
        }

        newImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        // Grabs the first frame (16x16 pixels) of the thaumcraft texture
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                int rgb = original.getRGB(x, y);
                newImage.setRGB(x, y, rgb);
            }
        }
        // Registers the new texture with Minecraft's texture manager
        DynamicTexture texture = new DynamicTexture(newImage);
        ResourceLocation newLocation = new ResourceLocation("salisarcana", "textures/items/dust.png");
        Minecraft.getMinecraft()
            .getTextureManager()
            .loadTexture(newLocation, texture);

        return newLocation;
    }

}
