package dev.rndmorris.salisarcana.mixins.late.lib;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import dev.rndmorris.salisarcana.api.NbtUtilities;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

@Mixin(value = ThaumcraftCraftingManager.class, remap = false)
public abstract class MixinThaumicCraftingManager_SuppressAspectsTag {

    @WrapMethod(method = "getObjectTags")
    private static AspectList onGetObjectTags(ItemStack itemStack, Operation<AspectList> original) {
        if (NbtUtilities.hasSuppressAspectsTag(itemStack)) {
            return new AspectList();
        }
        return original.call(itemStack);
    }

    @WrapMethod(method = "getBonusTags")
    private static AspectList onGetBonusTags(ItemStack itemStack, AspectList sourceTags,
        Operation<AspectList> original) {
        if (NbtUtilities.hasSuppressAspectsTag(itemStack)) {
            return new AspectList();
        }
        return original.call(itemStack, sourceTags);
    }
}
