package dev.rndmorris.tfixins.lib;

public class EnumHelper {

    public static <E extends Enum<E>> E tryParseEnum(E[] values, String name) {
        for (var val : values) {
            if (val.toString()
                .equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }
}
