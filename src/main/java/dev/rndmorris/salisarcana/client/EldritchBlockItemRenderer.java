package dev.rndmorris.salisarcana.client;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.block.BlockRenderer;
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

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        int meta = item.getItemDamage();
        return meta <= 1 || meta == 3 || meta == 8 || meta == 9;
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
                renderTileSimple(altarTile);
                break;
            case 1:
                renderObelisk();
                break;
            case 3:
                renderTileSimple(capstoneTile);
                break;
            case 8:
                renderLockingMechanism(type, renderer);
                break;
            case 9:
                renderTileSimple(crustedOpeningTile);
                break;
        }
    }

    private void renderTileSimple(TileEntity tileEntity) {

    }

    private void renderObelisk() {

    }

    private void renderLockingMechanism(ItemRenderType type, RenderBlocks renderer) {
        final var icons = ((BlockEldritch) ConfigBlocks.blockEldritch).insIcon;

        GL11.glPushMatrix();
        if(type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        }

        renderer.setRenderBounds(0f, 0f, 0f, 1f, 1f, 1f);
        BlockRenderer.drawFaces(renderer, ConfigBlocks.blockEldritch, icons[4], icons[4], icons[4], icons[3], icons[4], icons[4], true);
        GL11.glPopMatrix();
    }
}
