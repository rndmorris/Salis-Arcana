package dev.rndmorris.salisarcana.common.compat;

import com.gtnewhorizons.tcwands.api.TCWandAPI;
import com.gtnewhorizons.tcwands.api.wrappers.AbstractWandWrapper;
import com.gtnewhorizons.tcwands.api.wrappers.CapWrapper;
import com.gtnewhorizons.tcwands.api.wrappers.SceptreWrapper;
import com.gtnewhorizons.tcwands.api.wrappers.StaffSceptreWrapper;
import com.gtnewhorizons.tcwands.api.wrappers.StaffWrapper;
import com.gtnewhorizons.tcwands.api.wrappers.WandWrapper;

import dev.rndmorris.salisarcana.lib.WandType;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;

public class GTNHTCWandsCompat {

    public static AbstractWandWrapper getWandWrapper(WandRod rod, WandType type) {
        for (AbstractWandWrapper wrapper : TCWandAPI.getWandWrappers()) {
            if (!wrapper.getRodName()
                .equals(rod.getTag())) continue;

            switch (type) {
                case WAND -> {
                    if (wrapper instanceof WandWrapper) return wrapper;
                }
                case SCEPTER -> {
                    if (wrapper instanceof SceptreWrapper) return wrapper;
                }
                case STAFF -> {
                    if (wrapper instanceof StaffWrapper) return wrapper;
                }
                case STAFFTER -> {
                    if (wrapper instanceof StaffSceptreWrapper) return wrapper;
                }
            }
        }
        return null;
    }

    public static CapWrapper getCapWrapper(WandCap cap) {
        for (CapWrapper wrapper : TCWandAPI.getCaps()) {
            if (wrapper.getName()
                .equals(cap.getTag())) return wrapper;
        }
        return null;
    }
}
