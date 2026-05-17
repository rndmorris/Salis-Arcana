package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items;

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

import thaumcraft.common.items.ItemKey;

@Mixin(ItemKey.class)
public class MixinItemKey_LocalizeCorrectly {

    @Unique
    private static final ChatStyle salisArcana$wardedDoorStyle = new ChatStyle()
        .setColor(EnumChatFormatting.DARK_PURPLE)
        .setItalic(true);

    @WrapOperation(
        method = "onItemUseFirst",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/StatCollector;translateToLocal(Ljava/lang/String;)Ljava/lang/String;"))
    public String collectKeys(String langKey, Operation<String> original, @Share("key1") LocalRef<String> keyOne,
        @Share("key2") LocalRef<String> keyTwo) {

        // Two keys are required because some messages are made by concatenating two keys together.
        if (keyOne.get() == null) {
            keyOne.set(langKey);
        } else {
            keyTwo.set(langKey);
        }

        return "";
    }

    @ModifyArg(
        method = "onItemUseFirst",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/EntityPlayer;addChatMessage(Lnet/minecraft/util/IChatComponent;)V"))
    public IChatComponent replaceComponent(IChatComponent original, @Share("key1") LocalRef<String> keyOne,
        @Share("key2") LocalRef<String> keyTwo) {
        ChatComponentTranslation output = new ChatComponentTranslation(keyOne.get());
        output.setChatStyle(salisArcana$wardedDoorStyle);

        if (keyTwo.get() != null) {
            output.appendSibling(new ChatComponentTranslation(keyTwo.get()));
        }

        return output;
    }
}
