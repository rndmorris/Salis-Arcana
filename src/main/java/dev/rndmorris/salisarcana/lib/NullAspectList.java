package dev.rndmorris.salisarcana.lib;

import net.minecraft.nbt.NBTTagCompound;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public final class NullAspectList extends AspectList {

    public static final NullAspectList INSTANCE = new NullAspectList();
    private static final Aspect[] NO_ASPECTS = new Aspect[0];

    private NullAspectList() {}

    @Override
    public int size() {
        return 0;
    }

    @Override
    public int visSize() {
        return 0;
    }

    @Override
    public Aspect[] getAspects() {
        return NO_ASPECTS;
    }

    @Override
    public Aspect[] getPrimalAspects() {
        return NO_ASPECTS;
    }

    @Override
    public Aspect[] getAspectsSorted() {
        return NO_ASPECTS;
    }

    @Override
    public Aspect[] getAspectsSortedAmount() {
        return NO_ASPECTS;
    }

    @Override
    public int getAmount(Aspect key) {
        return 0;
    }

    @Override
    public boolean reduce(Aspect key, int amount) {
        return false;
    }

    @Override
    public AspectList remove(Aspect key, int amount) {
        return this;
    }

    @Override
    public AspectList remove(Aspect key) {
        return this;
    }

    @Override
    public AspectList add(Aspect aspect, int amount) {
        throw new UnsupportedOperationException("Cannot add aspects to a constant NullAspectList.");
    }

    @Override
    public AspectList merge(Aspect aspect, int amount) {
        throw new UnsupportedOperationException("Cannot add aspects to a constant NullAspectList.");
    }

    @Override
    public AspectList add(AspectList in) {
        throw new UnsupportedOperationException("Cannot add aspects to a constant NullAspectList.");
    }

    @Override
    public AspectList merge(AspectList in) {
        throw new UnsupportedOperationException("Cannot add aspects to a constant NullAspectList.");
    }

    @Override
    public AspectList copy() {
        return this;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        throw new UnsupportedOperationException("Cannot load aspects from NBT to a constant NullAspectList.");
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound, String label) {
        throw new UnsupportedOperationException("Cannot load aspects from NBT to a constant NullAspectList.");
    }
}
