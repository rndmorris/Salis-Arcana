package dev.rndmorris.salisarcana.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.rndmorris.salisarcana.client.handlers.InfusionData;
import gregtech.api.gui.modularui.GTUITextures;
import thaumcraft.api.IGoggles;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.lib.RenderEventHandler;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.TileInfusionMatrix;

public class InfusionPreview {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    // Missing Enchantments and extra text ("Invalid Recipe", "Missing Research", etc)
    // Maybe add (in)stability into the text aswell? Both item instability and structure instability
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
            if (te instanceof TileInfusionMatrix matrix && matrix.validLocation()) {
                InfusionData data = new InfusionData(world, matrix).compute();

                boolean spaceAbove = event.player.worldObj.isAirBlock(xCoord, yCoord + 1, zCoord);

                // spotless:off
                ItemStack output = data.getOutputStack();
                if (output == null) return;
                data.fetchEssentiaSources();
                drawTagsOnContainer(
                        data,
                    output,
                    xCoord, yCoord + (spaceAbove ? 0.4F : 0.0F), zCoord,
                    220,
                    spaceAbove ? ForgeDirection.UP : ForgeDirection.getOrientation(event.target.sideHit),
                    event.partialTicks
                );
                //spotless:on
            }
        }
    }

    // spotless:off
    public static void drawTagsOnContainer(InfusionData data, ItemStack stack, double x, double y, double z, int bright, ForgeDirection dir, float partialTicks) {

        if (RenderEventHandler.tagscale < 0.3F) {
            RenderEventHandler.tagscale += 0.031F - RenderEventHandler.tagscale / 10.0F;
        }

        AspectList tags = data.getEssentia();
        if (Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer player && tags != null && tags.size() > 0) {
            String recipeName = data.getOutputString(stack);
            AspectList sources = data.buildAspectList();
            double posX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
            double posY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
            double posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;
            final int ROW_SIZE = 5;
            int current = 0;
            int tagsLeft = tags.size();
            int rows = (tagsLeft - 1) / ROW_SIZE + 1;

            float tagscale = RenderEventHandler.tagscale;

            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glPushMatrix();
            GL11.glTranslated(
                -posX + x + 0.5 + (tagscale * 2 * dir.offsetX),
                -posY + y + (rows * tagscale * 1.05f) + 0.5D + (tagscale * 2 * (float)dir.offsetY),
                -posZ + z + 0.5D + (tagscale * 2 * (float)dir.offsetZ));
            float xd = (float)(posX - (x + 0.5D));
            float zd = (float)(posZ - (z + 0.5D));
            float rotYaw = (float)(Math.atan2(xd, zd) * 180.0D / 3.141592653589793D);
            GL11.glRotatef(rotYaw + 180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(RenderEventHandler.tagscale, RenderEventHandler.tagscale, RenderEventHandler.tagscale);

            GL11.glScalef(0.05F, 0.05F, 0.05F);
            GL11.glTranslated(0.0D, 0, -0.1D);
            final int iconWidth = 20;
            String display = EnumChatFormatting.GREEN + "Valid Recipe Found";
            int sw = Minecraft.getMinecraft().fontRenderer.getStringWidth(display);
            GL11.glPushMatrix();
            GL11.glTranslatef(-sw / 2f, -Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT - 4, 0);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(display, 0, 0, 0xffffff);
            GL11.glPopMatrix();
            sw = Minecraft.getMinecraft().fontRenderer.getStringWidth(recipeName);
            GL11.glTranslatef(-(sw + iconWidth) / 2f, 0, 0);

            Minecraft mc = Minecraft.getMinecraft();
            new RenderItem().renderItemIntoGUI(
                mc.fontRenderer,
                mc.getTextureManager(),
                stack,
                0,
                -4
            );

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor4f(1, 1, 1, 1);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(recipeName, iconWidth, 0, 0xffffff);

            GL11.glPopMatrix();
            float shifty = 0.0f;
            for (Aspect tag : tags.getAspects()) {
                int div = Math.min(tagsLeft, ROW_SIZE);
                if (current >= ROW_SIZE) {
                    current = 0;
                    shifty -= tagscale * 1.05F;
                    tagsLeft -= ROW_SIZE;
                    if (tagsLeft < ROW_SIZE) {
                        div = tagsLeft % ROW_SIZE;
                    }
                }

                float shift = ((float)current - (float)div / 2.0F + 0.5F) * tagscale * 4.0F;
                shift = shift * tagscale;

                int color = tag.getColor();
                float red   = ((color >> 16) & 0xFF) / 255f;
                float green = ((color >> 8) & 0xFF) / 255f;
                float blue  = (color & 0xFF) / 255f;

                GL11.glPushMatrix();
                GL11.glTranslated(
                        -posX + x + 0.5 + (tagscale * 2 * dir.offsetX),
                        -posY + y - shifty + 0.5D + (tagscale * 2 * (float)dir.offsetY),
                        -posZ + z + 0.5D + (tagscale * 2 * (float)dir.offsetZ));
                xd = (float)(posX - (x + 0.5D));
                zd = (float)(posZ - (z + 0.5D));
                rotYaw = (float)(Math.atan2(xd, zd) * 180.0D / Math.PI);
                GL11.glRotatef(rotYaw + 180.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslated(shift, 0.0D, 0.0D);
                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                GL11.glScalef(RenderEventHandler.tagscale, RenderEventHandler.tagscale, RenderEventHandler.tagscale);
                if (!Thaumcraft.proxy.playerKnowledge.hasDiscoveredAspect(player.getCommandSenderName(), tag)) {
                    UtilsFX.renderQuadCenteredFromTexture(
                            "textures/aspects/_unknown.png",
                            1.0F,
                            red, green, blue,
                            bright,
                            771,
                            0.75F
                    );
                } else {
                    UtilsFX.renderQuadCenteredFromTexture(
                            tag.getImage(),
                            1.0F,
                            red, green, blue,
                            bright,
                            771,
                            0.75F
                    );
                }

                int amount = tags.getAmount(tag);
                if (amount >= 0) {
                    GL11.glPushMatrix();
                    String am = Integer.toString(amount);
                    GL11.glScalef(0.04F, 0.04F, 0.04F);
                    GL11.glTranslated(0.0D, 6.0D, -0.1D);
                    sw = Minecraft.getMinecraft().fontRenderer.getStringWidth(am);
                    GL11.glEnable(3042);
                    Minecraft.getMinecraft().fontRenderer.drawString(am, 14 - sw, 1, 1118481);
                    GL11.glTranslated(0.0D, 0.0D, -0.1D);
                    Minecraft.getMinecraft().fontRenderer.drawString(am, 13 - sw, 0, 16777215);

                    GL11.glPopMatrix();
                }

                //TODO this crashes in dev env because GTUITextures isn't a hard dep. Needs to be polled during init or something idk
                if (InfusionData.hasEnoughEssentia(sources, tag, amount)) {
                    // Minecraft.getMinecraft().renderEngine.bindTexture(GTUITextures.OVERLAY_BUTTON_CHECKMARK.location);
                } else {
                    // Minecraft.getMinecraft().renderEngine.bindTexture(GTUITextures.OVERLAY_BUTTON_CROSS.location);
                }
                GL11.glPushMatrix();
                GL11.glTranslatef(0.4f, -0.4f, 0);
                UtilsFX.renderQuadCenteredFromTexture(
                        2/3f,
                        1, 1, 1,
                        0,
                        771,
                        1f
                );
                GL11.glPopMatrix();

                GL11.glPopMatrix();
                current++;
            }
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

    }
    //spotless:on
}
