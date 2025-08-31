package dev.rndmorris.salisarcana.lib;

import dev.rndmorris.salisarcana.core.SalisArcanaCore;

public enum ObfuscationInfo {
    // spotless:off

    ADD_COLLISION_BOXES_TO_LIST("func_149743_a", "addCollisionBoxesToList"),
    GET_SELECTED_BOUNDING_BOX_FROM_POOL("func_149633_g", "getSelectedBoundingBoxFromPool"),
    HARVEST_LEVEL("field_78001_f", "harvestLevel"),

    ;

    // spotless:on

    private final String obfName;
    private final String deobfName;

    ObfuscationInfo(String obfName, String deobfName) {
        this.obfName = obfName;
        this.deobfName = deobfName;
    }

    public String getName() {
        return SalisArcanaCore.isObfuscated ? obfName : deobfName;
    }
}
