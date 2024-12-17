package dev.rndmorris.tfixins.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class TileVisNodeRestoreData implements IReadFromNBT, IWriteToNBT {

    private static final String TAG_PARENT = "parent";
    private static final String TAG_CHILDREN = "children";
    private static final String TAG_DX = "dX";
    private static final String TAG_DY = "dY";
    private static final String TAG_DZ = "dZ";

    public byte[] parentOffsets;
    public final List<byte[]> childrenOffsets = new ArrayList<>();

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey(TAG_PARENT, Constants.NBT.TAG_COMPOUND)) {
            final var parentTag = nbttagcompound.getCompoundTag(TAG_PARENT);
            parentOffsets = readOffsets(parentTag);
        }
        if (nbttagcompound.hasKey(TAG_CHILDREN, Constants.NBT.TAG_LIST)) {
            childrenOffsets.clear();
            final var childrenTag = nbttagcompound.getTagList(TAG_CHILDREN, Constants.NBT.TAG_COMPOUND);
            for (var index = 0; index < childrenTag.tagCount(); ++index) {
                final var childTag = childrenTag.getCompoundTagAt(index);
                final var childOffsets = readOffsets(childTag);
                if (childOffsets != null) {
                    childrenOffsets.add(childOffsets);
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        if (parentOffsets != null) {
            NBTTagCompound parent;
            nbttagcompound.setTag(TAG_PARENT, parent = nbttagcompound.getCompoundTag(TAG_PARENT));
            writeOffsets(parent, parentOffsets);
        }

        NBTTagList children;
        nbttagcompound
            .setTag(TAG_CHILDREN, children = nbttagcompound.getTagList(TAG_CHILDREN, Constants.NBT.TAG_COMPOUND));
        for (var child : childrenOffsets) {
            final var childTag = new NBTTagCompound();
            children.appendTag(childTag);
            writeOffsets(childTag, child);
        }
    }

    private void writeOffsets(NBTTagCompound tag, byte[] offsets) {
        tag.setByte(TAG_DX, offsets[0]);
        tag.setByte(TAG_DY, offsets[1]);
        tag.setByte(TAG_DZ, offsets[2]);
    }

    private byte[] readOffsets(NBTTagCompound tag) {
        if (!tag.hasKey(TAG_DX, Constants.NBT.TAG_BYTE) || !tag.hasKey(TAG_DY, Constants.NBT.TAG_BYTE)
            || !tag.hasKey(TAG_DZ, Constants.NBT.TAG_BYTE)) {
            return null;
        }
        return new byte[] { tag.getByte(TAG_DX), tag.getByte(TAG_DY), tag.getByte(TAG_DZ), };
    }
}
