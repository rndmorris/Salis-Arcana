package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import thaumcraft.common.blocks.BlockMetalDevice;

@Mixin(BlockMetalDevice.class)
public class MixinBlockMetalDevice_LocalizeCorrectly {

    @Unique
    private static final ChatStyle salisArcana$alembicStyle = new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA);

    @WrapOperation(
        method = "onBlockActivated",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/StatCollector;translateToLocal(Ljava/lang/String;)Ljava/lang/String;"))
    public String captureKey(String langKey, Operation<String> original, @Share("key") LocalRef<String> key) {
        key.set(langKey);
        return "";
    }

    @WrapOperation(
        method = "onBlockActivated",
        at = @At(
            value = "NEW",
            target = "(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/util/ChatComponentTranslation;"))
    public ChatComponentTranslation modifyChatComponent(String langKey, Object[] params,
        Operation<ChatComponentTranslation> original, @Share("key") LocalRef<String> key) {
        final ChatComponentTranslation out = original.call(key.get(), params);
        out.setChatStyle(salisArcana$alembicStyle);
        return out;
    }
}
