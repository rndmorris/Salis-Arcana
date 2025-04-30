package dev.rndmorris.salisarcana.config.settings;

import static dev.rndmorris.salisarcana.lib.IntegerHelper.tryParseHexInteger;

import net.minecraftforge.common.config.Configuration;

import org.apache.commons.lang3.text.WordUtils;

import dev.rndmorris.salisarcana.config.IEnabler;

public class HexColorSetting extends Setting {

    protected final String name;
    protected final String comment;

    protected final String defaultHexString;

    private int colorValue = -1;

    public HexColorSetting(IEnabler dependency, String name, String comment, String defaultHexString) {
        super(dependency);

        this.name = name;
        this.comment = comment;
        this.defaultHexString = defaultHexString;
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        enabled = configuration.getBoolean(
            "enable" + WordUtils.capitalizeFully(name)
                .replace(" ", ""),
            getCategory(),
            false,
            "Enable " + WordUtils.capitalizeFully(name));

        final var colorString = configuration.getString(name, getCategory(), defaultHexString, comment);
        final var colorInt = tryParseHexInteger(colorString);
        if (colorInt != null) {
            colorValue = colorInt;
        }
    }

    public int getColorValue() {
        return colorValue;
    }
}
