package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.lib;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import baubles.api.BaublesApi;
import thaumcraft.client.lib.REHWandHandler;

@Mixin(value = REHWandHandler.class, remap = false)
public class MixinREHWandHandler_ExtendedBaublesSupport {

    @ModifyConstant(method = "handleFociRadial", constant = @Constant(intValue = 4, ordinal = 0), remap = false)
    private int handleFociRadial(int value, Minecraft mc, long time, RenderGameOverlayEvent event) {
        return BaublesApi.getBaubles(mc.thePlayer)
            .getSizeInventory();
    }
}
