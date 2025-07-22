package dev.rndmorris.salisarcana.common.compat;

import baubles.api.expanded.BaubleExpandedSlots;
import dev.rndmorris.salisarcana.config.SalisConfig;

public class BaublesExpandedCompat {

    public static final String POUCH_SLOT = "focus_pouch";

    public static void initialize() {
        if (!SalisConfig.modCompat.baublesExpanded.isEnabled()) return;

        if (SalisConfig.modCompat.baublesExpanded.focusPouchSlot.isEnabled()) {
            BaubleExpandedSlots.tryRegisterType(POUCH_SLOT);
            BaubleExpandedSlots.tryAssignSlotOfType(POUCH_SLOT);
        }
    }
}
