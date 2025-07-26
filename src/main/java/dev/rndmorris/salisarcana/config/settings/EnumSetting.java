package dev.rndmorris.salisarcana.config.settings;

import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.core.SalisArcanaCore;

public class EnumSetting<E extends Enum<E>> extends Setting {

    protected final Class<E> enumClass;
    protected final String name;
    protected final String comment;
    protected final @Nullable E disabledValue;
    protected E value;

    /**
     * Create a Setting with a fixed set of options based on an Enum
     *
     * @param dependency    The parent dependency of this option
     * @param name          The name of this configuration, as used in the .cfg file
     * @param comment       The informational comment displayed above the configuration.
     * @param defaultValue  The default value of the configuration when it isn't set.
     * @param disabledValue The value of the config that makes it "disabled". Set to null if none of the options
     *                      "disable" the setting.
     */
    public EnumSetting(IEnabler dependency, String name, String comment, @Nonnull E defaultValue,
        @Nullable E disabledValue) {
        super(dependency);
        this.name = name;
        enumClass = defaultValue.getDeclaringClass();
        value = defaultValue;
        this.disabledValue = disabledValue;

        this.comment = comment + " Valid values: " + Arrays.toString(enumClass.getEnumConstants());
    }

    public E getValue() {
        return this.value;
    }

    @Override
    public boolean isEnabled() {
        return this.value != this.disabledValue && super.isEnabled();
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        final var validValues = Arrays.stream(enumClass.getEnumConstants())
            .map(Enum::toString)
            .toArray(String[]::new);
        final var valueString = configuration.getString(name, getCategory(), value.toString(), comment, validValues);

        try {
            value = Enum.valueOf(enumClass, valueString);
        } catch (IllegalArgumentException e) {
            // Don't do anything, just use the default value
            SalisArcanaCore.LOG.error(
                "Invalid enum value for config \"{}\": {}. Value must be one of: {}",
                this.name,
                valueString,
                String.join(", ", validValues));
        }
    }
}
