package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import dev.rndmorris.salisarcana.lib.PlayerHelper;
import dev.rndmorris.salisarcana.lib.ifaces.IFocalManipulatorWithXP;
import thaumcraft.common.tiles.TileFocalManipulator;

@Mixin(TileFocalManipulator.class)
public abstract class MixinTileFocalManipulator_CancelReturnXP implements IFocalManipulatorWithXP {

    @Shadow(remap = false)
    public int size;
    @Unique
    private int salisArcana$craftingXP = 0;

    @WrapMethod(method = "writeCustomNBT", remap = false)
    public void writeCraftingXp(NBTTagCompound nbt, Operation<Void> original) {
        original.call(nbt);
        if (this.size > 0) {
            nbt.setInteger("salisArcana$craftingXP", salisArcana$craftingXP);
        }
    }

    @WrapMethod(method = "readCustomNBT", remap = false)
    public void readCraftingXp(NBTTagCompound nbt, Operation<Void> original) {
        original.call(nbt);
        salisArcana$craftingXP = this.size > 0 ? nbt.getInteger("salisArcana$craftingXP") : 0;
    }

    @WrapMethod(method = "startCraft", remap = false)
    public boolean captureXP(int id, EntityPlayer p, Operation<Boolean> original) {
        int xp = PlayerHelper.getExperienceTotal(p);
        boolean success = original.call(id, p);
        if (success) {
            // Block has already been marked for update
            salisArcana$craftingXP = xp - PlayerHelper.getExperienceTotal(p);
        }
        return success;
    }

    @Inject(
        method = "updateEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;playSoundEffect(DDDLjava/lang/String;FF)V",
            ordinal = 0))
    public void returnOnCancel(CallbackInfo ci) {
        this.salisArcana$addXP(this.salisArcana$craftingXP);
        this.salisArcana$craftingXP = 0;
        // Block will be marked for update since a recipe has been cancelled.
    }
}
