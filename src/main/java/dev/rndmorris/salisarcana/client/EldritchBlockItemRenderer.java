package dev.rndmorris.salisarcana.client;

import dev.rndmorris.salisarcana.lib.R;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.common.blocks.BlockCosmeticSolid;
import thaumcraft.common.blocks.BlockEldritch;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileEldritchAltar;
import thaumcraft.common.tiles.TileEldritchCap;
import thaumcraft.common.tiles.TileEldritchCrabSpawner;
import thaumcraft.common.tiles.TileEldritchObelisk;

public class EldritchBlockItemRenderer implements IItemRenderer {
    private final TileEldritchAltar altarTile = new TileEldritchAltar();
    private final TileEldritchCap capstoneTile = new TileEldritchCap();
    private final TileEldritchCrabSpawner crustedOpeningTile = new TileEldritchCrabSpawner();
    private final TileEldritchObelisk obeliskTile = new TileEldritchObelisk();

    public EldritchBlockItemRenderer() {
        // TODO Check for obfuscated compatibility
        R.of(crustedOpeningTile).set("facing", (byte) 5);
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

        GL11.glPushMatrix();
        if(type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        }

        switch (item.getItemDamage()) {
            case 0:
                renderTileSimple(altarTile, 0d);
                break;
            case 1:
            case 2:
                renderObelisk();
                break;
            case 3:
                renderTileSimple(capstoneTile, 0d);
                break;
            case 8:
                final var icons = ((BlockEldritch) ConfigBlocks.blockEldritch).insIcon;
                renderer.setRenderBounds(0f, 0f, 0f, 1f, 1f, 1f);
                BlockRenderer.drawFaces(renderer, ConfigBlocks.blockEldritch, icons[4], icons[4], icons[4], icons[3], icons[4], icons[4], true);
                break;
            case 9:
                final var icon = ((BlockCosmeticSolid) ConfigBlocks.blockCosmeticSolid).icon[25];
                renderer.setRenderBounds(0f, 0f, 0f, 1f, 1f, 1f);
                BlockRenderer.drawFaces(renderer, ConfigBlocks.blockEldritch, icon, true);

                // For some reason, the tile entity doesn't match up with the block on its own.
                renderTileSimple(crustedOpeningTile, -0.1d);
                break;
        }

        GL11.glPopMatrix();
    }

    private void renderTileSimple(TileEntity tileEntity, double y) {
        TileEntityRendererDispatcher.instance.renderTileEntityAt(tileEntity, 0d, y, 0d, 0f);
    }

    private void renderObelisk() {
        final var viewpoint = Minecraft.getMinecraft().renderViewEntity;

        // Prevent far-away rendering optimizations from triggering.
        obeliskTile.xCoord = MathHelper.floor_double(viewpoint.posX);
        obeliskTile.yCoord = MathHelper.floor_double(viewpoint.posY);
        obeliskTile.zCoord = MathHelper.floor_double(viewpoint.posZ);

        GL11.glScalef(0.35f, 0.35f, 0.35f);

        TileEntityRendererDispatcher.instance.renderTileEntityAt(obeliskTile,0.5d, -1.75d, 0.5d, 0f);

        // Obelisk Renderer overrides some flags which the inventory needs to be able to render
        GL11.glEnable(0x803A); // '耺' - GL_RESCALE_NORMAL
    }
}
