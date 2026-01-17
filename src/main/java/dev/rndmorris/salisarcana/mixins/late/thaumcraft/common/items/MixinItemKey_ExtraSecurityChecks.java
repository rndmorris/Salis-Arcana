package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.common.items.ItemKey;
import thaumcraft.common.tiles.TileOwned;

@Mixin(ItemKey.class)
public class MixinItemKey_ExtraSecurityChecks {

    @ModifyExpressionValue(
        method = "onItemUseFirst",
        at = @At(value = "NEW", target = "(Lnet/minecraft/item/Item;II)Lnet/minecraft/item/ItemStack;"))
    public ItemStack addExtraSecurityInfo(ItemStack key, @Local(argsOnly = true) EntityPlayer player,
        @Local(argsOnly = true) World world) {
        final var nbt = new NBTTagCompound();
        nbt.setString("salisarcana:creator", player.getCommandSenderName());
        nbt.setInteger("salisarcana:dimension", world.provider.dimensionId);
        key.setTagCompound(nbt);
        return key;
    }

    @WrapOperation(
        method = "onItemUseFirst",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/nbt/NBTTagCompound;getString(Ljava/lang/String;)Ljava/lang/String;"))
    public String checkExtraSecurityInfo(NBTTagCompound tag, String nbtKey, Operation<String> original,
        @Local TileEntity tileEntity, @Local(argsOnly = true) World world, @Local(argsOnly = true) ItemStack key) {
        final var creator = tag.getString("salisarcana:creator");

        if (!creator.isEmpty()) {
            // This key has the extra security features.
            if (tag.getInteger("salisarcana:dimension") != world.provider.dimensionId) {
                // The key is for the wrong dimension.
                return null;
            }

            final var tile = (TileOwned) tileEntity;
            if (!(creator.equals(tile.owner)
                || (key.getItemDamage() == 0 && tile.accessList.contains("1" + creator)))) {
                // The creator of the key does not have permission to create keys for this door.
                return null;
            }
        }

        // Either this is a legacy key or the security checks passed.
        return original.call(tag, nbtKey);
    }

    @Inject(method = "addInformation", at = @At("TAIL"))
    public void addExtraInfo(ItemStack key, EntityPlayer player, List<String> text, boolean advancedTooltips,
        CallbackInfo ci) {
        if (key.hasTagCompound() && key.stackTagCompound.hasKey("salisarcana:creator")) {
            final int lastLine = text.size() - 1;
            final String dimInfo = StatCollector.translateToLocalFormatted(
                "salisarcana:misc.arcane_key.dimension",
                key.stackTagCompound.getInteger("salisarcana:dimension"));

            text.set(lastLine, text.get(lastLine) + dimInfo);
            text.add(
                StatCollector.translateToLocalFormatted(
                    "salisarcana:misc.arcane_key.creator",
                    key.stackTagCompound.getString("salisarcana:creator")));
        }
    }
}
