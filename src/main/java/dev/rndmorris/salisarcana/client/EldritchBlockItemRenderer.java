package dev.rndmorris.salisarcana.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.lib.R;
import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.common.blocks.BlockCosmeticSolid;
import thaumcraft.common.blocks.BlockEldritch;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileEldritchAltar;
import thaumcraft.common.tiles.TileEldritchCap;
import thaumcraft.common.tiles.TileEldritchCrabSpawner;
import thaumcraft.common.tiles.TileEldritchObelisk;

public class EldritchBlockItemRenderer implements IItemRenderer {

    public static void register() {
        final var item = Item.getItemFromBlock(ConfigBlocks.blockEldritch);
        if (item != null) {
            try {
                MinecraftForgeClient.registerItemRenderer(item, new EldritchBlockItemRenderer());
            } catch (Exception e) {
                SalisArcana.LOG.error("Unable to register item renderer for BlockEldritch", e);
            }
        }
    }

    private final TileEldritchAltar altarTile = new TileEldritchAltar();
    private final TileEldritchCap capstoneTile = new TileEldritchCap();
    private final TileEldritchCrabSpawner crustedOpeningTile = new TileEldritchCrabSpawner();
    private final TileEldritchObelisk obeliskTile = new TileEldritchObelisk();

    public EldritchBlockItemRenderer() {
        R.of(crustedOpeningTile)
            .set("facing", (byte) 5);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        int meta = item.getItemDamage();
        return meta <= 2 || meta == 3 || meta == 8 || meta == 9;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        RenderBlocks renderer = (RenderBlocks) data[0];

        switch (item.getItemDamage()) {
            case 0:
                renderTileSimple(type, altarTile);
                break;
            case 1:
            case 2:
                renderObelisk(type);
                break;
            case 3:
                renderTileSimple(type, capstoneTile);
                break;
            case 8:
                final var icons = ((BlockEldritch) ConfigBlocks.blockEldritch).insIcon;
                drawBlock(type, renderer, icons[4], icons[4], icons[4], icons[3], icons[4], icons[4]);
                break;
            case 9:
                drawBlock(type, renderer, ((BlockCosmeticSolid) ConfigBlocks.blockCosmeticSolid).icon[25]);
                renderTileSimple(type, crustedOpeningTile);
                break;
        }
    }

    private void drawBlock(ItemRenderType type, RenderBlocks renderer, IIcon icon) {
        drawBlock(type, renderer, icon, icon, icon, icon, icon, icon);
    }

    private void drawBlock(ItemRenderType type, RenderBlocks renderer, IIcon icon1, IIcon icon2, IIcon icon3,
        IIcon icon4, IIcon icon5, IIcon icon6) {
        if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        }
        renderer.setRenderBounds(0f, 0f, 0f, 1f, 1f, 1f);
        BlockRenderer.drawFaces(renderer, ConfigBlocks.blockEldritch, icon1, icon2, icon3, icon4, icon5, icon6, true);
        if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glPopMatrix();
        }
    }

    private void renderTileSimple(ItemRenderType type, TileEntity tileEntity) {
        final double shift = (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) ? 0d
            : -0.5d;
        TileEntityRendererDispatcher.instance.renderTileEntityAt(tileEntity, shift, shift, shift, 0f);
    }

    private void renderObelisk(ItemRenderType type) {
        final var viewpoint = Minecraft.getMinecraft().renderViewEntity;

        // Prevent far-away rendering optimizations from triggering.
        obeliskTile.xCoord = MathHelper.floor_double(viewpoint.posX);
        obeliskTile.yCoord = MathHelper.floor_double(viewpoint.posY);
        obeliskTile.zCoord = MathHelper.floor_double(viewpoint.posZ);

        GL11.glScalef(0.35f, 0.35f, 0.35f);

        final double shift = (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) ? 0d
            : -0.5d;

        TileEntityRendererDispatcher.instance.renderTileEntityAt(obeliskTile, shift, -2d + shift, shift, 0f);

        // Obelisk Renderer overrides some flags which the inventory needs to be able to render
        GL11.glEnable(0x803A); // '耺' - GL_RESCALE_NORMAL
    }
}
