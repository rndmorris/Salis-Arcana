package dev.rndmorris.salisarcana.lib;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import dev.rndmorris.salisarcana.SalisArcana;
import thaumcraft.common.config.ConfigBlocks;

public class CrucibleHeatLogic {

    /**
     * Meant to be called from the relevant TileCrucible and TileThaumatorium mixins, where we target the
     * {@code mat == Material.lava} check.
     * All of these parameters already exist as locals in the relevant target methods, so we re-use them instead of
     * getting them again ourselves.
     */
    public static boolean isCrucibleHeatSource(Block block, int blockMetadata, Material blockMaterial) {
        // Maintain default behavior. If the block's material is lava or fire, or the block is Nitor, it can already
        // heat the crucible.
        if (blockMaterial == Material.lava || blockMaterial == Material.fire
            || (block == ConfigBlocks.blockAiry && blockMetadata == 1)) {
            return true;
        }

        // Then check the ore dictionary.
        final var oreIds = OreDictionary.getOreIDs(new ItemStack(block, 1, blockMetadata));
        return ArrayHelper.indexOf(oreIds, SalisArcana.proxy.oreDictIds.heatSource) > -1;
    }
}
