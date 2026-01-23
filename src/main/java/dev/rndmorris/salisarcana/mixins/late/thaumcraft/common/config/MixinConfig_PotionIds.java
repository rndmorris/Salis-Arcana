package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.config;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.util.Arrays;

import net.minecraft.potion.Potion;
import net.minecraft.util.ChatComponentTranslation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.ObfuscationInfo;
import dev.rndmorris.salisarcana.lib.R;
import dev.rndmorris.salisarcana.lib.pojo.PotionInfo;
import dev.rndmorris.salisarcana.notifications.StartupNotifications;
import thaumcraft.common.config.Config;

@Mixin(value = Config.class, remap = false)
public abstract class MixinConfig_PotionIds {

    @Inject(
        method = "initPotions",
        at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V", ordinal = 0))
    private static void initPotions(CallbackInfo ci, @Local(name = "potionOffset") LocalIntRef potionOffsetRef,
        @Share("potionIndex") LocalIntRef potionIndexRef, @Share("potionInfo") LocalRef<PotionInfo[]> potionInfoRef) {

        LOG.info("Overriding Thaumcraft's potion id assignement.");

        // do not change the order of this array, it determines which setting gets used for which potion effect
        final var potionIdInfo = new PotionInfo[] { PotionInfo.taintPoison(), PotionInfo.fluxFlu(),
            PotionInfo.fluxPhage(), PotionInfo.unnaturalHunger(), PotionInfo.warpWard(), PotionInfo.deadlyGaze(),
            PotionInfo.blurredVision(), PotionInfo.sunScorned(), PotionInfo.thaumarhia() };

        potionInfoRef.set(potionIdInfo);

        final var maxId = Arrays.stream(potionIdInfo)
            .mapToInt(PotionInfo::id)
            .max()
            .getAsInt();
        final var arrayLengthLimit = SalisConfig.thaum.potionIdLimitRaised.isEnabled() ? Integer.MAX_VALUE
            : Byte.MAX_VALUE;
        final var minRequiredSize = 32 + potionIdInfo.length; // 32 is the vanilla length

        final var expandTo = 1 + Math.min(
            // we may need IDs above the max
            Math.max(maxId, minRequiredSize),
            // but we don't want to go over our safe limit
            arrayLengthLimit);
        if (!sa$expandPotionArray(expandTo)) {
            potionIndexRef.set(-1);
            return;
        }

        potionIndexRef.set(0);
        potionOffsetRef.set(Integer.MAX_VALUE); // this skips Thaum's array expansion, since we've already done our own
    }

    @WrapOperation(
        method = "initPotions",
        at = @At(value = "INVOKE", target = "Lthaumcraft/common/config/Config;getNextPotionId(I)I"))
    private static int wrapPotionId(int _start, Operation<Integer> original,
        @Share("potionIndex") LocalIntRef potionIndexRef, @Share("lastAutoId") LocalIntRef lastAutoIdRef,
        @Share("potionInfo") LocalRef<PotionInfo[]> potionInfoRef) {
        final var potionIndex = potionIndexRef.get();
        if (potionIndex == -1) {
            return -1;
        }
        potionIndexRef.set(potionIndex + 1);
        final var potionInfo = potionInfoRef.get()[potionIndex];
        final var setting = potionInfo.setting();

        final var potionId = setting.getValue();
        if (setting.isEnabled()) {
            // we have an id to assign
            final var isInLimit = potionId
                < (SalisConfig.thaum.potionIdLimitRaised.isEnabled() ? Integer.MAX_VALUE : Byte.MAX_VALUE);
            if (isInLimit) {
                // and its within our safe limits
                if (Potion.potionTypes[potionId] == null) {
                    // and not already claimed, so return it!
                    return potionId;
                }
                // but it's already claimed, so we log an error and look up the next available id
                sa$logPotionIdClaimed(potionInfo);
            } else {
                // but it's above the safe limit
                sa$logPotionIdAboveSafeLimit(potionInfo);
            }
        }

        // either we don't have an override set, or something was wrong with the overridden id
        final var autoId = sa$findNextOpenId(lastAutoIdRef, potionInfo.loggingName());
        if (autoId == -1) {
            // we can no longer safely expand the array, abort the rest of the process
            potionIndexRef.set(-1);
            return -1;
        }
        return autoId;
    }

    @Unique
    private static int sa$findNextOpenId(LocalIntRef lastAutoRef, String potionName) {
        final int startIndex = 32;

        for (var index = Math.max(lastAutoRef.get() + 1, startIndex); index < Potion.potionTypes.length; ++index) {
            if (Potion.potionTypes[index] == null) {
                lastAutoRef.set(index);
                LOG.info("Found unassigned potion id {}. Assigning it to {}.", index, potionName);
                return index;
            }
        }

        LOG.info("Hit the potion array limit at {}, requesting +1 expansion.", Potion.potionTypes.length);
        final var nextId = Potion.potionTypes.length;
        if (sa$expandPotionArray(nextId + 1)) {
            lastAutoRef.set(nextId);
            return nextId;
        }
        sa$logArrayTooLong();
        return -1;
    }

    @Unique
    private static boolean sa$expandPotionArray(int increaseTo) {
        final var oldArray = Potion.potionTypes;
        if (oldArray.length >= increaseTo) {
            LOG.info(
                "Requested increasing the potion array to {} but the array is already of length {}.",
                increaseTo,
                oldArray.length);
            return true;
        }
        final var limitRaised = SalisConfig.thaum.potionIdLimitRaised.isEnabled();
        if (!limitRaised && increaseTo > Byte.MAX_VALUE) {
            return false;
        }

        LOG.info("Expanding potion array to {}", increaseTo);
        final var newArray = new Potion[increaseTo];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        R.of(Potion.class)
            .set(ObfuscationInfo.POTION_TYPES.getName(), newArray);
        return true;
    }

    @Unique
    private static void sa$logArrayTooLong() {
        LOG.error("No unassigned potion ids could be found within the safe limit.");
        StartupNotifications.queueError(new ChatComponentTranslation("salisaracan:error.potion_array_too_long"));
    }

    @Unique
    private static void sa$logPotionIdClaimed(PotionInfo potionInfo) {
        LOG.error(
            "{} could not be given id {} as configured because that id has been assigned to a different potion effect.",
            potionInfo.loggingName(),
            potionInfo.id());
        StartupNotifications.queueError(
            new ChatComponentTranslation(
                "salisarcana:error.potion_id_claimed",
                new ChatComponentTranslation(potionInfo.langKey()),
                potionInfo.id()));
    }

    @Unique
    private static void sa$logPotionIdAboveSafeLimit(PotionInfo potionInfo) {
        LOG.error(
            "{} was assigned id {}, which is above the safe limit of 127. If the limit has been raised through another mod, please set _uncapped_potion_ids to `true` in the Salis Arcana feature configs.",
            potionInfo.loggingName(),
            potionInfo.id());
        StartupNotifications.queueError(
            new ChatComponentTranslation(
                "salisarcana:error.potion_id_limit",
                new ChatComponentTranslation(potionInfo.langKey()),
                potionInfo.id()));
    }
}
