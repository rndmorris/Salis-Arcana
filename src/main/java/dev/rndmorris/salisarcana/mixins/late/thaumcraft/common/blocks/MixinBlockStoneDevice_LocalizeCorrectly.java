package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import thaumcraft.common.blocks.BlockStoneDevice;

@Mixin(BlockStoneDevice.class)
public class MixinBlockStoneDevice_LocalizeCorrectly {

    @Unique
    private static final ChatStyle salisArcana$redTextStyle = new ChatStyle().setColor(EnumChatFormatting.RED);

    @ModifyArg(
        method = "onBlockActivated",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/EntityPlayer;addChatMessage(Lnet/minecraft/util/IChatComponent;)V"))
    public IChatComponent replaceComponent(IChatComponent original) {
        return new ChatComponentTranslation("tc.researchmissing").setChatStyle(salisArcana$redTextStyle);
    }
}
