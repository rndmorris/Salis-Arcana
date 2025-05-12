package dev.rndmorris.salisarcana.client;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
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
        return meta <= 1 || meta == 3 || meta >= 7;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
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
            case 7:
                renderBlock(ConfigBlocks.blockEldritch.icon);
        }
    }

    private void renderTileSimple(TileEntity tileEntity) {

    }

    private void renderObelisk() {

    }

    private void renderBlock(IIcon... sides) {

    }
}
