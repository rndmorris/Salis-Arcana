package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.common.blocks.BlockMirrorItem;

@Mixin(BlockMirrorItem.class)
public class MixinBlockMirrorItem_LocalizableText {

    @Unique
    private static final ChatStyle salisArcana$errorStyle = new ChatStyle().setColor(EnumChatFormatting.DARK_PURPLE)
        .setItalic(true);

    @WrapOperation(
        method = "onItemUseFirst",
        at = @At(
            value = "NEW",
            target = "(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/util/ChatComponentTranslation;"))
    public ChatComponentTranslation alterComponent(String j, Object[] objects,
        Operation<ChatComponentTranslation> original) {
        final var component = original.call("salisarcana:misc.mirror.already_linked", objects);
        component.setChatStyle(salisArcana$errorStyle);
        return component;
    }
}
