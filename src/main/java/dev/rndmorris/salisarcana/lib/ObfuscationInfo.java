package dev.rndmorris.salisarcana.lib;

import dev.rndmorris.salisarcana.core.SalisArcanaCore;

public enum ObfuscationInfo {
    // spotless:off

    HARVEST_LEVEL("field_78001_f", "harvestLevel"),
    POTION_TYPES("field_76425_a", "potionTypes"),
    ;

    // spotless:on

    private final String obfName;
    private final String deobfName;

    ObfuscationInfo(String obfName, String deobfName) {
        this.obfName = obfName;
        this.deobfName = deobfName;
    }

    public String getName() {
        return SalisArcanaCore.isObf() ? obfName : deobfName;
    }
}
