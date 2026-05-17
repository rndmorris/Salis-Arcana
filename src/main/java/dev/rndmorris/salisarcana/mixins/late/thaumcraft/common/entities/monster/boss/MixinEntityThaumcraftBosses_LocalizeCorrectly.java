package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.entities.monster.boss;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.common.entities.monster.boss.EntityTaintacleGiant;
import thaumcraft.common.entities.monster.boss.EntityThaumcraftBoss;

@Mixin({ EntityTaintacleGiant.class, EntityThaumcraftBoss.class })
public class MixinEntityThaumcraftBosses_LocalizeCorrectly {

    @WrapOperation(
        method = "attackEntityFrom",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/StatCollector;translateToLocal(Ljava/lang/String;)Ljava/lang/String;"))
    public String preventLocalization(String langKey, Operation<String> original) {
        return "";
    }

    @ModifyArg(
        method = "attackEntityFrom",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/EntityPlayer;addChatMessage(Lnet/minecraft/util/IChatComponent;)V"))
    public IChatComponent addTranslationComponent(IChatComponent original) {
        return original.appendSibling(new ChatComponentTranslation("tc.boss.enrage"));
    }
}
