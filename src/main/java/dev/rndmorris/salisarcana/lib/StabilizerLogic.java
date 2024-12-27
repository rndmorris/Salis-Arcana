package dev.rndmorris.salisarcana.lib;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.api.crafting.IInfusionStabiliser;

public class StabilizerLogic {

    public static boolean isStabilizer(World world, int x, int y, int z) {
        final var block = world.getBlock(x, y, z);
        final var metadata = world.getBlockMetadata(x, y, z);

        final var additions = ConfigModuleRoot.enhancements.stabilizerAdditions;
        if (additions.isEnabled() && additions.hasMatch(block, metadata)) {
            return true;
        }

        final var isStabilizer = block == Blocks.skull
            || (block instanceof IInfusionStabiliser stabilizer && stabilizer.canStabaliseInfusion(world, x, y, z));

        final var exclusions = ConfigModuleRoot.enhancements.stabilizerExclusions;
        if (exclusions.isEnabled() && exclusions.hasMatch(block, metadata)) {
            return false;
        }

        return isStabilizer;
    }

}
