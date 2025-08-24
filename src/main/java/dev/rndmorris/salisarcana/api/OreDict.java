package dev.rndmorris.salisarcana.api;

/**
 * Ore dictionary names used by Salis Arcana.
 */
public class OreDict {

    /**
     * Blocks that will be used as heat sources for the crucible and thaumatorium.
     */
    public static final String HEAT_SOURCE = "salisarcana:heatSource";
    public static final String GREATWOOD_PLANKS = "plankGreatwood";
    public static final String SILVERWOOD_PLANKS = "plankSilverwood";

    /**
     * Blocks with this oredict tag will always be included in the output of the Pickaxe of the Core's right-click
     * ability.
     */
    public static final String ELEMENTAL_PICK_SCAN_INCLUDE = "salisarcana:elementalPickScanInclude";

    /**
     * Blocks with this oredict tag will not be included in the output of the Pickaxe of the Core's right-click ability,
     * even if they normally would be.
     */
    public static final String ELEMENTAL_PICK_SCAN_EXCLUDE = "salisarcana:elementalPickScanExclude";
}
