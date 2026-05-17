package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.events;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import thaumcraft.common.lib.events.EventHandlerEntity;

@Mixin(EventHandlerEntity.class)
public class MixinEventHandlerEntity_LocalizeCorrectly {

    @Unique
    private static final ChatStyle salisArcana$disableFlyStyle = new ChatStyle().setColor(EnumChatFormatting.GRAY)
        .setItalic(true);

    @Unique
    private static final ChatStyle salisArcana$enderPearlStyle = new ChatStyle()
        .setColor(EnumChatFormatting.DARK_PURPLE)
        .setItalic(true);

    @Unique
    private static final ChatStyle salisArcana$hungerFadesStyle = new ChatStyle()
        .setColor(EnumChatFormatting.DARK_GREEN)
        .setItalic(true);

    @Unique
    private static final ChatStyle salisArcana$hungerUnsatisfiedStyle = new ChatStyle()
        .setColor(EnumChatFormatting.DARK_RED)
        .setItalic(true);

    @ModifyArg(
        method = "livingTick(Lnet/minecraftforge/event/entity/living/LivingEvent$LivingUpdateEvent;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/EntityPlayer;addChatMessage(Lnet/minecraft/util/IChatComponent;)V"))
    public IChatComponent disableFlyComponent(IChatComponent original) {
        return new ChatComponentTranslation("tc.break.fly").setChatStyle(salisArcana$disableFlyStyle);
    }

    @ModifyConstant(
        method = "playerInteract",
        constant = @Constant(stringValue = "You are not my Master!"),
        remap = false)
    public String golemPlayerRejectionKey(String constant) {
        return "salisarcana:misc.golem.reject_player";
    }

    @ModifyArg(
        method = "entitySpawns",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/EntityPlayer;addChatMessage(Lnet/minecraft/util/IChatComponent;)V"))
    public IChatComponent enderPearlCancelComponent(IChatComponent original) {
        return new ChatComponentTranslation("salisarcana:misc.warded.destroy_ender_pearl")
            .setChatStyle(salisArcana$enderPearlStyle);
    }

    @ModifyArg(
        method = "finishedUsingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/EntityPlayer;addChatMessage(Lnet/minecraft/util/IChatComponent;)V",
            ordinal = 0))
    public IChatComponent unsatisfiedHungerComponent(IChatComponent original) {
        return new ChatComponentTranslation("warp.text.hunger.1").setChatStyle(salisArcana$hungerUnsatisfiedStyle);
    }

    @ModifyArg(
        method = "finishedUsingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/EntityPlayer;addChatMessage(Lnet/minecraft/util/IChatComponent;)V",
            ordinal = 1))
    public IChatComponent fadingHungerComponent(IChatComponent original) {
        return new ChatComponentTranslation("warp.text.hunger.2").setChatStyle(salisArcana$hungerFadesStyle);
    }
}
