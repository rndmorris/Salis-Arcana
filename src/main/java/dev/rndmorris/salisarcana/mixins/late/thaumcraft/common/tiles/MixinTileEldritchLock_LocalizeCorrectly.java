package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import thaumcraft.common.tiles.TileEldritchLock;

@Mixin(TileEldritchLock.class)
public class MixinTileEldritchLock_LocalizeCorrectly {

    @WrapOperation(
        method = { "spawnWardenBossRoom", "spawnGolemBossRoom", "spawnCultistBossRoom", "spawnTaintBossRoom" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/StatCollector;translateToLocal(Ljava/lang/String;)Ljava/lang/String;"))
    public String captureKey(String langKey, Operation<String> original, @Share("key") LocalRef<String> key) {
        key.set(langKey);
        return "";
    }

    @ModifyArg(
        method = { "spawnWardenBossRoom", "spawnGolemBossRoom", "spawnCultistBossRoom", "spawnTaintBossRoom" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/EntityPlayer;addChatMessage(Lnet/minecraft/util/IChatComponent;)V"))
    public IChatComponent replaceComponent(IChatComponent original, @Share("key") LocalRef<String> key) {
        return new ChatComponentTranslation(key.get());
    }
}
