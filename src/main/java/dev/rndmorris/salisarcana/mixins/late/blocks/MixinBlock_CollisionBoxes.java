package dev.rndmorris.salisarcana.mixins.late.blocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Redirect;
import thaumcraft.common.blocks.BlockAiry;
import thaumcraft.common.blocks.BlockAlchemyFurnace;
import thaumcraft.common.blocks.BlockArcaneFurnace;
import thaumcraft.common.blocks.BlockMetalDevice;
import thaumcraft.common.blocks.BlockStoneDevice;
import thaumcraft.common.blocks.BlockTube;
import thaumcraft.common.blocks.BlockWoodenDevice;

@Mixin({BlockAiry.class, BlockAlchemyFurnace.class, BlockArcaneFurnace.class, BlockMetalDevice.class, BlockStoneDevice.class, BlockTube.class, BlockWoodenDevice.class})
public class MixinBlock_CollisionBoxes {

    @Redirect(method = "addCollisionBoxesToList", at = @At(value = "INVOKE", desc = @Desc(value = "setBlockBoundsBasedOnState", args = {net.minecraft.world.IBlockAccess.class, int.class, int.class, int.class})))
    public void setBoundingBox() {

    }
}
