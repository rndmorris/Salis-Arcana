package dev.rndmorris.salisarcana.client.infusion;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.modularui.GTUITextures;
import thaumcraft.api.IGoggles;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.lib.RenderEventHandler;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.TileInfusionMatrix;

public class InfusionPreview {

    private static ResourceLocation checkmarkLocation;
    private static ResourceLocation crossLocation;
    private final RenderItem renderItem = new RenderItem();

    public InfusionPreview() {
        if (Loader.isModLoaded("gregtech")) {
            checkmarkLocation = GTUITextures.OVERLAY_BUTTON_CHECKMARK.location;
            crossLocation = GTUITextures.OVERLAY_BUTTON_CROSS.location;
        } else {
            new Thread(() -> {
                try {
                    checkmarkLocation = loadTextureFromUrl(
                        "https://raw.githubusercontent.com/GTNewHorizons/GT5-Unofficial/master/src/main/resources/assets/gregtech/textures/gui/overlay_button/checkmark.png",
                        "checkmark");
                    crossLocation = loadTextureFromUrl(
                        "https://raw.githubusercontent.com/GTNewHorizons/GT5-Unofficial/master/src/main/resources/assets/gregtech/textures/gui/overlay_button/cross.png",
                        "cross");
                } catch (Exception e) {
                    e.printStackTrace();
                    //TODO do something if this fails, else it throws NPE
                }
            }).start();

        }
    }

    // TODO list:
    // Make it not scan every tick
    // Add localization
    // Add thaumonomicon page
    // Fix structure checks
    // For future content: Maybe add instability
    // Simplify rendering a bit

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void blockHighlight(DrawBlockHighlightEvent event) {
        ItemStack helmet = event.player.inventory.armorItemInSlot(3);
        if (helmet == null) return;
        Item goggles = helmet.getItem();
        if (goggles instanceof IGoggles && ((IGoggles) goggles).showIngamePopups(helmet, event.player)) {
            MovingObjectPosition mouseOver = event.target;
            int xCoord = mouseOver.blockX;
            int yCoord = mouseOver.blockY;
            int zCoord = mouseOver.blockZ;
            World world = Minecraft.getMinecraft().theWorld;
            TileEntity te = world.getTileEntity(mouseOver.blockX, mouseOver.blockY, mouseOver.blockZ);
            if (te instanceof TileInfusionMatrix matrix) {
                if (matrix.crafting) return;
                boolean spaceAbove = event.player.worldObj.isAirBlock(xCoord, yCoord + 1, zCoord);
                float y = yCoord + (spaceAbove ? 0.4F : 0.0F);
                ForgeDirection dir = spaceAbove ? ForgeDirection.UP
                    : ForgeDirection.getOrientation(event.target.sideHit);

                if (!matrix.validLocation()) {
                    drawString(EnumChatFormatting.RED + "Invalid Location", xCoord, y, zCoord, dir, event.partialTicks);
                    return;
                }
                if (!matrix.active) {
                    drawString(
                        EnumChatFormatting.GOLD + "Right-Click with a wand to activate",
                        xCoord,
                        y,
                        zCoord,
                        dir,
                        event.partialTicks);
                    return;
                }
                InfusionData data = new InfusionData(world, matrix).compute();

                // spotless:off
                ItemStack output = data.getOutputStack();
                if (output == null) {
                    drawString(EnumChatFormatting.RED + "No valid recipe found.", xCoord, y, zCoord, dir, event.partialTicks);
                    return;
                }
                data.fetchEssentiaSources();
                drawTagsOnContainer(
                    data,
                    output,
                    xCoord, y, zCoord,
                    dir,
                    event.partialTicks
                );
                //spotless:on
            }
        }
    }

    // spotless:off
    private void drawTagsOnContainer(InfusionData data, ItemStack stack, float x, float y, float z, ForgeDirection dir, float partialTicks) {

        if (!(Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer player)) return;

        AspectList tags = data.getEssentia();

        if (tags != null && tags.size() > 0) {
            if (RenderEventHandler.tagscale < 0.3F) {
                RenderEventHandler.tagscale += 0.031F - RenderEventHandler.tagscale / 10.0F;
            }

            Minecraft mc = Minecraft.getMinecraft();
            FontRenderer fontRenderer = mc.fontRenderer;
            String recipeName = data.getOutputString();
            AspectList sources = data.buildAspectList();
            float posX = (float) (player.prevPosX + (player.posX - player.prevPosX) * partialTicks);
            float posY = (float) (player.prevPosY + (player.posY - player.prevPosY) * partialTicks);
            float posZ = (float) (player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks);
            final int ROW_SIZE = 5;
            int current = 0;
            int tagsLeft = tags.size();
            int rows = (tagsLeft - 1) / ROW_SIZE + 1;

            float tagscale = RenderEventHandler.tagscale;

            GL11.glDisable(GL11.GL_DEPTH_TEST);

            GL11.glPushMatrix();
            GL11.glTranslatef(
                -posX + x + 0.5f + (2f * tagscale * dir.offsetX),
                -posY + y + 0.5f + (2f * tagscale * dir.offsetY),
                -posZ + z + 0.5f + (2f * tagscale * dir.offsetZ));
            float xd = posX - (x + 0.5f);
            float zd = posZ - (z + 0.5f);
            float rotYaw = (float)(Math.atan2(xd, zd) * 180.0f / Math.PI);
            GL11.glRotatef(rotYaw, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(tagscale, -tagscale, tagscale);

            GL11.glPushMatrix();
            // Thaumcraft's scale is quite weird... -y makes it go up while +y makes it go down...
            GL11.glTranslatef(0, -(rows * 1.05f), 0);
            GL11.glScalef(0.05f, 0.05f, 0.05f);
            final int iconWidth = 20; //16 + 4 padding

            int sw = fontRenderer.getStringWidth(recipeName);
            GL11.glTranslatef(-(sw + iconWidth) / 2f, 0, 0);

            GL11.glPushMatrix();
            GL11.glTranslatef(0, -5, 0);
            renderItem.renderItemIntoGUI(
                mc.fontRenderer,
                mc.getTextureManager(),
                stack,
                0,
                0
            );
            GL11.glPopMatrix();
            GL11.glDisable(GL11.GL_LIGHTING);

            GL11.glColor4f(1, 1, 1, 1);
            fontRenderer.drawStringWithShadow(recipeName, iconWidth, 0, 0xffffff);

            GL11.glPopMatrix();

            GL11.glEnable(GL11.GL_BLEND);
            boolean missingEssentia = false;
            GL11.glPushMatrix();
            for (Aspect tag : tags.getAspects()) {
                int div = Math.min(tagsLeft, ROW_SIZE);
                if (current >= ROW_SIZE) {
                    current = 0;
                    GL11.glTranslatef(0, -1.05F, 0);
                    tagsLeft -= ROW_SIZE;
                    if (tagsLeft < ROW_SIZE) {
                        div = tagsLeft;
                    }
                }

                float shift = ((float)current - (float)div / 2.0F + 0.5F) * tagscale * 4.0F;

                GL11.glPushMatrix();
                GL11.glTranslatef(-shift, 0, 0);
                int color = tag.getColor();
                float red   = ((color >> 16) & 0xFF) / 255f;
                float green = ((color >> 8) & 0xFF) / 255f;
                float blue  = (color & 0xFF) / 255f;
                if (!Thaumcraft.proxy.playerKnowledge.hasDiscoveredAspect(player.getCommandSenderName(), tag)) {
                    UtilsFX.renderQuadCenteredFromTexture(
                            "textures/aspects/_unknown.png",
                            1.0F,
                            red, green, blue,
                        220,
                            771,
                            0.75F
                    );
                } else {
                    UtilsFX.renderQuadCenteredFromTexture(
                            tag.getImage(),
                            1.0F,
                            red, green, blue,
                        220,
                            771,
                            0.75F
                    );
                }

                int amount = tags.getAmount(tag);
                if (amount >= 0) {
                    GL11.glPushMatrix();
                    String am = Integer.toString(amount);
                    GL11.glScalef(0.04F, 0.04F, 0.04F);
                    GL11.glTranslatef(0, 6, -0.1f);
                    sw = fontRenderer.getStringWidth(am);
                    GL11.glEnable(GL11.GL_BLEND);
                    fontRenderer.drawString(am, 14 - sw, 1, 1118481);
                    GL11.glTranslatef(0, 0, -0.1f);
                    fontRenderer.drawString(am, 13 - sw, 0, 16777215);

                    GL11.glPopMatrix();
                }

                if (InfusionData.hasEnoughEssentia(sources, tag, amount)) {
                    mc.renderEngine.bindTexture(checkmarkLocation);
                } else {
                    missingEssentia = true;
                    mc.renderEngine.bindTexture(crossLocation);
                }
                GL11.glTranslatef(0.4f, -0.4f, 0);
                UtilsFX.renderQuadCenteredFromTexture(
                        2/3f,
                        1, 1, 1,
                        0,
                        771,
                        1f
                );

                GL11.glPopMatrix();
                current++;
            }
            GL11.glPopMatrix();
            String display = missingEssentia ? EnumChatFormatting.RED + "Not enough essentia" : EnumChatFormatting.GOLD + "Right-Click with wand to start";
            sw = fontRenderer.getStringWidth(display);
            GL11.glPushMatrix();
            GL11.glTranslatef(0, 1.05F, 0);
            GL11.glScalef(0.05f, 0.05f, 0.05f);
            GL11.glTranslatef(-sw / 2f, -fontRenderer.FONT_HEIGHT + 1, 0);
            fontRenderer.drawStringWithShadow(display, 0, 0, 0xffffff);
            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glPopMatrix();
        }

    }
    //spotless:on

    private void drawString(String text, float x, float y, float z, ForgeDirection dir, float partialTicks) {
        if (!(Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer player)) return;

        if (RenderEventHandler.tagscale < 0.3F) {
            RenderEventHandler.tagscale += 0.031F - RenderEventHandler.tagscale / 10.0F;
        }

        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fontRenderer = mc.fontRenderer;
        float posX = (float) (player.prevPosX + (player.posX - player.prevPosX) * partialTicks);
        float posY = (float) (player.prevPosY + (player.posY - player.prevPosY) * partialTicks);
        float posZ = (float) (player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks);

        float tagscale = RenderEventHandler.tagscale;

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glPushMatrix();
        GL11.glTranslatef(
            -posX + x + 0.5f + (2f * tagscale * dir.offsetX),
            -posY + y + 0.5f + (2f * tagscale * dir.offsetY),
            -posZ + z + 0.5f + (2f * tagscale * dir.offsetZ));
        float xd = posX - (x + 0.5f);
        float zd = posZ - (z + 0.5f);
        float rotYaw = (float) (Math.atan2(xd, zd) * 180.0f / Math.PI);
        GL11.glRotatef(rotYaw, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(tagscale, -tagscale, tagscale);

        GL11.glPushMatrix();
        // Thaumcraft's scale is quite weird... -y makes it go up while +y makes it go down...
        GL11.glScalef(0.05f, 0.05f, 0.05f);
        int sw = fontRenderer.getStringWidth(text);
        GL11.glPushMatrix();
        GL11.glTranslatef(-sw / 2f, 0, 0);
        fontRenderer.drawStringWithShadow(text, 0, 0, 0xffffff);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public static BufferedImage downloadImage(String url) throws Exception {
        try (InputStream in = new URL(url).openStream()) {
            return ImageIO.read(in);
        }
    }

    public static ResourceLocation loadTextureFromUrl(String url, String name) throws Exception {
        BufferedImage image = downloadImage(url);
        if (image == null) {
            throw new RuntimeException("Failed to load image from " + url);
        }
        DynamicTexture texture = new DynamicTexture(image);
        return Minecraft.getMinecraft()
            .getTextureManager()
            .getDynamicTextureLocation(name, texture);
    }
}
