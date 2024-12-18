package dev.rndmorris.tfixins.config;

import static dev.rndmorris.tfixins.lib.IntegerHelper.tryParseHexInteger;

import net.minecraftforge.common.config.Configuration;

import org.apache.commons.lang3.text.WordUtils;

public class HexColorSetting extends Setting {

    protected final String category;
    protected final String name;
    protected final String comment;

    protected final String defaultHexString;

    private int colorValue = -1;

    public HexColorSetting(IConfigModule module, ConfigPhase phase, String category, String name, String comment,
        String defaultHexString) {
        super(module, phase);

        this.category = category;
        this.name = name;
        this.comment = comment;
        this.defaultHexString = defaultHexString;
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        enabled = configuration.getBoolean(
            "enable" + WordUtils.capitalizeFully(name)
                .replace(" ", ""),
            category,
            false,
            "Enable " + WordUtils.capitalizeFully(name));

        final var colorString = configuration.getString(name, category, defaultHexString, comment);
        final var colorInt = tryParseHexInteger(colorString);
        if (colorInt != null) {
            colorValue = colorInt;
        }
    }

    public int getColorValue() {
        return colorValue;
    }
}
