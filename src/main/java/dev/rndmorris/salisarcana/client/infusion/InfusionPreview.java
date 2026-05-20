package dev.rndmorris.salisarcana.client.infusion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.common.infusion.InfusionPreviewInfo;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.network.MessageRequestInfusionPreview;
import dev.rndmorris.salisarcana.network.NetworkHandler;
import gregtech.api.gui.modularui.GTUITextures;
import thaumcraft.api.IGoggles;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.lib.RenderEventHandler;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.tiles.TileInfusionMatrix;

public class InfusionPreview {

    private static ResourceLocation checkmarkLocation;
    private static ResourceLocation crossLocation;
    private final RenderItem renderItem = new RenderItem();

    public InfusionPreview() {
        if (Loader.isModLoaded("gregtech")) { // Save on unnecessary texture allocations
            checkmarkLocation = GTUITextures.OVERLAY_BUTTON_CHECKMARK.location;
            crossLocation = GTUITextures.OVERLAY_BUTTON_CROSS.location;
        } else {
            checkmarkLocation = new ResourceLocation(SalisArcana.MODID, "textures/infusion/checkmark.png");
            crossLocation = new ResourceLocation(SalisArcana.MODID, "textures/infusion/cross.png");
        }
    }

    private static TileInfusionMatrix matrix;
    private static InfusionPreviewInfo info;

    // Refresh cadence for the server-authoritative preview.
    private static final int REQUEST_INTERVAL = 4;
    private static int ticks;

    private static void invalidate() {
        matrix = null;
        info = null;
    }

    private static void requestPreview(TileInfusionMatrix matrix) {
        NetworkHandler.instance
            .sendToServer(new MessageRequestInfusionPreview(matrix.xCoord, matrix.yCoord, matrix.zCoord));
    }

    public static void onPreviewReceived(int x, int y, int z, InfusionPreviewInfo received) {
        // Drop if the player has already invalidated or moved on to a different matrix —
        // packets in flight from a previous focus should not overwrite the current one.
        if (matrix == null || matrix.xCoord != x || matrix.yCoord != y || matrix.zCoord != z) return;
        info = received;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START || matrix == null) return;
        final World currentWorld = Minecraft.getMinecraft().theWorld;
        if (currentWorld == null || matrix.getWorldObj() != currentWorld) {
            invalidate();
            return;
        }
        MovingObjectPosition mouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (mouseOver == null || mouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            invalidate();
            return;
        }
        if (mouseOver.blockX != matrix.xCoord || mouseOver.blockY != matrix.yCoord
            || mouseOver.blockZ != matrix.zCoord) {
            invalidate();
            return;
        }

        if (++ticks >= REQUEST_INTERVAL) {
            ticks = 0;
            requestPreview(matrix);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void blockHighlight(DrawBlockHighlightEvent event) {
        final MovingObjectPosition mouseOver = event.target;
        final int xCoord = mouseOver.blockX;
        final int yCoord = mouseOver.blockY;
        final int zCoord = mouseOver.blockZ;
        // First check if the old matrix is invalid
        if (matrix != null) {
            if (matrix.xCoord != xCoord || matrix.yCoord != yCoord || matrix.zCoord != mouseOver.blockZ) {
                invalidate();
            }
        }

        ItemStack helmet = event.player.inventory.armorItemInSlot(3);
        if (helmet == null) {
            invalidate();
            return;
        }
        Item helmetItem = helmet.getItem();
        if (!(helmetItem instanceof IGoggles goggles) || !goggles.showIngamePopups(helmet, event.player)) {
            invalidate();
            return;
        }
        if (!SalisConfig.features.infusionPreview.isEnabled()) {
            invalidate();
            return;
        }
        if (!ThaumcraftApiHelper
            .isResearchComplete(event.player.getCommandSenderName(), "salisarcana:INFUSION_PREVIEW")) {
            invalidate();
            return;
        }

        final World currentWorld = Minecraft.getMinecraft().theWorld;
        // Then check if the player is hovering over a matrix
        if (matrix == null) {
            if (currentWorld.getTileEntity(xCoord, yCoord, zCoord) instanceof TileInfusionMatrix tile) {
                matrix = tile;
                // Kick an immediate request so the first render after focusing a matrix isn't
                // blank for a full REQUEST_INTERVAL.
                ticks = 0;
                requestPreview(matrix);
            } else {
                return;
            }
        }
        if (matrix.crafting) return;
        boolean spaceAbove = currentWorld.isAirBlock(xCoord, yCoord + 1, zCoord);
        float y = yCoord + (spaceAbove ? 0.4F : 0.0F);
        ForgeDirection dir = spaceAbove ? ForgeDirection.UP : ForgeDirection.getOrientation(event.target.sideHit);

        if (!matrix.active) {
            boolean unformed = WandManager
                .fitInfusionAltar(currentWorld, matrix.xCoord - 1, matrix.yCoord - 2, matrix.zCoord - 1);
            if (unformed || matrix.validLocation()) {
                drawString(
                    StatCollector.translateToLocal("salisarcana:infusion.preview.unformed"),
                    xCoord,
                    y,
                    zCoord,
                    dir,
                    event.partialTicks);
                return;
            }

            drawString(
                StatCollector.translateToLocal("salisarcana:infusion.preview.invalid"),
                xCoord,
                y,
                zCoord,
                dir,
                event.partialTicks);
            return;
        }

        // spotless:off
        if (info == null) {
            return;
        }
        ItemStack output = info.recipeOutput;
        if (output == null) {
            drawString(
                StatCollector.translateToLocal("salisarcana:infusion.preview.norecipe"),
                xCoord, y, zCoord,
                dir,
                event.partialTicks
            );
            return;
        }
        drawTagsOnContainer(
            output,
            xCoord, y, zCoord,
            dir,
            event.partialTicks
        );
        //spotless:on
    }

    // spotless:off
    private void drawTagsOnContainer(ItemStack stack, float x, float y, float z, ForgeDirection dir, float partialTicks) {
        if (!(Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer player)) return;

        AspectList tags = info.recipeEssentia;

        if (tags != null && tags.size() > 0) {
            Minecraft mc = Minecraft.getMinecraft();
            FontRenderer fontRenderer = mc.fontRenderer;
            setupRender(player, x, y, z, dir, partialTicks);
            String recipeName = info.getOutputString();
            AspectList sources = info.availableEssentia != null ? info.availableEssentia : new AspectList();

            final int ROW_SIZE = 5;
            int current = 0;
            int tagsLeft = tags.size();
            int rows = (tagsLeft - 1) / ROW_SIZE + 1;

            float tagscale = RenderEventHandler.tagscale;


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

                if (sources.getAmount(tag) >= amount) {
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
            String display = StatCollector.translateToLocal(
                missingEssentia ? "salisarcana:infusion.preview.missingessentia" : "salisarcana:infusion.preview.valid"
            );
            String instability = info.getInstabilityLevelString();

            drawStatusLine(fontRenderer, display, 1.05F);
            drawInstabilityLine(fontRenderer, instability, 2.1F);
            endRender();
        }

    }
    //spotless:on

    private void drawString(String text, float x, float y, float z, ForgeDirection dir, float partialTicks) {
        if (!(Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer player)) return;

        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fontRenderer = mc.fontRenderer;

        setupRender(player, x, y, z, dir, partialTicks);

        GL11.glPushMatrix();
        GL11.glScalef(0.05f, 0.05f, 0.05f);
        int sw = fontRenderer.getStringWidth(text);
        GL11.glTranslatef(-sw / 2f, 0, 0);
        fontRenderer.drawStringWithShadow(text, 0, 0, 0xffffff);
        GL11.glPopMatrix();

        endRender();
    }

    private void drawStatusLine(FontRenderer fontRenderer, String text, float yOffset) {
        int sw = fontRenderer.getStringWidth(text);
        GL11.glPushMatrix();
        GL11.glTranslatef(0, yOffset, 0);
        GL11.glScalef(0.05f, 0.05f, 0.05f);
        GL11.glTranslatef(-sw / 2f, -fontRenderer.FONT_HEIGHT + 1, 0);
        fontRenderer.drawStringWithShadow(text, 0, 0, 0xffffff);
        GL11.glPopMatrix();
    }

    private void drawInstabilityLine(FontRenderer fontRenderer, String instabilityText, float yOffset) {
        String prefix = StatCollector.translateToLocal("tc.inst") + ": ";
        int prefixWidth = fontRenderer.getStringWidth(prefix);
        int instabilityWidth = fontRenderer.getStringWidth(instabilityText);
        int totalWidth = prefixWidth + instabilityWidth;

        GL11.glPushMatrix();
        GL11.glTranslatef(0, yOffset, 0);
        GL11.glScalef(0.05f, 0.05f, 0.05f);
        GL11.glTranslatef(-totalWidth / 2f, -fontRenderer.FONT_HEIGHT + 1, 0);
        fontRenderer.drawStringWithShadow(prefix, 0, 0, 0xffffff);
        drawOutlinedText(fontRenderer, instabilityText, prefixWidth, 0);
        GL11.glPopMatrix();
    }

    private void drawOutlinedText(FontRenderer fontRenderer, String text, int x, int y) {
        String shadowText = EnumChatFormatting.getTextWithoutFormattingCodes(text);
        int color = 0x000000;
        if (text.startsWith(EnumChatFormatting.DARK_BLUE.toString())) {
            color = 0x4A6984;
        }
        fontRenderer.drawString(shadowText, x + 1, y, color);
        fontRenderer.drawString(shadowText, x - 1, y, color);
        fontRenderer.drawString(shadowText, x, y + 1, color);
        fontRenderer.drawString(shadowText, x, y - 1, color);
        fontRenderer.drawString(text, x, y, 0xffffff);
    }

    private void setupRender(EntityPlayer player, float x, float y, float z, ForgeDirection dir, float partialTicks) {
        if (RenderEventHandler.tagscale < 0.3F) {
            RenderEventHandler.tagscale += 0.031F - RenderEventHandler.tagscale / 10.0F;
        }

        float posX = (float) (player.prevPosX + (player.posX - player.prevPosX) * partialTicks);
        float posY = (float) (player.prevPosY + (player.posY - player.prevPosY) * partialTicks);
        float posZ = (float) (player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks);

        float tagscale = RenderEventHandler.tagscale;

        GL11.glDisable(GL11.GL_DEPTH_TEST);

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
    }

    private void endRender() {
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }
}
