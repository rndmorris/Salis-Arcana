package dev.rndmorris.salisarcana.common.compat;

import java.util.Collections;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.Loader;
import dev.rndmorris.salisarcana.lib.R;
import exterminatorJeff.undergroundBiomes.common.UndergroundBiomes;
import exterminatorJeff.undergroundBiomes.common.block.StoneSlabPair;
import thaumcraft.common.items.equipment.ItemPrimalCrusher;

public class ModCompat {

    public static void init() {
        if (Loader.isModLoaded("UndergroundBiomes")) {
            ubcPrimalCrusherCompat();
        }
    }

    private static void ubcPrimalCrusherCompat() {
        OreDictionary.getOres("stone");
        final var primalCrusher = new R(ItemPrimalCrusher.class);
        @SuppressWarnings("unchecked")
        final Set<Block> blockSet = primalCrusher.get("isEffective", Set.class);

        final var ubcSlabs = new StoneSlabPair[] { UndergroundBiomes.igneousStoneSlab,
            UndergroundBiomes.igneousCobblestoneSlab, UndergroundBiomes.igneousBrickSlab,
            UndergroundBiomes.metamorphicStoneSlab, UndergroundBiomes.metamorphicCobblestoneSlab,
            UndergroundBiomes.metamorphicBrickSlab, UndergroundBiomes.sedimentaryStoneSlab, };

        for (var slab : ubcSlabs) {
            Collections.addAll(blockSet, slab.half, slab.full);
        }
    }
}
