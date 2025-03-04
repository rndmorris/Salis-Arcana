package dev.rndmorris.salisarcana.mixins.late.tiles;

import java.util.Objects;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.common.DisenchantFocusUpgrade;
import dev.rndmorris.salisarcana.lib.IFocalManipulatorWithXP;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileFocalManipulator;
import thaumcraft.common.tiles.TileThaumcraftInventory;

@Mixin(TileFocalManipulator.class)
public abstract class MixinTileFocalManipulator extends TileThaumcraftInventory implements IFocalManipulatorWithXP {

    @Shadow(remap = false)
    public int size;
    @Shadow(remap = false)
    public AspectList aspects;
    @Shadow(remap = false)
    public int upgrade;
    @Unique
    private int salisArcana$storedXP = 0;
    @Unique
    private @Nullable EntityPlayer salisArcana$craftingOriginator = null;

    @WrapOperation(
        method = "updateEntity",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/api/wands/ItemFocusBasic;applyUpgrade(Lnet/minecraft/item/ItemStack;Lthaumcraft/api/wands/FocusUpgradeType;I)Z",
            remap = false))
    public boolean applyUpgradeOverride(ItemFocusBasic focus, ItemStack focusStack, FocusUpgradeType type, int rank,
        Operation<Boolean> original) {
        if (type.equals(DisenchantFocusUpgrade.baseInstance)) {
            short[] appliedUpgrades = focus.getAppliedUpgrades(focusStack);
            DisenchantFocusUpgrade specific = DisenchantFocusUpgrade.createSpecific(appliedUpgrades);
            NBTTagCompound tag = focusStack.getTagCompound();

            if (specific.lastRank <= 1 && tag != null
                && tag.func_150296_c()
                    .size() <= 1) {
                // Remove the whole tag, since there are no more enchantments and no other properties.
                focusStack.setTagCompound(null);
            } else if (specific.lastRank > 0) {
                appliedUpgrades[specific.lastRank - 1] = -1; // Remove last upgrade
                NBTTagList upgradeListNBT = new NBTTagList();

                for (short upgrade : appliedUpgrades) {
                    NBTTagCompound upgradeNBT = new NBTTagCompound();
                    upgradeNBT.setShort("id", upgrade);
                    upgradeListNBT.appendTag(upgradeNBT);
                }

                assert tag != null; // If it was null, there wouldn't be a lastRank.
                tag.setTag("upgrade", upgradeListNBT);
            } else {
                // In case a Focal Manipulator is somehow tricked into running an invalid recipe, don't give XP.
                return false;
            }

            salisArcana$storedXP += specific.getXpPoints();
            if (this.salisArcana$craftingOriginator != null) {
                this.salisArcana$transferXpToPlayer(this.salisArcana$craftingOriginator);
                this.salisArcana$craftingOriginator = null;
            }
            return true;
        }
        return original.call(focus, focusStack, type, rank);
    }

    @Inject(method = "startCraft", at = @At("HEAD"), cancellable = true, remap = false)
    public void startDisenchantment(int id, EntityPlayer p, CallbackInfoReturnable<Boolean> cir) {
        if (id == DisenchantFocusUpgrade.upgradeID) {
            if (!ResearchManager.isResearchComplete(p.getCommandSenderName(), DisenchantFocusUpgrade.RESEARCH_KEY)) {
                cir.setReturnValue(false);
                return;
            }
            // this.size determines whether the table is currently enchanting
            ItemStack focusStack = this.getStackInSlot(0);
            if (this.size <= 0 && focusStack != null && focusStack.getItem() instanceof ItemFocusBasic focus) {
                short[] upgrades = focus.getAppliedUpgrades(focusStack);
                if (upgrades[0] != -1) {
                    var upgrade = DisenchantFocusUpgrade.createSpecific(upgrades);
                    this.aspects = upgrade.getVisPoints();
                    this.size = this.aspects.visSize();
                    this.upgrade = DisenchantFocusUpgrade.upgradeID;

                    this.markDirty();
                    this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                    this.worldObj
                        .playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "thaumcraft:craftstart", 0.25F, 1.0F);
                    cir.setReturnValue(true);
                    return;
                }
            }
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "writeCustomNBT", at = @At("TAIL"), remap = false)
    public void writeStoredXp(NBTTagCompound nbt, CallbackInfo ci) {
        nbt.setInteger("salisArcana$storedXp", salisArcana$storedXP);
    }

    @Inject(method = "readCustomNBT", at = @At("TAIL"), remap = false)
    public void readStoredXp(NBTTagCompound nbt, CallbackInfo ci) {
        salisArcana$storedXP = nbt.getInteger("salisArcana$storedXp");
    }

    @Override
    public void salisArcana$transferXpToPlayer(EntityPlayer player) {
        if (salisArcana$storedXP > 0) {
            player.addExperience(salisArcana$storedXP);
            salisArcana$storedXP = 0;

            this.markDirty();
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    @Override
    public void salisArcana$setCraftingOriginator(EntityPlayer player) {
        if (!this.worldObj.isRemote) this.salisArcana$craftingOriginator = player;
    }

    @Override
    public void salisArcana$disconnectPlayer(EntityPlayer player) {
        if (Objects.equals(player, this.salisArcana$craftingOriginator)) {
            this.salisArcana$craftingOriginator = null;
        }
    }
}
