package dev.rndmorris.tfixins.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;
import dev.rndmorris.tfixins.common.item.BlockPlankItem;
import thaumcraft.common.config.ConfigBlocks;

public class CustomBlocks {

    public static Block blockPlank;

    public static void registerBlocks() {
        blockPlank = new BlockPlank();
        GameRegistry.registerBlock(blockPlank, BlockPlankItem.class, "blockCustomPlank");
        // To-do: is this needed? It appears to break the ore-dicted slab and stair recipes
        OreDictionary.registerOre("plankWood", new ItemStack(blockPlank, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("greatwoodPlanks", new ItemStack(blockPlank, 1, 0));
        OreDictionary.registerOre("silverwoodPlanks", new ItemStack(blockPlank, 1, 1));

        OreDictionary.registerOre("greatwoodPlanks", new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6));
        OreDictionary.registerOre("silverwoodPlanks", new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 7));
    }

}
