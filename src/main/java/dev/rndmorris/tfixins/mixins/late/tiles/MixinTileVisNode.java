package dev.rndmorris.tfixins.mixins.late.tiles;

import static dev.rndmorris.tfixins.ThaumicFixins.LOG;
import static dev.rndmorris.tfixins.ThaumicFixins.MODID;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.rndmorris.tfixins.data.TileVisNodeRestoreData;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.visnet.TileVisNode;

@Mixin(value = TileVisNode.class, remap = false)
public abstract class MixinTileVisNode extends TileThaumcraft {

    @Shadow
    WeakReference<TileVisNode> parent;

    @Shadow
    ArrayList<WeakReference<TileVisNode>> children;

    @Shadow
    public boolean nodeRefresh;
    @Shadow
    private boolean nodeRegged;
    @Unique
    TileVisNodeRestoreData tf$restoreData = null;

    @Inject(method = "updateEntity", at = @At("HEAD"))
    private void mixinOnUpdate(CallbackInfo ci) {
        if (tf$restoreData != null) {
            tf$restoreFromData();
        }
    }

    @Unique
    private void tf$restoreFromData() {
        if (worldObj == null) {
            return;
        }
        final var restoreData = tf$restoreData;
        if (restoreData.parentOffsets != null) {
            final var parentNode = tf$getNodeCoordsFromOffsets(restoreData.parentOffsets);
            if (parentNode != null) {
                LOG.debug(
                    "TileVisNode ({}/{}/{}) in dim {} loaded parent {}/{}/{}",
                    xCoord,
                    yCoord,
                    zCoord,
                    worldObj.provider.dimensionId,
                    parentNode.xCoord,
                    parentNode.yCoord,
                    parentNode.zCoord);
                this.parent = new WeakReference<>(parentNode);
                tf$ensureThisIsInParentList(parentNode);
            }
        }

        if (children == null) {
            children = new ArrayList<>();
        }
        children.clear();
        for (var childOffset : restoreData.childrenOffsets) {
            final var childNode = tf$getNodeCoordsFromOffsets(childOffset);
            if (childNode == null) {
                continue;
            }
            LOG.debug(
                "TileVisNode ({}/{}/{}) in dim {} loaded child {}/{}/{}",
                xCoord,
                yCoord,
                zCoord,
                worldObj.provider.dimensionId,
                childNode.xCoord,
                childNode.yCoord,
                childNode.zCoord);
            tf$ensureChildIsActuallyChild(childNode);
        }
        this.nodeRefresh = false;
        this.nodeRegged = false;
        tf$restoreData = null;
    }

    @Inject(method = "setParent", at = @At("HEAD"))
    private void mixinSetParent(CallbackInfo ci) {
        markDirty();
    }

    @Inject(method = "getChildren", at = @At("HEAD"))
    private void mixinGetChildren(CallbackInfoReturnable<ArrayList<WeakReference<TileVisNode>>> ci) {
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);

        if (!nbttagcompound.hasKey(MODID, Constants.NBT.TAG_COMPOUND)) {
            return;
        }
        final var patchTag = nbttagcompound.getCompoundTag(MODID);
        tf$restoreData = new TileVisNodeRestoreData();
        tf$restoreData.readFromNBT(patchTag);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);

        final var restoreData = new TileVisNodeRestoreData();

        NBTTagCompound patchTag;
        nbttagcompound.setTag(MODID, patchTag = nbttagcompound.getCompoundTag(MODID));

        TileVisNode parent = this.parent != null ? this.parent.get() : null;
        if (parent != null) {
            restoreData.parentOffsets = tf$getOffsetsToNode(parent);
        }

        if (this.children != null) {
            for (var childRef : children) {
                final var child = childRef.get();
                if (child == null) {
                    continue;
                }
                final var childOffsets = tf$getOffsetsToNode(child);
                restoreData.childrenOffsets.add(childOffsets);
            }
        }

        restoreData.writeToNBT(patchTag);
    }

    @Unique
    private byte[] tf$getOffsetsToNode(@Nonnull TileVisNode toNode) {
        return new byte[] { (byte) (toNode.xCoord - xCoord), (byte) (toNode.yCoord - yCoord),
            (byte) (toNode.zCoord - zCoord), };
    }

    @Unique
    private @Nullable TileVisNode tf$getNodeCoordsFromOffsets(@Nonnull byte[] offsets) {
        final int x = xCoord + offsets[0], y = yCoord + offsets[1], z = zCoord + offsets[2];
        if (worldObj.getTileEntity(x, y, z) instanceof TileVisNode node) {
            return node;
        }
        return null;
    }

    @Unique
    private void tf$ensureThisIsInParentList(@Nonnull TileVisNode parentNode) {
        final var parentChildren = parentNode.getChildren();
        for (var childRef : parentChildren) {
            final var child = childRef.get();
            if (child == null) {
                continue;
            }
            if (tf$coordsMatchThis(child)) {
                return;
            }
        }
        // parent doesn't contain this as a child, add it
        final var thisNode = (TileVisNode) ((Object) this);
        parentChildren.add(new WeakReference<>(thisNode));
    }

    @Unique
    private void tf$ensureChildIsActuallyChild(@Nonnull TileVisNode childNode) {
        final var parentRef = childNode.getParent();
        TileVisNode parent;

        if (parentRef == null || (parent = parentRef.get()) == null || !tf$coordsMatchThis(parent)) {
            return;
        }
        LOG.info(
            "{}/{}/{} loaded child {}/{}/{}",
            xCoord,
            yCoord,
            zCoord,
            childNode.xCoord,
            childNode.yCoord,
            childNode.zCoord);
        final var thisNode = (TileVisNode) ((Object) this);
        childNode.setParent(new WeakReference<>(thisNode));
        children.add(new WeakReference<>(childNode));
    }

    @Unique
    private boolean tf$coordsMatchThis(TileVisNode node) {
        return xCoord == node.xCoord && yCoord == node.yCoord && zCoord == node.zCoord;
    }
}
