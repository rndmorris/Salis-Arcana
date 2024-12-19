package dev.rndmorris.salisarcana.lib;

import javax.annotation.Nullable;

import net.minecraft.util.EnumChatFormatting;

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

    @Nullable
    public static EnumChatFormatting findByFormattingCode(char formattingCode) {
        for (var value : EnumChatFormatting.values()) {
            if (value.getFormattingCode() == formattingCode) {
                return value;
            }
        }
        return null;
    }
}
