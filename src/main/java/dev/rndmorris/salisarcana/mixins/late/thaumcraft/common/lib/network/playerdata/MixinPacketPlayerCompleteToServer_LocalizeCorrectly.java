package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.network.playerdata;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.common.lib.network.playerdata.PacketPlayerCompleteToServer;

@Mixin(PacketPlayerCompleteToServer.class)
public class MixinPacketPlayerCompleteToServer_LocalizeCorrectly {

    @WrapOperation(
        method = "onMessage(Lthaumcraft/common/lib/network/playerdata/PacketPlayerCompleteToServer;Lcpw/mods/fml/common/network/simpleimpl/MessageContext;)Lcpw/mods/fml/common/network/simpleimpl/IMessage;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/StatCollector;translateToLocal(Ljava/lang/String;)Ljava/lang/String;"))
    public String doNotTranslateEarly(String translationKey, Operation<String> original) {
        return translationKey;
    }
}
