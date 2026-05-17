package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.relics;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import thaumcraft.common.items.relics.ItemHandMirror;

@Mixin(ItemHandMirror.class)
public class MixinItemHandMirror_LocalizeCorrectly {

    @Unique
    private static final ChatStyle salisArcana$mirrorStyle = new ChatStyle().setColor(EnumChatFormatting.DARK_PURPLE)
        .setItalic(true);

    @ModifyArg(
        method = "onItemUseFirst",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/EntityPlayer;addChatMessage(Lnet/minecraft/util/IChatComponent;)V"))
    public IChatComponent replaceConnectedMessage(IChatComponent par1) {
        return new ChatComponentTranslation("tc.handmirrorlinked").setChatStyle(salisArcana$mirrorStyle);
    }

    @ModifyArg(
        method = "onItemRightClick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/EntityPlayer;addChatMessage(Lnet/minecraft/util/IChatComponent;)V"))
    public IChatComponent replaceErrorMessage(IChatComponent par1) {
        return new ChatComponentTranslation("tc.handmirrorerror").setChatStyle(salisArcana$mirrorStyle);
    }

    @ModifyArg(
        method = "transport",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/EntityPlayer;addChatMessage(Lnet/minecraft/util/IChatComponent;)V"))
    private static IChatComponent replaceErrorMessage2(IChatComponent par1) {
        return new ChatComponentTranslation("tc.handmirrorerror").setChatStyle(salisArcana$mirrorStyle);
    }
}
