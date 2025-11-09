// Code here adapted from
// https://github.com/GTNewHorizons/Hodgepodge/blob/master/src/main/java/com/mitchej123/hodgepodge/mixins/late/thaumcraft/MixinTileWandPedestal.java
// and therefore under the LGPL-3.0 license.

package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import java.util.ArrayList;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.api.IVisContainer;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.visnet.TileVisNode;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.tiles.TileWandPedestal;

@Mixin(TileWandPedestal.class)
public abstract class MixinTileWandPedestal_WandPedestalCv extends TileThaumcraft implements ISidedInventory {

    @Unique
    private int sa$ticksSinceLastSync = 0;
    @Unique
    private boolean sa$needSync;

    @Shadow(remap = false)
    ArrayList<ChunkCoordinates> nodes = null;

    @Shadow(remap = false)
    public boolean draining;

    @Shadow(remap = false)
    boolean somethingChanged;

    @Inject(
        method = "updateEntity",
        at = @At(
            remap = false,
            value = "FIELD",
            target = "Lthaumcraft/common/tiles/TileWandPedestal;draining:Z",
            opcode = Opcodes.PUTFIELD,
            ordinal = 0,
            shift = At.Shift.AFTER,
            by = 1),
        require = 1)
    private void sa$rechargeWandViaCV(CallbackInfo ci, @Local ItemWandCasting wand) {
        this.sa$rechargeItem((IVisContainer) wand);
    }

    @Inject(
        method = "updateEntity",
        at = @At(
            remap = false,
            value = "FIELD",
            target = "Lthaumcraft/common/tiles/TileWandPedestal;draining:Z",
            opcode = Opcodes.PUTFIELD,
            ordinal = 3,
            shift = At.Shift.AFTER,
            by = 1),
        require = 1)
    private void sa$rechargeAmuletViaCV(CallbackInfo ci, @Local ItemAmuletVis amulet) {
        this.sa$rechargeItem((IVisContainer) amulet);
    }

    @Inject(method = "updateEntity", at = @At("TAIL"))
    private void sa$onTickEnd(CallbackInfo ci) {
        if (!worldObj.isRemote && sa$needSync && sa$ticksSinceLastSync++ > 5) {
            sa$ticksSinceLastSync = 0;
            sa$needSync = false;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @SuppressWarnings("LocalCaptureFailException")
    @Inject(
        at = @At(
            args = "classValue=thaumcraft/api/nodes/INode",
            target = "Lnet/minecraft/world/World;getTileEntity(III)Lnet/minecraft/tileentity/TileEntity;",
            value = "CONSTANT"),
        locals = LocalCapture.CAPTURE_FAILEXCEPTION,
        method = "findNodes()V",
        remap = false,
        require = 1)
    private void sa$addCVNodes(CallbackInfo ci, int xx, int yy, int zz, TileEntity te) {
        if (te instanceof TileVisNode) {
            this.nodes.add(new ChunkCoordinates(te.xCoord, te.yCoord, te.zCoord));
        }
    }

    @Unique
    private void sa$rechargeItem(IVisContainer container) {
        ItemStack stack = getStackInSlot(0);

        AspectList as = container.getAspectsWithRoom(stack);
        if (as != null && as.size() > 0) {
            for (Aspect aspect : as.getAspects()) {
                // Pedestal operates every 5 ticks
                int drained = VisNetHandler.drainVis(this.worldObj, this.xCoord, this.yCoord, this.zCoord, aspect, 25);
                if (drained > 0) {
                    container.addRealVis(stack, aspect, drained, true);
                    draining = true;
                    somethingChanged = true;
                    sa$needSync = true;
                }
            }
        }
    }
}
