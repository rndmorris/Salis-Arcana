package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.common.DisenchantFocusUpgrade;
import dev.rndmorris.salisarcana.lib.ifaces.IFocalManipulatorWithXP;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileFocalManipulator;
import thaumcraft.common.tiles.TileThaumcraftInventory;

@Mixin(TileFocalManipulator.class)
public abstract class MixinTileFocalManipulator_FocusDisenchanting extends TileThaumcraftInventory
    implements IFocalManipulatorWithXP {

    @Shadow(remap = false)
    public int size;
    @Shadow(remap = false)
    public AspectList aspects;
    @Shadow(remap = false)
    public int upgrade;

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

                // noinspection DataFlowIssue
                tag.setTag("upgrade", upgradeListNBT);
            } else {
                // In case a Focal Manipulator is somehow tricked into running an invalid recipe, don't give XP.
                return false;
            }

            this.salisArcana$addXP(specific.getXpPoints());
            return true;
        }
        return original.call(focus, focusStack, type, rank);
    }

    @WrapMethod(method = "startCraft", remap = false)
    public boolean startDisenchantment(int id, EntityPlayer p, Operation<Boolean> original) {
        if (id == DisenchantFocusUpgrade.upgradeID) {
            if (!ResearchManager.isResearchComplete(p.getCommandSenderName(), DisenchantFocusUpgrade.RESEARCH_KEY)) {
                return false;
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

                    this.salisArcana$prioritizePlayer(p);
                    return true;
                }
            }
            return false;
        }
        return original.call(id, p);
    }
}
