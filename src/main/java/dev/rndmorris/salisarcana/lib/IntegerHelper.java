package dev.rndmorris.salisarcana.lib;

public class IntegerHelper {

    public static Integer tryParse(String val) {
        try {
            return Integer.parseInt(val);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Integer tryParseWithMin(String val, int min) {
        final var result = tryParse(val);
        if (result != null && result < min) {
            return null;
        }
        return result;
    }

    public static Integer tryParseHexInteger(String hexInteger) {

        var color = hexInteger;
        while (color.length() >= 2 && color.substring(0, 2)
            .equalsIgnoreCase("0x")) {
            color = color.substring(2);
        }

        try {
            return Integer.parseInt(color, 16);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
