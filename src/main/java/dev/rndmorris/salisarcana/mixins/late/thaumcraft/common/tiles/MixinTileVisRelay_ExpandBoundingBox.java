package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.tileentity.TileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.common.tiles.TileVisRelay;

@Mixin(value = TileVisRelay.class, remap = false)
public class MixinTileVisRelay_ExpandBoundingBox extends TileEntity {

    @Unique
    private static final double sa$Expanded = SalisConfig.features.visRelayBoxExpansion.getValue();

    // modifying a call to AxisAlignedBB::expand(5.0D, 5.0D, 5.0D)
    @ModifyConstant(method = "updateEntity", constant = @Constant(doubleValue = 5.0D))
    private double modifyBoundingBox(double original) {
        return sa$Expanded;
    }
}
