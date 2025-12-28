package dev.rndmorris.salisarcana.mixins.late.tuhljin.automagy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import tuhljin.automagy.blocks.BlockBoiler;

@Mixin(BlockBoiler.class)
public class MixinBlockBoiler_FakePlayer extends Block {

    protected MixinBlockBoiler_FakePlayer(Material materialIn) {
        super(materialIn);
    }

    @WrapOperation(
        method = "onBlockActivated",
        at = @At(
            value = "INVOKE",
            target = "Lcpw/mods/fml/common/network/internal/FMLNetworkHandler;openGui(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/Object;ILnet/minecraft/world/World;III)V",
            remap = false))
    private void sa$dontOpenFakePlayerGui(EntityPlayer entityPlayer, Object obj, int modGuiId, World world, int x,
        int y, int z, Operation<Void> original) {
        if (entityPlayer instanceof FakePlayer) {
            return;
        }
        original.call(entityPlayer, obj, modGuiId, world, x, y, z);
    }
}
