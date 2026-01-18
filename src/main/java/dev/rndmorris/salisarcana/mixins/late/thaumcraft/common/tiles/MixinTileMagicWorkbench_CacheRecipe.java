package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.rndmorris.salisarcana.lib.CraftingHelper;
import dev.rndmorris.salisarcana.lib.KnowItAll;
import dev.rndmorris.salisarcana.lib.MundaneCraftingBridge;
import dev.rndmorris.salisarcana.lib.ifaces.ICachedMagicWorkbench;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.common.tiles.TileMagicWorkbench;

@Mixin(TileMagicWorkbench.class)
public abstract class MixinTileMagicWorkbench_CacheRecipe extends TileThaumcraft
    implements IInventory, ICachedMagicWorkbench {

    @Shadow(remap = false)
    public ItemStack[] stackList;

    @Unique
    private IArcaneRecipe salisArcana$arcaneRecipe;

    @Unique
    private IRecipe salisArcana$mundaneRecipe;

    @Unique
    private boolean salisArcana$recipeCacheReady = false;

    @Override
    public final IRecipe salisArcana$getMundaneRecipe() {
        if (!this.salisArcana$recipeCacheReady) {
            this.salisArcana$calculateRecipeCache();
        }

        return this.salisArcana$mundaneRecipe;
    }

    @Override
    public final IArcaneRecipe salisArcana$getArcaneRecipe() {
        if (!this.salisArcana$recipeCacheReady) {
            this.salisArcana$calculateRecipeCache();
        }

        return this.salisArcana$arcaneRecipe;
    }

    @Inject(method = "setInventorySlotContents", at = @At("HEAD"))
    private void resetCache1(int par1, ItemStack par2ItemStack, CallbackInfo ci) {
        this.salisArcana$recipeCacheReady = false;
    }

    @Inject(method = "getStackInSlotOnClosing", at = @At("HEAD"))
    private void resetCache2(int par1, CallbackInfoReturnable<ItemStack> cir) {
        if (this.stackList[par1] != null) {
            this.salisArcana$recipeCacheReady = false;
        }
    }

    @Inject(method = "decrStackSize", at = @At("HEAD"))
    private void resetCache3(int par1, int par2, CallbackInfoReturnable<ItemStack> cir) {
        if (this.stackList[par1] != null) {
            this.salisArcana$recipeCacheReady = false;
        }
    }

    @Inject(method = "readCustomNBT", at = @At("HEAD"), remap = false)
    private void resetCache4(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        this.salisArcana$recipeCacheReady = false;
    }

    @Unique
    private void salisArcana$calculateRecipeCache() {
        final var bridge = new MundaneCraftingBridge((TileMagicWorkbench) (Object) this);
        final var knowItAll = KnowItAll.getInstance();

        if (this.salisArcana$mundaneRecipe != null) {
            if (this.salisArcana$mundaneRecipe.matches(bridge, this.worldObj)) {
                // The new mundane recipe is the same as the previous recipe
                this.salisArcana$recipeCacheReady = true;
                return;
            }
        } else if (this.salisArcana$arcaneRecipe != null) {
            if (this.salisArcana$arcaneRecipe.matches(this, knowItAll.worldObj, knowItAll)) {
                // The new arcane recipe is the same as the previous recipe
                this.salisArcana$recipeCacheReady = true;
                return;
            }
        }

        this.salisArcana$mundaneRecipe = CraftingHelper.INSTANCE.findMundaneRecipe(bridge, this.worldObj);

        if (this.salisArcana$mundaneRecipe == null) {
            this.salisArcana$arcaneRecipe = CraftingHelper.INSTANCE.findArcaneRecipe(this, knowItAll);
        } else {
            this.salisArcana$arcaneRecipe = null;
        }

        this.salisArcana$recipeCacheReady = true;
    }
}
