package dev.rndmorris.salisarcana.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import thaumcraft.common.Thaumcraft;

public class BlockPlank extends Block {

    private IIcon iconSilverwood;
    private IIcon iconGreatwood;

    public BlockPlank() {
        super(Material.wood);
        this.setHardness(2.5F);
        this.setResistance(10.0F);
        this.setStepSound(soundTypeWood);
        this.setCreativeTab(Thaumcraft.tabTC);
        this.setBlockName("blockCustomPlank");
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir) {
        this.iconGreatwood = ir.registerIcon("thaumcraft:planks_greatwood");
        this.iconSilverwood = ir.registerIcon("thaumcraft:planks_silverwood");
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    public IIcon getIcon(int par1, int par2) {
        return par2 == 0 ? this.iconGreatwood : this.iconSilverwood;
    }
}
