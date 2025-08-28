/*
 * Copyright (c) 2015 Christopher "BlayTheNinth" Baker
 * Licensed under the MIT License
 * Taken from ThaumicInventoryScanning
 * https://github.com/GTNewHorizons/ThaumicInventoryScanning
 */

package dev.rndmorris.salisarcana.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.inventory.SlotMerchantResult;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.mixins.early.accessor.AccessorGuiContainer;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ScanResult;
import thaumcraft.client.lib.ClientTickEventsFML;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketScannedToServer;
import thaumcraft.common.lib.research.ScanManager;

public class ThaumicInventoryScanner {

    private static final int SOUND_TICKS = 5;
    private static final int INVENTORY_PLAYER_X = 26;
    private static final int INVENTORY_PLAYER_Y = 8;
    private static final int INVENTORY_PLAYER_WIDTH = 52;
    private static final int INVENTORY_PLAYER_HEIGHT = 70;

    private boolean isValidSlot = false;
    private Item thaumometer;
    /**
     * Slot the cursor is hovering over
     **/
    private Slot hoveringSlot;
    private Slot lastHoveredSlot;
    private int ticksHovered = 0;
    private ClientTickEventsFML effectRenderer;
    private ScanResult currentScan = null;
    /**
     * Is the cursor hovering above the player sprite in Inventory
     **/
    private boolean isHoveringOverPlayer;
    public static ThaumicInventoryScanner instance;

    // +5 is added to the duration to account for the fact that in the config, we set the duration to be the total usage
    // duration - 5 since thaumcraft completes the scan when the remaining duration is <= 5
    private static int SCAN_TICKS;

    public void onServerInstallStatusChanged(boolean newValue) {
        if (newValue) {
            MinecraftForge.EVENT_BUS.register(this);
            FMLCommonHandler.instance()
                .bus()
                .register(this);
        } else {
            MinecraftForge.EVENT_BUS.unregister(this);
            FMLCommonHandler.instance()
                .bus()
                .unregister(this);
        }
    }

    /**
     * Checks whether the currently hovered item or player would be a valid scan and sets currentScan and isValidScan
     * accordingly
     **/

    public void init(FMLInitializationEvent event) {
        instance = this;
        effectRenderer = new ClientTickEventsFML();
    }

    public void postInit(FMLPostInitializationEvent event) {
        thaumometer = GameRegistry.findItem("Thaumcraft", "ItemThaumometer");
        SCAN_TICKS = SalisConfig.features.thaumometerDuration.getValue() + 5;
    }

    private void simulateScan(EntityPlayer player) {
        // Handle scanning player
        ScanResult result = new ScanResult((byte) 2, 0, 0, player, "");
        if (isHoveringOverPlayer && ScanManager.isValidScanTarget(player, result, "@")) {
            currentScan = result;
            isValidSlot = true;
            return;
        }
        // Handle scanning item
        if (hoveringSlot != null && hoveringSlot.getStack() != null) {
            // spotless:off

            // spotless made this completely unreadable
            result = new ScanResult(
                (byte) 1,
                Item.getIdFromItem(hoveringSlot.getStack().getItem()),
                hoveringSlot.getStack().getItemDamage(),
                null,
                ""
            );
            // spotless:on
            if (hoveringSlot.canTakeStack(player) && !(hoveringSlot instanceof SlotCrafting)
                && !(hoveringSlot instanceof SlotMerchantResult)
                && ScanManager.isValidScanTarget(player, result, "@")
                && !ScanManager.getScanAspects(result, Minecraft.getMinecraft().theWorld.provider.worldObj).aspects
                    .isEmpty()) {
                currentScan = result;
                isValidSlot = true;
                return;
            }
        }
        // Invalid scan
        cancel();
    }

    private boolean notHoldingThaumometer(EntityPlayer player) {
        return player == null || player.inventory.getItemStack() == null
            || player.inventory.getItemStack()
                .getItem() != thaumometer;
    }

    private void cancel() {
        ticksHovered = 0;
        currentScan = null;
        isValidSlot = false;
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        // Get minecraft and player objects
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (notHoldingThaumometer(player)) {
            cancel();
            return;
        }
        // Check if the cursor moved to a different slot
        if (hoveringSlot == lastHoveredSlot) {
            // Slots did not change
            if (isValidSlot) {
                // Scan Slot
                ticksHovered++;
                playScanningSoundTick(player);
                if (ticksHovered >= SCAN_TICKS) tryCompleteScan(player);
            } else {
                // Check if there was a sudden jump to player sprite, otherwise do nothing
                if (isHoveringOverPlayer) {
                    cancel();
                    simulateScan(player);
                }
            }
        } else {
            // Slots have changed, recheck if the new one can be scanned
            cancel();
            simulateScan(player);
        }
        lastHoveredSlot = hoveringSlot;
    }

    /**
     * Completes a Scan if currentScan was set to a valid target, will complete self and item scans
     **/
    private void tryCompleteScan(EntityPlayer player) {
        if (ScanManager.completeScan(player, currentScan, "@")) {
            // use have to use thaum's packet handler here since PacketScannedToServer is a thaumcraft packet, not a
            // salis packet
            PacketHandler.INSTANCE.sendToServer(new PacketScannedToServer(this.currentScan, player, "@"));
        }
        cancel();
    }

    /**
     * Plays one tick of the scanning sound, needs to be called every tick while scanning while incrementing
     * ticksHovered
     **/
    private void playScanningSoundTick(EntityPlayer entityPlayer) {
        if (ticksHovered > SOUND_TICKS && ticksHovered % 2 == 0) {
            entityPlayer.worldObj.playSound(
                entityPlayer.posX,
                entityPlayer.posY,
                entityPlayer.posZ,
                "thaumcraft:cameraticks",
                0.2F,
                0.45F + entityPlayer.worldObj.rand.nextFloat() * 0.1F,
                false);
        }
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (event.itemStack.getItem() == thaumometer) {
            event.toolTip.add("§6" + I18n.format("salisarcana:tcinventoryscan.thaumometerTooltip"));
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                String[] lines = I18n.format("salisarcana:tcinventoryscan.thaumometerTooltipMore")
                    .split("\\\\n");
                for (String line : lines) {
                    event.toolTip.add("§3" + line);
                }
            }
        }
    }

    @SubscribeEvent
    public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.gui instanceof GuiContainer) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.thePlayer;
            if (notHoldingThaumometer(player)) return;
            // Calculate the slot the cursor is hovering over
            isHoveringOverPlayer = isHoveringPlayer((GuiContainer) event.gui, event.mouseX, event.mouseY);
            hoveringSlot = ((AccessorGuiContainer) event.gui).getHoveredSlot();
            boolean stackExists = !(hoveringSlot == null || hoveringSlot.getStack() == null);
            if (!isHoveringOverPlayer && !stackExists) return;
            // If there's something being scanned
            if (currentScan != null && stackExists) {
                renderScanningProgress(event.gui, event.mouseX, event.mouseY, ticksHovered / (float) SCAN_TICKS);
            } else {
                // Display Tooltips and aspects
                if (!isHoveringOverPlayer) {
                    event.gui.renderToolTip(hoveringSlot.getStack(), event.mouseX, event.mouseY);
                    effectRenderer.renderAspectsInGui((GuiContainer) event.gui, player);
                } else {
                    renderPlayerAspects(event.gui, event.mouseX, event.mouseY);
                }
            }
        }
    }

    // copied from MixinClientTickEvents
    public void renderPlayerAspects(GuiScreen gui, int mouseX, int mouseY) {
        EntityPlayer entityPlayer = FMLClientHandler.instance()
            .getClientPlayerEntity();
        AspectList tags = ScanManager.generateEntityAspects(entityPlayer);
        if (tags != null && tags.size() > 0) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            final int shiftX, shiftY;
            if (Thaumcraft.instance.aspectShift) {
                shiftX = -16;
                shiftY = -16;
            } else {
                shiftX = -8;
                shiftY = -8;
            }
            final String name = entityPlayer.getCommandSenderName();
            final int y = mouseY + 7 - 33;
            final float zLevel = UtilsFX.getGuiZLevel(gui);

            int x = mouseX + 17;
            for (Aspect tag : tags.getAspectsSortedAmount()) {
                if (tag != null) {

                    UtilsFX.bindTexture("textures/aspects/_back.png");
                    GL11.glPushMatrix();
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glTranslated(x + shiftX - 2, y + shiftY - 2, 0.0D);
                    GL11.glScaled(1.25D, 1.25D, 0.0D);
                    UtilsFX.drawTexturedQuadFull(0, 0, zLevel);
                    GL11.glPopMatrix();

                    if (Thaumcraft.proxy.playerKnowledge.hasDiscoveredAspect(name, tag)) {
                        UtilsFX.drawTag(x + shiftX, y + shiftY, tag, (float) tags.getAmount(tag), 0, zLevel);
                    } else {
                        UtilsFX.bindTexture("textures/aspects/_unknown.png");
                        UtilsFX.drawTexturedQuadFull(x + shiftX, y + shiftY, zLevel);
                    }

                    x += 18;
                }
            }

            GL11.glDisable(GL11.GL_BLEND);

            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
    }

    public void renderScanningProgress(GuiScreen gui, int mouseX, int mouseY, float progress) {
        StringBuilder sb = new StringBuilder("§6");
        sb.append(I18n.format("salisarcana:tcinventoryscan.scanning"));
        if (progress >= 0.75f) {
            sb.append("...");
        } else if (progress >= 0.5f) {
            sb.append("..");
        } else if (progress >= 0.25f) {
            sb.append(".");
        }
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        float oldZLevel = gui.zLevel;
        gui.zLevel = 300;
        Minecraft.getMinecraft().fontRenderer
            .drawStringWithShadow(sb.toString(), mouseX, mouseY - 30, Integer.MAX_VALUE);
        gui.zLevel = oldZLevel;
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }

    public boolean isHoveringPlayer(GuiContainer gui, int mouseX, int mouseY) {
        return gui instanceof GuiInventory && mouseX >= gui.guiLeft + INVENTORY_PLAYER_X
            && mouseX < gui.guiLeft + INVENTORY_PLAYER_X + INVENTORY_PLAYER_WIDTH
            && mouseY >= gui.guiTop + INVENTORY_PLAYER_Y
            && mouseY < gui.guiTop + INVENTORY_PLAYER_Y + INVENTORY_PLAYER_HEIGHT;
    }
}
