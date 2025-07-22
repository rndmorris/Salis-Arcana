package dev.rndmorris.salisarcana.common.compat;

public class ModCompat {

    public static void preInit() {
        BaublesExpandedCompat.initialize();
    }

    public static void init() {
        UBCCompat.init();
    }

}
