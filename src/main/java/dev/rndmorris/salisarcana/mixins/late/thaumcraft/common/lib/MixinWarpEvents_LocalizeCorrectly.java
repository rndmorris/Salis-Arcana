package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import thaumcraft.common.lib.WarpEvents;

@Mixin(WarpEvents.class)
public class MixinWarpEvents_LocalizeCorrectly {

    @Unique
    private static final ChatStyle salisArcana$warpChatStyle = new ChatStyle().setColor(EnumChatFormatting.DARK_PURPLE)
        .setItalic(true);

    @WrapOperation(
        method = { "checkWarpEvent", "spawnMist", "suddenlySpiders" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/StatCollector;translateToLocal(Ljava/lang/String;)Ljava/lang/String;"))
    private static String captureKey(String langKey, Operation<String> original, @Share("key") LocalRef<String> key) {
        key.set(langKey);
        return "";
    }

    @ModifyArg(
        method = { "checkWarpEvent", "spawnMist", "suddenlySpiders" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/EntityPlayer;addChatMessage(Lnet/minecraft/util/IChatComponent;)V"))
    private static IChatComponent replaceComponent(IChatComponent original, @Share("key") LocalRef<String> key) {
        return new ChatComponentTranslation(key.get()).setChatStyle(salisArcana$warpChatStyle);
    }
}
