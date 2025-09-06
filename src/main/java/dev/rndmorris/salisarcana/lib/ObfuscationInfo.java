package dev.rndmorris.salisarcana.lib;

import dev.rndmorris.salisarcana.core.SalisArcanaCore;

public enum ObfuscationInfo {
    // spotless:off

    ADD_COLLISION_BOXES_TO_LIST("func_149743_a", "addCollisionBoxesToList"),
    GET_SELECTED_BOUNDING_BOX_FROM_POOL("func_149633_g", "getSelectedBoundingBoxFromPool"),
    SET_BLOCK_BOUNDS("func_149676_a", "setBlockBounds"),
    SET_RENDER_BOUNDS_FROM_BLOCK("func_147775_a", "setRenderBoundsFromBlock"),
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
        return SalisArcanaCore.isObfuscated ? obfName : deobfName;
    }
}
