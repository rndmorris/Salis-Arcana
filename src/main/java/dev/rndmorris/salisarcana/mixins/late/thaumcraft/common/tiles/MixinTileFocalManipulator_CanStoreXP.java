package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import dev.rndmorris.salisarcana.lib.ifaces.IFocalManipulatorWithXP;
import thaumcraft.common.tiles.TileFocalManipulator;
import thaumcraft.common.tiles.TileThaumcraftInventory;

@Mixin(TileFocalManipulator.class)
public class MixinTileFocalManipulator_CanStoreXP extends TileThaumcraftInventory implements IFocalManipulatorWithXP {

    @Unique
    private int salisArcana$storedXP = 0;
    @Unique
    private final ArrayList<EntityPlayer> salisArcana$playersConnected = new ArrayList<>(3);

    @WrapMethod(method = "writeCustomNBT", remap = false)
    public void writeStoredXp(NBTTagCompound nbt, Operation<Void> original) {
        original.call(nbt);
        nbt.setInteger("salisArcana$storedXp", salisArcana$storedXP);
    }

    @WrapMethod(method = "readCustomNBT", remap = false)
    public void readStoredXp(NBTTagCompound nbt, Operation<Void> original) {
        original.call(nbt);
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
    public void salisArcana$connectPlayer(EntityPlayer player) {
        if (!this.salisArcana$playersConnected.contains(player)) {
            this.salisArcana$playersConnected.add(player);
        }
    }

    @Override
    public void salisArcana$disconnectPlayer(EntityPlayer player) {
        this.salisArcana$playersConnected.remove(player);
    }

    @Override
    public void salisArcana$addXP(int xp) {
        salisArcana$storedXP += xp;
    }

    @Override
    public void salisArcana$prioritizePlayer(EntityPlayer player) {
        this.salisArcana$playersConnected.remove(player);
        this.salisArcana$playersConnected.add(0, player);
    }
}
