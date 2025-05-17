package dev.rndmorris.salisarcana.client;

import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.lib.R;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.tile.TileEldritchNothingRenderer;
import thaumcraft.common.config.ConfigBlocks;

public class EldritchNothingItemRenderer implements IItemRenderer {
    public static void register() {
        final var item = Item.getItemFromBlock(ConfigBlocks.blockEldritchNothing);
        if(item != null) {
            try {
                MinecraftForgeClient.registerItemRenderer(item, new EldritchNothingItemRenderer());
            } catch (RuntimeException e) {
                SalisArcana.LOG.error("Unable to register item renderer for BlockEldritchNothing", e);
            }
        }
    }

    private final TileEldritchNothingRenderer renderer = new TileEldritchNothingRenderer();

    public EldritchNothingItemRenderer() {
        R.of(renderer).set("inrange", true);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        if(type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslatef(0f, 0.1f, 0f);
        } else {
            GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
        }

        final double playerX = TileEntityRendererDispatcher.staticPlayerX;
        final double playerY = TileEntityRendererDispatcher.staticPlayerY;
        final double playerZ = TileEntityRendererDispatcher.staticPlayerZ;

        TileEntityRendererDispatcher.staticPlayerX /= 10d;
        TileEntityRendererDispatcher.staticPlayerY /= 10d;
        TileEntityRendererDispatcher.staticPlayerZ /= 10d;

        this.renderer.drawPlaneXNeg(0,0,0,0f);
        this.renderer.drawPlaneXPos(0,0,0,0f);
        this.renderer.drawPlaneYNeg(0,0,0,0f);
        this.renderer.drawPlaneYPos(0,0,0,0f);
        this.renderer.drawPlaneZNeg(0,0,0,0f);
        this.renderer.drawPlaneZPos(0,0,0,0f);

        TileEntityRendererDispatcher.staticPlayerX = playerX;
        TileEntityRendererDispatcher.staticPlayerY = playerY;
        TileEntityRendererDispatcher.staticPlayerZ = playerZ;

        GL11.glPopMatrix();
    }
}
