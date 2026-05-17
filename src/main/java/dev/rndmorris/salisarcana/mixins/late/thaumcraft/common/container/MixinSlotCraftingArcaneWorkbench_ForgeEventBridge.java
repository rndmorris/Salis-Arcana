package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import dev.rndmorris.salisarcana.lib.MundaneCraftingBridge;
import dev.rndmorris.salisarcana.lib.ifaces.ICachedMagicWorkbench;
import thaumcraft.common.container.SlotCraftingArcaneWorkbench;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.tiles.TileMagicWorkbench;

@Mixin(SlotCraftingArcaneWorkbench.class)
public class MixinSlotCraftingArcaneWorkbench_ForgeEventBridge {

    @Shadow(remap = false)
    private EntityPlayer thePlayer;

    @ModifyArg(
        method = "onPickupFromSlot",
        at = @At(
            value = "INVOKE",
            target = "Lcpw/mods/fml/common/FMLCommonHandler;firePlayerCraftingEvent(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;Lnet/minecraft/inventory/IInventory;)V",
            remap = false),
        index = 2)
    public IInventory wrapEventInventory(IInventory craftMatrix) {
        if (craftMatrix instanceof ICachedMagicWorkbench cache) {
            if (cache.salisArcana$getMundaneRecipe() != null) {
                return new MundaneCraftingBridge((TileMagicWorkbench) craftMatrix);
            }
        } else if (craftMatrix.getStackInSlot(10) == null
            || ThaumcraftCraftingManager.findMatchingArcaneRecipeAspects(craftMatrix, this.thePlayer)
                .size() == 0) {
                    return new MundaneCraftingBridge((TileMagicWorkbench) craftMatrix);
                }

        return craftMatrix;
    }
}
