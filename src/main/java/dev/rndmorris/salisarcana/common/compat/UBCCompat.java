package dev.rndmorris.salisarcana.common.compat;

import java.util.Collections;
import java.util.Set;

import net.minecraft.block.Block;

import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.R;
import exterminatorJeff.undergroundBiomes.common.UndergroundBiomes;
import exterminatorJeff.undergroundBiomes.common.block.StoneSlabPair;
import thaumcraft.common.items.equipment.ItemPrimalCrusher;

public class UBCCompat {

    public static void init() {
        final var undergroundBiomes = SalisConfig.modCompat.undergroundBiomes;
        if (!undergroundBiomes.isEnabled()) {
            return;
        }
        if (undergroundBiomes.primalCrusherMinesUBCSlabs.isEnabled()) {
            primalCrusherCompat();
        }
    }

    private static void primalCrusherCompat() {
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
