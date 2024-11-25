package dev.rndmorris.tfixins.lib;

public class IntegerHelper {

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
