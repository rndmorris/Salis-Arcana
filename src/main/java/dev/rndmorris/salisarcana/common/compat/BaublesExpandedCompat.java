package dev.rndmorris.salisarcana.common.compat;

import baubles.api.expanded.BaubleExpandedSlots;

public class BaublesExpandedCompat {
    public static final String POUCH_SLOT = "focus_pouch";

    public static void initialize() {
        BaubleExpandedSlots.tryRegisterType(POUCH_SLOT);
        BaubleExpandedSlots.tryAssignSlotOfType(POUCH_SLOT);
    }
}
