package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.common.lib.utils.Utils;

@Mixin(Utils.class)
public class MixinUtils_UpdateBiomeColor {

    @Inject(method = "setBiomeAt", at = @At("TAIL"), remap = false)
    private static void updateClientColor(World world, int x, int z, BiomeGenBase biome, CallbackInfo ci) {
        // Important: apply only to client instances. - setSide(Side.CLIENT)
        if (biome != null && world.isRemote) {
            Minecraft.getMinecraft().renderGlobal.markBlockRangeForRenderUpdate(x - 1, 0, z - 1, x + 1, 256, z + 1);
        }
    }
}
