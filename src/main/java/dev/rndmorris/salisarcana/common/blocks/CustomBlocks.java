package dev.rndmorris.salisarcana.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;
import dev.rndmorris.salisarcana.common.item.BlockPlankItem;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.common.config.ConfigBlocks;

public class CustomBlocks {

    public static Block blockPlank;

    public static final String ORE_DICT_GREATWOOD_PLANKS = "plankGreatwood";
    public static final String ORE_DICT_SILVERWOOD_PLANKS = "plankSilverwood";

    public static void registerBlocks() {

        if (ConfigModuleRoot.enhancements.lookalikePlanks.isEnabled()) {
            registerLookaLikePlanks();
        }

    }

    private static void registerLookaLikePlanks() {
        blockPlank = new BlockPlank();
        GameRegistry.registerBlock(blockPlank, BlockPlankItem.class, "blockCustomPlank");
        // To-do: is this needed? It appears to break the ore-dicted slab and stair recipes
        OreDictionary.registerOre("plankWood", new ItemStack(blockPlank, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre(ORE_DICT_GREATWOOD_PLANKS, new ItemStack(blockPlank, 1, 0));
        OreDictionary.registerOre(ORE_DICT_SILVERWOOD_PLANKS, new ItemStack(blockPlank, 1, 1));

        OreDictionary.registerOre(ORE_DICT_GREATWOOD_PLANKS, new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6));
        OreDictionary.registerOre(ORE_DICT_SILVERWOOD_PLANKS, new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 7));
    }

}
